package com.gitee.starblues.extension.mybatis;

import com.gitee.starblues.extension.AbstractExtension;
import com.gitee.starblues.extension.mybatis.group.MybatisConfigGroup;
import com.gitee.starblues.extension.mybatis.group.PluginEntityAliasesGroup;
import com.gitee.starblues.extension.mybatis.group.PluginMapperGroup;
import com.gitee.starblues.extension.mybatis.mybatisplus.MybatisPlusProcessor;
import com.gitee.starblues.extension.mybatis.tkmyabtis.TkMybatisProcessor;
import com.gitee.starblues.factory.process.pipe.PluginPipeProcessorExtend;
import com.gitee.starblues.factory.process.pipe.bean.PluginBeanRegistrarExtend;
import com.gitee.starblues.factory.process.pipe.classs.PluginClassGroupExtend;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis 扩展
 * @author starBlues
 * @version 2.4.0
 */
public class SpringBootMybatisExtension extends AbstractExtension {

    private static final String KEY = "SpringBootMybatisPlusExtension";

    private final Type type;

    /**
     * 初始化扩展
     * @param type 根据当前环境所集成的框架来选择Type类型
     */
    public SpringBootMybatisExtension(Type type) {
        if(type == null){
            this.type = Type.MYBATIS;
        } else {
            this.type = type;
        }
    }

    @Override
    public String key() {
        return KEY;
    }

    @Override
    public void initialize(ApplicationContext mainApplicationContext) throws Exception {
    }

    @Override
    public List<PluginClassGroupExtend> getPluginClassGroup(ApplicationContext mainApplicationContext) {
        final List<PluginClassGroupExtend> pluginClassGroups = new ArrayList<>();
        pluginClassGroups.add(new MybatisConfigGroup());
        pluginClassGroups.add(new PluginEntityAliasesGroup());
        pluginClassGroups.add(new PluginMapperGroup());
        return pluginClassGroups;
    }

    @Override
    public List<PluginBeanRegistrarExtend> getPluginBeanRegistrar(ApplicationContext mainApplicationContext) {
        final List<PluginBeanRegistrarExtend> pluginBeanRegistrarExtends = new ArrayList<>(3);
        if(type == Type.MYBATIS_PLUS){
            pluginBeanRegistrarExtends.add(new MybatisPlusProcessor());
        } else if(type == Type.TK_MYBATIS){
            pluginBeanRegistrarExtends.add(new TkMybatisProcessor());
        } else {
            pluginBeanRegistrarExtends.add(new MybatisProcessor());
        }
        return pluginBeanRegistrarExtends;
    }

    public enum Type{
        MYBATIS,
        MYBATIS_PLUS,
        TK_MYBATIS
    }

}
