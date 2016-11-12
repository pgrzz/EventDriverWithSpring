package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.CountEvent;
import EventModel.event.Event;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;

/**
 * Created by lenovo on 2016/10/9.
 */
@BpmEventHandler(value = EventType.CountEvent,ORDER = 2)
public class ZhekouUserHandler implements EventHandler {


    @Override
    public void handlerEvent(Event event) {
        //在这里对具体的数据进行service 处理
       if(event instanceof CountEvent){
           CountEvent countEvent=(CountEvent)event;
           System.out.println("count!!!!!!!!"+countEvent.getZhekou());
       }

    }
}
