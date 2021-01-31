package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * url
 */
public class TestAssigneeUel {
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
                .name("出差申请流程-uel")
                .addClasspathResource("bpmn/evection-uel.bpmn")
                .addClasspathResource("bpmn/evection-uel.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id-" + deploy.getId());
        System.out.println("流程部署名字-" + deploy.getName());
    }

    @Test
    public void startAssigneeUel() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 设定assignee的值，用来替换uel表达式
        Map<String, Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0", "张三");
        assigneeMap.put("assignee1", "李经理");
        assigneeMap.put("assignee2", "王总经理");
        assigneeMap.put("assignee3", "赵财务");
        // 启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection1", assigneeMap);

    }
}
