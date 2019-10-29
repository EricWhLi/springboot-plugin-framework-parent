package com.mybatisplus.plugin;

import com.gitee.starblues.extension.mybatis.configuration.SpringBootMybatisConfig;
import com.gitee.starblues.realize.BasePlugin;
import org.pf4j.PluginWrapper;

import java.util.HashSet;
import java.util.Set;

/**
 * description
 *
 * @author zhangzhuo
 * @version 1.0
 */
public class MybatisPlusPlugin extends BasePlugin implements SpringBootMybatisConfig {

    private final Set<String> mybatisMapperXmlLocationsMatch = new HashSet<>();


    public MybatisPlusPlugin(PluginWrapper wrapper) {
        super(wrapper);
        mybatisMapperXmlLocationsMatch.add("classpath:mapper/*Mapper.xml");
    }

    @Override
    protected void startEvent() {

    }

    @Override
    protected void deleteEvent() {

    }

    @Override
    protected void stopEvent() {

    }

    @Override
    public Set<String> mybatisMapperXmlLocationsMatch() {
        return mybatisMapperXmlLocationsMatch;
    }
}

