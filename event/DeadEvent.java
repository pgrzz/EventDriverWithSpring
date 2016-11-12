package EventModel.event;

import EventModel.event.eventType.EventType;

/**
 * Created by lenovo on 2016/10/11.
 */
public class DeadEvent extends  Event {

    private String methodName;
    private String errorEvent;
    private String errorHandler;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public DeadEvent setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getErrorEvent() {
        return errorEvent;
    }

    public DeadEvent setErrorEvent(String errorEvent) {
        this.errorEvent = errorEvent;
        return  this;
    }

    public String getErrorHandler() {
        return errorHandler;
    }

    public DeadEvent setErrorHandler(String errorHandler) {
        this.errorHandler = errorHandler;
        return  this;
    }

    public String getMethodName() {
        return methodName;
    }

    public DeadEvent setMethodName(String methodName) {
        this.methodName = methodName;
        return  this;
    }

    public DeadEvent() {
        this.setEventType(EventType.DeadEvent);
    }
}
