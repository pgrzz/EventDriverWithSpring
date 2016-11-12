package EventModel.bus;



import EventModel.annonation.AllowConcurrentEvents;
import EventModel.annonation.BpmEventHandler;
import EventModel.event.DeadEvent;
import EventModel.event.Event;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;
import EventModel.handler.Impl.DefaultEventHandler;
import org.hibernate.bytecode.buildtime.spi.*;
import org.hibernate.bytecode.buildtime.spi.ExecutionException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lenovo on 2016/9/6.
 */
public class EventDispatcher   {

    //对注册了的handler 判断有没有触发的事件有的话就进行处理

    private  Map<EventType,List<EventHandler>>  eventHandlerMap=new ConcurrentHashMap<>(); // 定义成组播 因为关心这个事件的会有很多
    private  BlockingQueue<Event> blockingQueue=new LinkedBlockingQueue<>();
    private ReentrantLock lock=new ReentrantLock();

    private  ExecutorService pool= Executors.newFixedThreadPool(2);


    //Load xml 一个 handler 的视图

    private  volatile AtomicBoolean stop=new AtomicBoolean(false);     // 这里在高并发的情况下 非原子性操作

    public void run(){

        pool.submit(new Runnable() {
            @Override
            public void run() {
                while(!stop.get() ){
                    //判断事件触发
                    try {
                        Event event=  blockingQueue.take();
                        EventType eventType=  event.getEventType();   // 找到对应的注册了的handler
                        List<EventHandler> list =eventHandlerMap.get(eventType);
                        if(list.size()==0){
                            // 这里 如果是处理不了的事件   这样太不友好了 应该定义 deadEvent 来让 它处理
                          stop.compareAndSet(false,true);
                            throw new RuntimeException("you didn't registry same handler to this event"+event.getEventType());
                        }else {
                            pool.submit(new Runnable() {
                                @Override
                                public void run() {
                                    for(EventHandler eventHandler:list){
                                        if(event.getClass().getAnnotation(AllowConcurrentEvents.class)!=null){
                                            synchronized (event){
                                                eventHandler.handlerEvent(event);
                                            }
                                        }else{
                                            eventHandler.handlerEvent(event);
                                        }
                                    }

                                }
                            });

                        }

                    } catch (InterruptedException e) {
                        System.out.println("异常退出");
                      stop.compareAndSet(false,true);

                        e.printStackTrace();
                    }finally {

                        System.out.println("finally");
                    }


                }
            }
        });

  }

 // 当多个   handler关心同一个事件的时候 可以提供顺序给它

    public boolean register(EventType eventType, EventHandler eventHandler) throws Exception {  // 支持异步或者同步模式在这里考虑热插拔

        try {
            lock.lock();
            //先查看有没有对应的类型
            List<EventHandler> handlers = eventHandlerMap.get(eventType);
            if (handlers != null && handlers.size() > 0) {
                //判断 有没有该list
                if (handlers.contains(eventHandler)) {
                    throw new Exception("you have register this handler" + eventHandler + "once more");
                    // 这里要定义成自定义类型
                } else {
                    handlers.add(eventHandler);
                    //       eventHandlerMap.put(eventType,handlers);
                }
            } else {
                //没有事件对应的handlers
                List<EventHandler> newlist = new ArrayList<>();
                newlist.add(eventHandler);
                eventHandlerMap.put(eventType, newlist);
                return true;

            }
        }finally {
            lock.unlock();
        }
        return false;
    }

    public void pushTask(Event event){

        try {
         if(event.isSync())
                blockingQueue.put(event);
            else
             handlerEvent(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //  内部容器出问题不应该抛给客服
        }
    }

     // 移除所有的event对应的 handler
    public void remove(EventType eventType){
        try {
            lock.lock();
            eventHandlerMap.remove(eventType);
        }finally {
            lock.unlock();
        }

    }

    // 移除专门的handler
    public void remove(EventHandler eventHandler){

       EventType eventType =  eventHandler.getClass().getAnnotation(BpmEventHandler.class).value();
         if(eventType ==null){
             throw new RuntimeException("error handler, don't have the  Annotation BpmEventHandler ");
         }

        try {
            lock.lock();
            List<EventHandler> list = eventHandlerMap.get(eventType);
            list.remove(eventHandler);
            eventHandlerMap.put(eventType, list);
        }finally {
            lock.unlock();
        }

    }



    private void handlerEvent(Event event){

        //用户输入应该做检查的避免 NULL
        Optional<Event> optionalevent=Optional.of(event);
        try {
            optionalevent.orElseThrow(()->new RuntimeException("传入的事件类型为null"));
        }catch (Exception e){
            pushTask(new DeadEvent().setMethodName("handlerEvent").setErrorEvent("null").setErrorMessage("nullPoint").sync());
        }

        EventType eventType=  event.getEventType();
      // 当找不到 注解对应类型 value = EventType.CountEvent 会报错
        Optional<List<EventHandler>> optional=Optional.of(eventHandlerMap.get(eventType));
      try {
          for(EventHandler eventHandler:  optional.orElseThrow(()->new RuntimeException("没有对应的事件的handler"))){
              eventHandler.handlerEvent(event);
          }
        }catch (Throwable throwable){
            // 不应该有不能处理的事件,2 当出现 线程池跑飞 要对整一个 thorwable 抓住 再考虑恢复 而不是让错误蔓延到上层
          pushTask(new DeadEvent().setMethodName("handlerEvnet").setErrorEvent(event.toString()).setErrorHandler("").setErrorMessage(throwable.getMessage()).sync());
        }

    }

    public void handlerEvent(Event event,Class<? extends EventHandler> eventHandlerClass){
        EventType eventType=  event.getEventType();   // 找到对应的注册了的handler
        List<EventHandler> list =eventHandlerMap.get(eventType);
        for(EventHandler handler:list){
            if(handler.getClass()==eventHandlerClass){
                handler.handlerEvent(event);
            }
        }


    }



}
