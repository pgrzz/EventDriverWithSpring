package EventModel.proxy;

import net.sf.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用于创建代理类
 * Created by lenovo on 2016/9/9.
 */
@Component
public class ProxyFactory {

     @Resource
     ConsumerProxy consumerProxy;

     public  Object getProxyObj(String clazz)throws  Exception{

          Class<?> superclass=Class.forName(clazz);
          Enhancer enhancer=new Enhancer();
          enhancer.setSuperclass(superclass);
          enhancer.setCallback(consumerProxy);

          return enhancer.create();
     }

     public <T> T getProxyObj(Class<T> tClass){
          Class superclass=tClass;
          Enhancer enhancer=new Enhancer();
          enhancer.setSuperclass(superclass);
          enhancer.setCallback(consumerProxy);
          return (T) enhancer.create();
     }

}
