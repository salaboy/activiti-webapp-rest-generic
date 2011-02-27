package org.activiti.rest.api.task;

import com.wordpress.salaboy.api.HumanTaskService;
import com.wordpress.salaboy.api.HumanTaskServiceFactory;
import com.wordpress.salaboy.conf.HumanTaskServiceConfiguration;
import com.wordpress.salaboy.smarttasks.activiti5wrapper.conf.ActivitiHumanTaskClientConfiguration;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.task.TaskEntity;
import org.activiti.rest.model.RestTask;
import org.activiti.rest.util.ActivitiRequest;
import org.activiti.rest.util.ActivitiWebScript;
import org.example.ws_ht.api.TTask;
import org.example.ws_ht.api.wsdl.IllegalArgumentFault;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;

/**
 * Returns info about a task.
 *
 * @author Erik Winlof
 */
public class TaskGet extends ActivitiWebScript {


  /**
   * Colelcts details about a task for the webscript template.
   *
   * @param req The webscripts request
   * @param status The webscripts status
   * @param cache The webscript cache
   * @param model The webscripts template model
   */
  @Override
  protected void executeWebScript(ActivitiRequest req, Status status, Cache cache, Map<String, Object> model) {
    String taskId = req.getMandatoryPathParameter("taskId");
    //TaskEntity task = (TaskEntity) getTaskService().createTaskQuery().taskId(taskId).singleResult();
    HumanTaskService humanTaskService = createHumanTaskService();
    TTask task = null;
        try {
            task = humanTaskService.getTaskInfo(taskId);
        } catch (IllegalArgumentFault ex) {
            Logger.getLogger(TaskGet.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    RestTask restTask = new RestTask(task);
    
    TaskFormData taskFormData = getFormService().getTaskFormData(taskId);
    if(taskFormData != null) {
      restTask.setFormResourceKey(taskFormData.getFormKey());     
    }
    
    model.put("task", restTask);
  }
  
   private HumanTaskService createHumanTaskService(){
        HumanTaskServiceConfiguration humanTaskServiceConfiguration = new HumanTaskServiceConfiguration();
        humanTaskServiceConfiguration.addHumanTaskClientConfiguration("Activiti", new ActivitiHumanTaskClientConfiguration("activity.cfg.xml"));
        return HumanTaskServiceFactory.newHumanTaskService(humanTaskServiceConfiguration);
   }
}
