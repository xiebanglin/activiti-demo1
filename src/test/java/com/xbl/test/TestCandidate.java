package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class TestCandidate {

    /**
     * 流程部署
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
                .name("出差申请-候选人")
                .addClasspathResource("bpmn/evection-candidate.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id-" + deploy.getId());
        System.out.println("流程部署名字-" + deploy.getName());
    }

    /**
     * 启动流程
     */
    @Test
    public void testStartProcess() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        String key = "evection-candidate";
        runtimeService.startProcessInstanceByKey(key);
    }

    /**
     * 查询组任务
     */
    @Test
    public void findGroupList() {

        // 流程定义key
        String key = "evection-candidate";
        // 任务候选人
        String candidateUser = "wangwu";
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();
        // 查询组任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser)  // 根据候选人查询任务
                .list();
        for (Task task : taskList) {
            System.out.println("==============================");
            System.out.println("流程实例id:" + task.getProcessInstanceId());
            System.out.println("任务id:" + task.getId());
            System.out.println("负责人:" + task.getAssignee());
        }
    }


    /**
     * 拾取组任务
     */
    @Test
    public void claimTask() {
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 获取要拾取的任务id
        String taskId = "55002";
        // 任务候选人
        String candidateUser = "wangwu";
        // 拾取任务
        // 即使该用户不是候选人也能拾取renwu
        // 校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();

        if (task != null) {
            taskService.claim(taskId, candidateUser);
            System.out.println("taskId:" + taskId + "用户：" + candidateUser + "拾取任务完成");
        }
    }

    /**
     * 归还任务
     */
    @Test
    public void testAssigneeToGroupTask() {

        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 获取要拾取的任务id
        String taskId = "55002";
        // 任务候选人
        String assignee = "wangwu";
        // 拾取任务
        // 即使该用户不是候选人也能拾取renwu
        // 校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();

        if (task != null) {
            // 归还任务,就是把负责人设置为null
            taskService.setAssignee(taskId, null);
            System.out.println("taskId:" + taskId + "用户：" + assignee + "归还任务完成");
        }
    }
    /**
     * 任务交接
     */
    @Test
    public void testAssigneeToCandidateUser() {

        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 获取要拾取的任务id
        String taskId = "55002";
        // 任务负责人
        String assignee = "wangwu";
        // 任务候选人
        String candidateUser = "lisi";
        // 拾取任务
        // 即使该用户不是候选人也能拾取renwu
        // 校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();

        if (task != null) {
            // 归还任务,就是把负责人设置为null
            taskService.setAssignee(taskId, candidateUser);
            System.out.println("taskId:" + taskId + "用户：" + assignee + "交接任务完成");
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        // 流程定义key
        String key = "evection-candidate";
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee("汤姆")
                .singleResult();

        if (task != null) {
            taskService.complete(task.getId());
        }

    }
}
