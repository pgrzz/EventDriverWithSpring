package EventModel.annonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lenovo on 2016/9/21.
 *
 * 注解 必须配合其他 注解使用 比如 现在 默认的 controller 或者 service 事件等  该注解只是 提供了处理事件的方式
 *  当你定义了的该注解在你的方法上，那么 该 方法关心的事件将会交给 ScriptHandler来处理来处理
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ScriptEventDoEvents {

}
