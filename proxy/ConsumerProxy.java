package EventModel.proxy;

import EventModel.annonation.ScriptEventDoEvents;
import EventModel.annonation.SyncEvents;
import EventModel.event.*;
import EventModel.event.eventType.EventType;
import EventModel.bus.EventDispatcher;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 事件的自动分发 当在类或方法上面定义了发布的事件 则该类会自动去分发这些定义的事件
 * Created by lenovo on 2016/9/9.
 */
@Component
public class ConsumerProxy implements MethodInterceptor {


    @Resource
    EventDispatcher eventDispatcher;

    // 对于 消费模式还要分 异步同步的  对于现在这种情况的 脚本又要分两种 阻塞等待 和非阻塞等待


    /**
     * 类级注解调用
     * @param proxy
     */
    private void Classbefore(Class proxy){

        Event event;
        Annotation script;
        // 先 保存一个注解类型的集合
            Class clazz=proxy;
             script=clazz.getAnnotation(ScriptEventDoEvents.class); //看是不是脚本事件
            Annotation[] ans=  clazz.getAnnotations();
            event=process(script,ans);
            if(event==null){
                // 空事件投递保护
            }else {
                if(proxy.getAnnotation(SyncEvents.class)!=null)
                    event.setSync(true);
                    eventDispatcher.pushTask(event);

            }

    }

    /**
     * 方法级注解调用
     * @param method
     */
    private void methodBefore(Method method){
        Event event;
        Annotation script;
        script=method.getAnnotation(ScriptEventDoEvents.class);
        Annotation[] ans=method.getAnnotations();
        event=process(script,ans);
        if(event==null){
            // 空事件投递保护
        }else {
            if(method.getAnnotation(SyncEvents.class)!=null)
                event.setSync(true);
            eventDispatcher.pushTask(event);


        }

    }

    /**
     * 根据方法上面的 是否有ScriptEventDoEvents 决定是否包装成一个脚本事件或非脚本事件
     * @param script
     * @param ans
     * @return
     */
    private Event process(Annotation script,Annotation ans[]){
        Event event;
        if(script!=null){
            ScriptEvent scriptEvent=new ScriptEvent();
            //脚本事件
            scriptEvent.setActionEvent(decideEvent(ans));
            scriptEvent.setId(1);
            event=scriptEvent;
        }else {
            //非脚本事件
            event=decideEvent(ans);
        }
        return event;
    }


    /**
     *  用于得到具体的事件类型根据 Class or Method 上面的注解
     * @param ans
     * @return
     */
    private Event decideEvent(Annotation[] ans) {
        Event event = null;
        for (Annotation annotation : ans) {
            EventType eventType = EventType.TypeToEntity(annotation.annotationType().getSimpleName()); // 这里能这样使用是依赖于 注解名=类型名
            if (eventType == null) {
                continue;
            }
            // 这里有一个问题 那个 controller service 或者等 其他注解 是否可以 多重定义   如果多重定义的话就在下面都派发一次
            switch (eventType) {
                case CONTROLLER:
                    LoginEvent loginEvent = new LoginEvent();
                    loginEvent.setIP("192.168.220.130");
                    loginEvent.setLoginTime("16:31");
                    loginEvent.setEventType(eventType);
                    event = loginEvent;
                    break;
                case SERVICE:
                    ServiceEvent serviceEvent = new ServiceEvent();
                    serviceEvent.setName("haha");
                    serviceEvent.setEventType(eventType);
                    event = serviceEvent;
                    break;
                case LOGIN:
                    LoginEvent lg=new LoginEvent();
                    lg.setLoginTime("etet");
                    lg.setIP("111111111111111111");
                    lg.setEventType(EventType.LOGIN);
                    event=lg;
                    break;
                case CountEvent:
                    CountEvent countEvent=new CountEvent();
                    countEvent.setTask("frist");
                    countEvent.setZhekou(1);
                    countEvent.setEventType(EventType.CountEvent);
                    event=countEvent;
                    break;
            }
        }

        return event;
    }




    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
         boolean isMethod=false;
       // 先判断 方法上面有没有注解
      Class clazz=o.getClass().getSuperclass();
       for(Annotation annotation: method.getAnnotations()){
           EventType eventType=EventType.TypeToEntity(annotation.annotationType().getSimpleName());
           if(eventType!=null){
            isMethod=true;
           }
       }
          if(isMethod){
              methodBefore(method);
          }else {
              Classbefore(clazz);
          }

        Object result=methodProxy.invokeSuper(o,objects);

        return result;
    }
}
