package com.xbl.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

public class TestCreate {

    /**
     * 使用activiti提供的默认方式来创建MySQL表
     */
    @Test
    public void testCreateTables() {

        // 需要使用activiti提供的工具类ProcessEngines,使用方法getDefaultProcessEngine，getDefaultProcessEngine
        // 会默认从resources下读取activiti.cfg.xml的文件
        // 创建ProcessEngine时就会创建表
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 自定义方式
        // 配置文件名字可以自定义，bean名字也可以自定义
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 获取流程引擎对象
        ProcessEngine processEngine = configuration.buildProcessEngine();

        System.out.println(processEngine);
    }
}
