package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key到activiti表
     */
    @Test
    public void addBusinessKey() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 启动流程的过程中添加businessKey
        // 第一个参数为流程定义的key， 第二个参数为businessKey
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        // 输出
        System.out.println("usinessKey:" + instance.getBusinessKey());
    }

    /**
     * 全部流程实例的 挂起 和 激活
     */
    @Test
    public void suspendAllProcessInstance() {

        // 获取流程实例
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 查询流程定义的信息,获取流程定义查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 获取当前流程定义实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 获取流程定义id
        String processDefinitionId = processDefinition.getId();
        // 如果是挂起，改为激活
        if (suspended) {
            // 执行激活操作
            // 参数一:流程定义的id,参数二：是否要激活，参数三：激活时间
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义id:" + processDefinitionId + "，已激活");
        } else {
            // 如果是激活，改为挂起
            // 参数一:流程定义的id,参数二：是否要暂停，参数三：暂停时间
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            System.out.println("流程定义id:" + processDefinitionId + "，已挂起");
        }
    }

    /**
     * 暂停单个流程实例
     */
    @Test
    public void suspendSingleProcessInstance() {

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 通过RuntimeService，获取流程实例的对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("25001")
                .singleResult();
        // 获取流程实例的暂停状态
        boolean suspended = instance.isSuspended();
        // 获取流程实例的id
        String instanceId = instance.getId();
        // 判断是否已经暂停，如果暂停，就执行激活
        if (suspended) {
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例：" + instanceId + "已激活");
        } else {
            // 如果激活，执行暂停操作
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例：" + instanceId + "已暂停");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completTask() {

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 使用TaskService获取任务,参数为流程实例id,负责人
        Task task = taskService.createTaskQuery()
                .processInstanceId("25001")
                .taskAssignee("zhangsan")
                .singleResult();
        System.out.println("流程实例id:" + task.getProcessInstanceId());
        System.out.println("流程任务id:" + task.getId());
        System.out.println("负责人:" + task.getAssignee());
        System.out.println("任务名称:" + task.getName());
        // 根据任务id完成任务
        taskService.complete(task.getId());
    }
}
