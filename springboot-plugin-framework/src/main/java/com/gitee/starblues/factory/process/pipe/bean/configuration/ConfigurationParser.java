package com.gitee.starblues.factory.process.pipe.bean.configuration;

import com.gitee.starblues.factory.PluginRegistryInfo;

/**
 * 配置解析者
 * @author starBlues
 * @version 1.0
 */
public interface ConfigurationParser {

    /**
     * 配置解析
     * @param pluginRegistryInfo 插件信息
     * @param pluginConfigDefinition 插件配置定义
     * @return 解析后映射值的对象
     * @throws Exception 抛出配置解析异常
     */
    Object parse(PluginRegistryInfo pluginRegistryInfo, PluginConfigDefinition pluginConfigDefinition) throws Exception;

}
