package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.eventType.EventType;
import EventModel.event.Event;

/**
 * Created by lenovo on 2016/9/7.
 */
@BpmEventHandler(value = EventType.CONTROLLER,ORDER = 1)
public class LeaveControllerHandler extends DefaultEventHandler {


    @Override
    public void handlerEvent(Event event) {


        System.out.println("事件类型为"+event.getEventType()+"处理的handler"+this.getClass().getName());
    }
}
