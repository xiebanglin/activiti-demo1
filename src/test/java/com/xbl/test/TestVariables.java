package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

/**
 * 流程变量
 */
public class TestVariables {

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
                .name("出差申请-variables")
                .addClasspathResource("bpmn/evection-global.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id-" + deploy.getId());
        System.out.println("流程部署名字-" + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {

        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        // 4、输出内容
        System.out.println("流程定义id:" + instance.getProcessDefinitionId());
        System.out.println("流程实例的id:" + instance.getId());
        System.out.println("当前活动的id" + instance.getActivityId());
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、根据任务id完成任务(完成张三的任务)
//        taskService.complete("12505");
// 获取jerry - myEvection对应的任务
//        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
//                .taskAssignee("jerry").singleResult();
// 获取jack - myEvection对应的任务
//        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
//                .taskAssignee("jack").singleResult();
// 获取rose - myEvection对应的任务
        Task task = taskService.createTaskQuery().processDefinitionKey("myEvection")
                .taskAssignee("rose").singleResult();

        System.out.println("流程实例的id:" + task.getProcessInstanceId());
        System.out.println("任务id:" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());

        taskService.complete(task.getId());
    }
}
