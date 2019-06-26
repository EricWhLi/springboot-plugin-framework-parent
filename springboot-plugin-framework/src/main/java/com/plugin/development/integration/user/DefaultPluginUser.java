package com.plugin.development.integration.user;

import com.plugin.development.context.factory.name.PluginBeanNameDefine;
import com.plugin.development.context.factory.name.PluginBeanNameDefineFactory;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 默认插件使用者
 * @author zhangzhuo
 * @version 1.0
 */
public class DefaultPluginUser implements PluginUser{

    private final ApplicationContext applicationContext;
    private final PluginManager pluginManager;
    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    private final PluginBeanNameDefine pluginBeanNameDefine;

    public DefaultPluginUser(ApplicationContext applicationContext, PluginManager pluginManager) {
        Objects.requireNonNull(applicationContext);
        Objects.requireNonNull(pluginManager);
        this.applicationContext = applicationContext;
        this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        this.pluginManager = pluginManager;
        this.pluginBeanNameDefine = PluginBeanNameDefineFactory.get();
    }

    /**
     * 通过bean名称得到插件的bean。（Spring管理的bean）
     * @param name 插件bean的名称。spring体系中的bean名称。可以通过注解定义，也可以自定义生成。具体可百度
     * @param <T> bean的类型
     * @return 返回bean
     */
    @Override
    public <T> T getSpringDefineBean(String name){
        Object bean = applicationContext.getBean(name);
        return (T) bean;
    }

    /**
     * 在主程序中定义的接口。插件或者主程序实现该接口。可以该方法获取到实现该接口的所有实现类。（Spring管理的bean）
     * @param aClass 接口的类
     * @param <T> bean的类型
     * @return List
     */
    @Override
    public <T> List<T> getSpringDefineBeansOfType(Class<T> aClass){
        Map<String, T> beansOfTypeMap = applicationContext.getBeansOfType(aClass);
        if(beansOfTypeMap == null){
            return Collections.emptyList();
        }
        return beansOfTypeMap.values()
                .stream()
                .filter(beansOfType-> beansOfTypeMap != null)
                .collect(Collectors.toList());
    }

    /**
     * 在主程序中定义的接口。获取插件中实现该接口的实现类。（Spring管理的bean）
     * @param aClass 接口的类
     * @param <T> bean的类型
     * @return List
     */
    @Override
    public <T> List<T> getPluginSpringDefineBeansOfType(Class<T> aClass) {
        Map<String, T> beansOfTypeMap = applicationContext.getBeansOfType(aClass);
        if(beansOfTypeMap == null){
            return Collections.emptyList();
        }
        return beansOfTypeMap.keySet()
                .stream()
                .filter(name -> name != null && pluginBeanNameDefine.isPluginBeanName(name))
                .map(name -> beansOfTypeMap.get(name))
                .filter(bean -> bean != null)
                .collect(Collectors.toList());
    }

    /**
     * 在主程序中获取注入了插件实现接口的bean。（Spring管理的bean）
     * 该bean是在主程序中定义的。但是在该bean中通过 @Autowire 注解需要注入插件中实现了主程序中定义的接口。可以通过该函数获取该bean.
     * 如果直接使用注解 @Autowire 使用该bean的话。则无法获取到插件对主程序接口定义的实现类。
     * 比如：在主程序定义了：A 接口。在插件中实现了A接口：A-imp1类、A-imp2 类。
     *      在主程序中C类 通过注解注入A接口的实现类（@Autowire {@code List<A>} list），而在C类的 method_c 中调用并使用了 list，则在
     *       {@code List<A>} list 不存在插件对A的实现类（A-imp1类、A-imp2 类）
     *      假如主程序D调用了C类的method_c方法，则在D程序中不要使用注解注入C类，直接使用该方法获取C类的实例，然后调用，这样就能动态
     *      向{@code List<A>} 注入插件对A的实现类（A-imp1类、A-imp2 类）了。
     * 说明：该方法比较绕，因此在程序中不要直接注入对插件定义的接口。建议直接使用 getSpringDefineBeansOfType({@code Class<T>} aClass) 方法
     *      获取对接口aClass的实现。
     * @param aClass  bean的类型
     * @return 返回bean
     */
    @Override
    public <T> T getSpringAutowirePluginDefineBean(Class<T> aClass){
        T bean = applicationContext.getBean(aClass);
        autowireCapableBeanFactory.autowireBean(bean);
        return bean;
    }

    /**
     * 得到插件扩展接口实现的bean。（非Spring管理）
     * @param tClass 接口的类
     * @param <T> bean的类型
     * @return 返回bean
     */
    @Override
    public <T> List<T> getPluginExtensions(Class<T> tClass){
        return pluginManager.getExtensions(tClass);
    }


}
