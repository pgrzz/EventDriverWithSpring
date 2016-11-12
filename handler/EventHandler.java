package EventModel.handler;



import EventModel.event.Event;

/**
 * Created by lenovo on 2016/9/6.
 */
public interface EventHandler {

      // 如果要做同构处理的话 ActivitiEventListener

      void   handlerEvent(Event event);




}
