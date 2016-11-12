package EventModel.event;


import EventModel.event.eventType.EventType;

/**
 * Created by lenovo on 2016/9/6.
 * 当一个事件分发到多个handler 的时候 会发生线程不安全
 */
public abstract class Event {

   private boolean isSync=false;

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    private EventType eventType;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Event sync(){
        isSync=true;
        return this;
    }

}
