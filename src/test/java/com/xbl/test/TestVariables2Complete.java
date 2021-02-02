package com.xbl.test;

import com.xie.demo.model.Evection;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务完成时添加变量
 */
public class TestVariables2Complete {

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
                .name("出差申请-variables-complete")
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

        // 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 流程变量的map
        Map<String, Object> variables = new HashMap<>();
        // 设置流程变量
//        Evection evection = new Evection();
//        evection.setNum(3d);
//        // 流程变量的实体类放入map
//        variables.put("evection", evection);
        // 设定任务负责人
        variables.put("assignee0", "李四3");
        variables.put("assignee1", "王经理3");
        variables.put("assignee2", "杨总经理3");
        variables.put("assignee3", "张财务3");
        // 根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("evection-global", variables);
        // 输出内容
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

        // 任务负责人
//        String assignee = "李四3";
//        String assignee = "王经理3";
        String assignee = "张财务3";
        // 设置流程变量
//        Evection evection = new Evection();
//        evection.setNum(2d);
//        Map<String,Object> map = new HashMap<>();
//        map.put("evection",evection);
        // 完成任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection-global")
                .taskAssignee(assignee)
                .singleResult();

        if (task != null) {
            // 根据任务id来完成任务
            // 传递流程变量
//            taskService.complete(task.getId(),map);
            taskService.complete(task.getId());
        }
    }
}
