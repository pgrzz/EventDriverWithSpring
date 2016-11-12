package EventModel.script;

import EventModel.event.CountEvent;
import EventModel.event.Event;
import EventModel.event.LoginEvent;
import groovy.lang.Binding;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.IOException;

/**
 * Created by lenovo on 2016/9/21.
 */
public class ScriptEngine {



   public void callScript(String scriptPath,Event event){

       String path="/F:/bpm/target/classes/test.groovy";

       String []roots=new String[]{path};

       try {
           GroovyScriptEngine engine = new GroovyScriptEngine(roots);
           Binding binding = new Binding();
           if (event instanceof CountEvent) {
               CountEvent countEvent = (CountEvent) event;

               GroovyObject groovyObject = (GroovyObject) engine.loadScriptByName("test.groovy").newInstance();

               Object objs[] = new Object[1];
               objs[0] = countEvent.getZhekou();
               int result = Integer.parseInt((String) groovyObject.invokeMethod("output", objs));


               countEvent.setZhekou(result);
           }

       } catch (IOException e) {
           e.printStackTrace();
       } catch (ResourceException e) {
           e.printStackTrace();
       } catch (InstantiationException e) {
           e.printStackTrace();
       } catch (IllegalAccessException e) {
           e.printStackTrace();
       } catch (ScriptException e) {
           e.printStackTrace();
       }

   }

}
