package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.bus.EventDispatcher;
import EventModel.event.Event;
import EventModel.event.ScriptEvent;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;
import EventModel.script.ScriptEngine;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2016/9/21.
 */
@BpmEventHandler(EventType.ScriptEvent)
public class ScriptHandler implements EventHandler {

     @Resource
    EventDispatcher eventDispatcher;


    @Override
    public void handlerEvent(Event event) {


        if(!(event instanceof ScriptEvent))
            return;   //传递到下一个handler
        ScriptEvent scriptEvent=(ScriptEvent)event;
        scriptEvent.getId();
        // 查询数据库得到 filePath
        Event event1=((ScriptEvent) event).getActionEvent();
        new ScriptEngine().callScript("test.groovy",event1);
        //应该这样 脚本发散完 把处理后的事件包装再进行发散就正确了
        eventDispatcher.pushTask(event1);



    }
}
