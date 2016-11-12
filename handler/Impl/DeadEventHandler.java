package EventModel.handler.Impl;

import EventModel.annonation.BpmEventHandler;
import EventModel.event.DeadEvent;
import EventModel.event.Event;
import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.ServiceRegistry;
import java.util.Optional;

/**
 * Created by lenovo on 2016/10/11.
 */
@BpmEventHandler(EventType.DeadEvent)
public class DeadEventHandler implements EventHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    @Override
    public void handlerEvent(Event event) {

      if(event instanceof DeadEvent){

             LOGGER.debug("event:"+((DeadEvent) event).getErrorEvent()+
                     "handler"+((DeadEvent) event).getErrorHandler()+
                     "method"+((DeadEvent) event).getMethodName()
             );

      }else{
          LOGGER.debug("!------------------------!");
      }


    }
}
