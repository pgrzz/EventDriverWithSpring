package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.Event;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;

/**
 * Created by lenovo on 2016/9/12.
 */

@BpmEventHandler(value = EventType.SERVICE,ORDER = 1)
public class ServiceHandler1 implements EventHandler {

    @Override
    public void handlerEvent(Event event) {
         System.out.println("事件类型"+event.getEventType());
    }
}
