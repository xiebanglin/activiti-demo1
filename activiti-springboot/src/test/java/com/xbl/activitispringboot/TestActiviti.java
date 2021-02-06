package com.xbl.activitispringboot;

import com.xbl.activitispringboot.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TestActiviti {

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RepositoryService repositoryService;


    /**
     * 查看流程定义内容
     * Activiti7可以自动部署流程
     */
    @Test
    public void findProcess() {

        securityUtil.logInAs("jack");
        Page<ProcessDefinition> definitionPage = processRuntime.processDefinitions(Pageable.of(0, 10));
        log.info("可用的流程定义总数:{}", definitionPage.getTotalItems());

        for (ProcessDefinition processDefinition : definitionPage.getContent()) {
            log.info("==========================================");
            log.info("流程定义内容:{}", processDefinition);
            log.info("==========================================");
        }

        // springboot正常来说会自动部署
//        repositoryService.createDeployment().name("mydemo")
//                .addClasspathResource("processes/mydemo.bpmn")
//                .deploy();
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess() {
        // 设置登录用户
        securityUtil.logInAs("system");
        ProcessInstance processInstance = processRuntime
                .start(ProcessPayloadBuilder.start().withProcessDefinitionKey("mydemo").build());
        log.info("流程实例的内容：{}", processInstance);
    }

    /**
     * 执行任务
     */
    @Test
    public void doTask() {

        // 设置登录用户
//        securityUtil.logInAs("rose");
        securityUtil.logInAs("jerry");
        // 查询任务
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 10));
        // 拾任务
        if (taskPage != null && taskPage.getTotalItems() > 0) {
            for (Task task : taskPage.getContent()) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                log.info("任务内容:{}", task);
                // 完成任务
                taskRuntime.complete(TaskPayloadBuilder
                        .complete()
                        .withTaskId(task.getId())
                        .build());
            }
        }


    }
}
