package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.LoginEvent;
import EventModel.event.eventType.EventType;
import EventModel.event.Event;
import EventModel.handler.EventHandler;

/**
 * Created by lenovo on 2016/9/9.
 */
@BpmEventHandler(value = EventType.LOGIN,ORDER = 4)
public class HappyHandler implements EventHandler {


    @Override
    public void handlerEvent(Event event) {

        if(event instanceof LoginEvent) {
            LoginEvent loginEvent = (LoginEvent) event;
            System.out.println("事件类型为"+event.getEventType()+"处理的handler"+this.getClass().getName()
                    +loginEvent.getIP()+":::::"+loginEvent.getLoginTime());
        }


    }
}
