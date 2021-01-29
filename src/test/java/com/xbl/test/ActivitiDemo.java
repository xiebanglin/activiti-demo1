package com.xbl.test;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {
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
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id-" + deploy.getId());
        System.out.println("流程部署名字-" + deploy.getName());
    }

    /**
     * 启动流程实例
     * <p>
     * `act_hi actinst'流程实例执行历史信息
     * "act_hiidntitylink`流程参与用户的历史信息
     * `act_hiprocinst`
     * 流程实例的历史信息
     * `act_hi_taskinst`
     * 流程任务的历史信息
     * "act_ru_execution`
     * 流程执行信息
     * * `act_ru_identitylink·―流程的正在参与用户信息
     * 'act_ru_task
     * 流程当前任务信息
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
     * 查询个人执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、根据流程的key 和 任务的负责人 来查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") // 要查询的负责人
                .list();
        // 4、输出
        for (Task task : list) {
            System.out.println("流程实例的id:" + task.getProcessInstanceId());
            System.out.println("任务id:" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
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

    /**
     * 使用zip进行批量部署
     */
    @Test
    public void testDeployProcessByZip() {

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 流程部署
        // 读取资源包文件，构造成InputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 使用压缩包的流，进行流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println("流程部署的ID" + deploy.getId());
        System.out.println("流程部署的名字" + deploy.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition() {
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositiryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 查询当前所有的流程定义
        // processDefinitionKey根据流程定义key查询
        // orderByProcessDefinitionVersion根据version倒序排序
        // list查所有的内容
        List<ProcessDefinition> list = processDefinitionQuery
                .processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        // 输出
        for (ProcessDefinition processDefinition : list) {
            System.out.println("流程定义的id:" + processDefinition.getId());
            System.out.println("流程定义的名称:" + processDefinition.getName());
            System.out.println("流程定义的key:" + processDefinition.getKey());
            System.out.println("流程定义的版本:" + processDefinition.getVersion());
            System.out.println("流程部署id:" + processDefinition.getDeploymentId());

        }

    }

    /**
     * 删除部署
     * <p>
     * 当前的流程如果并没有完成，想要删除的话，需要使用特殊方式，原理就是级联删除
     */
    @Test
    public void deleteDepolyment() {

        String deploymentId = "2501";
        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过引擎来获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 通过部署id来删除流程部署信息
//        repositoryService.deleteDeployment(deploymentId);
        // 级联删除
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 获下载资源文件
     * 方案一：使用activiti提供的api来资源文件,保存到文件目录
     * 方案二：自己写代码从数据库下载文件，使用jdbc对blob 类型或者clob读取出来，保存到文件目录
     * 解决io操作：commons-io.jar
     * <p>
     * 使用方案一：RepositoryService
     */
    @Test
    public void getDeployment() throws IOException {

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取查询对象ProcessDefinitionQuery, 查询流程定义信息
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery.processDefinitionKey("myEvection")
                .singleResult();
        // 通过流程定义信息获取部署id
        String deploymentId = processDefinition.getDeploymentId();
        // 通过ProcessDefinitionQuery，传递部署id参数，读取资源信息(png 和 bpmn 文件)
        // 获取png图片的流
        // 从流程定义表获取png文件目录和名字
        String pngName = processDefinition.getDiagramResourceName();
        // 通过部署id和图片名字获取资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pngName);
        // 获取bpmn文件的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 构造OutputStream 流
        File pngFile = new File("D:/activitiFlowFile/evection.png");
        File bpmnFile = new File("D:/activitiFlowFile/evection.bpmn");
        FileOutputStream pngOutput = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutput = new FileOutputStream(bpmnFile);
        // 输入流和输出流的转换
        IOUtils.copy(pngInput, pngOutput);
        IOUtils.copy(bpmnInput, bpmnOutput);
        // 关闭流
        pngOutput.close();
        bpmnOutput.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 历史记录查询
     */
    @Test
    public void findHistoryInfo() {

        // 获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取actinst表的查询对象
        HistoricActivityInstanceQuery activityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 查询actinst表,条件
        // 根据InstanceId
//        activityInstanceQuery.processInstanceId("12501");
        // 根据DefinitionId
        activityInstanceQuery.processDefinitionId("myEvection:1:7504");
        // 排序
        activityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所以内容
        List<HistoricActivityInstance> list = activityInstanceQuery.list();
        // 输出
        for (HistoricActivityInstance hi : list) {
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("------------------------------");
        }

    }
}
