package com.gitee.starblues.extension.mybatis.tkmyabtis;

import com.gitee.starblues.extension.mybatis.MybatisCommonConfig;
import org.mybatis.spring.SqlSessionFactoryBean;
import tk.mybatis.mapper.entity.Config;

/**
 * springboot tk-mybatis 配置接口
 * @author zhangzhuo
 * @version 1.0
 * @since 2020-12-11
 */
public interface SpringBootTkMybatisConfig extends MybatisCommonConfig {



    /**
     * 插件自主配置Mybatis的 SqlSessionFactoryBean
     * SqlSessionFactoryBean 具体配置说明参考 Mybatis 官网
     */
    default void oneselfConfig(SqlSessionFactoryBean sqlSessionFactoryBean){
    }

    /**
     * 插件自主配置tk的 Config
     * Config 具体配置说明参考 https://gitee.com/free/Mapper/wikis/1.1-java?sort_id=208196
     * @param config Config
     */
    default void oneselfConfig(Config config){
    }


}
