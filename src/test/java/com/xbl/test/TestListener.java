package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class TestListener {
    /**
     * 测试流程部署
     */
    @Test
    public void testDeployment() {
        // 1、创建ProcessEngine
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用service进行流程部署,定义一个流程名字，把bpmn文件和png文件部署到数据库
        Deployment deploy = repositoryService.createDeployment()
                .name("测试监听器")
                .addClasspathResource("bpmn/demo-listen.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id-" + deploy.getId());
        System.out.println("流程部署名字-" + deploy.getName());
    }

    @Test
    public void testStartProcess() {

        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("testListener");
        // 4、输出内容
        System.out.println("流程定义id:" + instance.getProcessDefinitionId());
        System.out.println("流程实例的id:" + instance.getId());
        System.out.println("当前活动的id" + instance.getActivityId());
    }
}
