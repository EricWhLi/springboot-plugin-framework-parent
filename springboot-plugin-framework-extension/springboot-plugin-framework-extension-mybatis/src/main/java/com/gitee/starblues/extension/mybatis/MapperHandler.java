package com.gitee.starblues.extension.mybatis;

import com.gitee.starblues.extension.mybatis.group.PluginMapperGroup;
import com.gitee.starblues.factory.PluginRegistryInfo;
import com.gitee.starblues.factory.process.pipe.bean.name.PluginAnnotationBeanNameGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.context.support.GenericApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mapper 接口处理者
 * @author starBlues
 * @version 2.4.0
 */
public class MapperHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperHandler.class);

    private static final String MAPPER_INTERFACE_NAMES = "MybatisMapperInterfaceNames";

    private final ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();


    public MapperHandler() {
    }

    /**
     * 处理插件中的Mapper
     * @param pluginRegistryInfo 插件信息
     * @param processMapper Mapper的具体处理者
     */
    public void processMapper(PluginRegistryInfo pluginRegistryInfo,
                              MapperHandler.ProcessMapper processMapper){
        GenericApplicationContext applicationContext = pluginRegistryInfo.getPluginApplicationContext();
        List<Class<?>> groupClasses = pluginRegistryInfo.getGroupClasses(PluginMapperGroup.GROUP_ID);
        if(groupClasses == null || groupClasses.isEmpty()){
            return;
        }
        String pluginId = pluginRegistryInfo.getPluginWrapper().getPluginId();
        Set<String> beanNames = new HashSet<>();
        for (Class<?> groupClass : groupClasses) {
            if (groupClass == null) {
                continue;
            }
            BeanNameGenerator beanNameGenerator = new PluginAnnotationBeanNameGenerator(pluginId);
            AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(groupClass);
            ScopeMetadata scopeMetadata = scopeMetadataResolver.resolveScopeMetadata(abd);
            abd.setScope(scopeMetadata.getScopeName());
            String beanName = beanNameGenerator.generateBeanName(abd, applicationContext);
            BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
            AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
            BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, applicationContext);
            try {
                processMapper.process(definitionHolder, groupClass);
                beanNames.add(beanName);
            } catch (Exception e) {
                LOGGER.error("process mapper '{}' error. {}", groupClass.getName(), e.getMessage(), e);
            }
        }
        pluginRegistryInfo.addExtension(MAPPER_INTERFACE_NAMES, beanNames);
    }


    /**
     * 公共注册生成代理Mapper接口
     * @param holder ignore
     * @param mapperClass ignore
     * @param sqlSessionFactory ignore
     * @param sqlSessionTemplate ignore
     */
    public void commonProcessMapper(BeanDefinitionHolder holder,
                                    Class<?> mapperClass,
                                    SqlSessionFactory sqlSessionFactory,
                                    SqlSessionTemplate sqlSessionTemplate) {
        GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
        definition.getConstructorArgumentValues().addGenericArgumentValue(mapperClass);
        definition.setBeanClass(MapperFactoryBean.class);
        definition.getPropertyValues().add("addToConfig", true);
        definition.getPropertyValues().add("sqlSessionFactory", sqlSessionFactory);
        definition.getPropertyValues().add("sqlSessionTemplate", sqlSessionTemplate);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
    }



    @FunctionalInterface
    public interface ProcessMapper{
        void process(BeanDefinitionHolder holder, Class<?> mapperClass) throws Exception;
    }


}
