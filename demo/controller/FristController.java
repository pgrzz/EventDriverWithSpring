package EventModel.demo.controller;


import EventModel.annonation.CountDoEvents;
import EventModel.annonation.LoginEvents;
import EventModel.annonation.ScriptEventDoEvents;
import EventModel.annonation.SyncEvents;
import EventModel.bus.EventDispatcher;
import EventModel.event.LoginEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2016/9/8.
 */

@Component
public class FristController {

    @Resource
    EventDispatcher eventDispatcher;


       @ScriptEventDoEvents
    @RequestMapping
     @CountDoEvents
    public void controller(){

        System.out.println("controlelr层");

    }

    public void check(String name,String pwd){
        if (name!=null){


             LoginEvent event=new LoginEvent();
             event.setSync(true);
             eventDispatcher.pushTask(event);
            //。。。。。。。。。。。。。。

           //
        }

    }


}
