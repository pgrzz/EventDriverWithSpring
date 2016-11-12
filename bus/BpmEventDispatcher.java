package EventModel.bus;

import EventModel.event.eventType.EventType;
import EventModel.handler.EventHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2016/9/19.
 */
@Component
public class BpmEventDispatcher {

    @Resource
   private  EventDispatcher eventDispatcher; // 注入


    private Map<String,List<EventHandler>> processHandlers=new ConcurrentHashMap<>();

    public void register(String processId,String type,String handlerName){


        //得到事件类型
        EventType eventType =  EventType.TypeToEntity(type);
        try {
            Object handler= Class.forName(handlerName).newInstance();
            eventDispatcher.register(eventType,(EventHandler)handler);
           List<EventHandler> handlers= processHandlers.get(processId);
                if(handlers!=null && handlers.size() ==0 ){
                    // 新的
                    List<EventHandler> list =new ArrayList<>();
                    list.add((EventHandler)handler);
                    processHandlers.put(processId,list);
                }else{
                    List<EventHandler> list=processHandlers.get(processId);
                     list.add((EventHandler)handler);
                }

            // 容器对于这层错误要进行包装
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    // 根据 流程id 得到 具体的 监听器s

    public List<EventHandler> handlers(String processId){
         return processHandlers.get(processId);
    }



}
