package EventModel.event;

import EventModel.event.eventType.EventType;

/**
 * Created by lenovo on 2016/9/21.
 */

public class ScriptEvent extends  Event {

    // 具体的事件
   private Event ActionEvent;

    // 从数据库带过来的 id
   private int id;

    public ScriptEvent() {
       setEventType(EventType.ScriptEvent);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Event getActionEvent() {
        return ActionEvent;
    }

    public void setActionEvent(Event actionEvent) {
        ActionEvent = actionEvent;
    }
}
