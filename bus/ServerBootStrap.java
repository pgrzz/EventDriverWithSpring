package EventModel.bus;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by lenovo on 2016/9/8.
 */
public class ServerBootStrap implements ApplicationContextAware {

   //配置策略是否要采用顺序  这里采用一个 config 类来 相关处理
    @Resource
    DispatcherConfig dispatcherConfig;

   private static ApplicationContext context;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          context=applicationContext;


           Map<String,Object> map= applicationContext.getBeansWithAnnotation(BpmEventHandler.class);
         Object objDis= applicationContext.getBean("eventDispatcher");
        if(!dispatcherConfig.isOrderBy()){
           for(Object obj:map.values()){
                  EventType eventType = obj.getClass().getAnnotation(BpmEventHandler.class).value();
             invokeMethod(eventType,obj,objDis);

           }
        }else{

            Map<EventType,Map<Integer,List<Object>> > mapOrder=new LinkedHashMap<>();

            for(Object obj:map.values()){

                EventType eventType = obj.getClass().getAnnotation(BpmEventHandler.class).value();
                int order =obj.getClass().getAnnotation(BpmEventHandler.class).ORDER();

                Map<Integer,List<Object>> listMap=mapOrder.get(eventType);

                 if(listMap!=null && mapOrder.get(eventType).get(order)!=null ) {
                     if( mapOrder.get(eventType).get(order).size()>0) {
                         List<Object> list = mapOrder.get(eventType).get(order);
                         list.add(obj);
                         Map<Integer, List<Object>> mapTemp = mapOrder.get(eventType);
                         mapTemp.put(order, list);
                         mapOrder.put(eventType, mapTemp);
                     }

                 }else{

                     List<Object> list=new LinkedList<>();
                     list.add(obj);
                     Map<Integer,List<Object>> tempmap=mapOrder.get(eventType);
                      if(tempmap==null){
                          tempmap=new HashMap<>();
                      }

                     tempmap.put(order, list);
                     mapOrder.put(eventType,tempmap);
                 }
            }

            for(EventType eventType :mapOrder.keySet()){

                Map<Integer,List<Object>> mapTemp=mapOrder.get(eventType);
                List<Integer> arrs=new ArrayList<>();
                arrs.addAll(mapTemp.keySet());
                 arrs.sort(new Comparator<Integer>() {
                     @Override
                     public int compare(Integer o1, Integer o2) {
                         int result;
                         result=o1>o2?-1:1;
                         return  result;
                     }
                 });
                 for(Integer index:arrs){
                     List<Object> list=mapTemp.get(index);
                      for(Object obj:list){
                          invokeMethod(eventType,obj,objDis);

                      }
                 }

            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
               EventDispatcher eventDispatcher=(EventDispatcher)objDis;
                eventDispatcher.run();
            }
        }).start();


    }

    private void invokeMethod(EventType eventType, Object handler, Object dis){
        try {
            Method method = EventDispatcher.class.getMethod("register", EventType.class, EventHandler.class);
            Object[] args = new Object[2];
            args[0]= eventType;
            args[1]=handler;

            method.invoke(dis,args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
