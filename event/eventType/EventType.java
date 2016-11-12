package EventModel.event.eventType;

/**
 * Created by lenovo on 2016/9/6.
 * 参数名必须和注解类名一样
 */
public enum EventType {


    CONTROLLER("Controller",1), // 这里的参数名必须和注解类名一样
    SERVICE("Service",2),
    DAO("DAO",3),
    ScriptEvent("ScriptEventDoEvents",4),
    CountEvent("CountDoEvents",5),
    DeadEvent("DeadDoEvents",6),

    // 方法级别的
    LOGIN("LoginEvents",7);


    private final String type;
    private final int val;




    EventType(String type, int val) {
        this.type = type;
        this.val = val;
    }



    public static EventType TypeToEntity(String type){
          for(EventType eventType :values()){
              if(eventType.type.equals(type)){
                  return eventType;
              }
          }
        return null;
    }



}
