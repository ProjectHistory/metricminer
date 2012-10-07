package org.metricminer.tasks.runner;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.TaskQueueStatus;

import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.tasks.scheduler.Scheduled;


@PrototypeScoped
@Scheduled(cron = "0/10 * * * * ?")
public class TaskQueueCleanerJob implements br.com.caelum.vraptor.tasks.Task {

    private final Session daoSession;
    private final TaskQueueStatus queueStatus;
    private final TaskDao taskDao;
    private Logger log = Logger.getLogger(TaskQueueCleanerJob.class);

    public TaskQueueCleanerJob(SessionFactory sessionFactory, TaskQueueStatus queueStatus) {
        this.daoSession = sessionFactory.openSession();
        this.queueStatus = queueStatus;
        this.taskDao = new TaskDao(daoSession);
    }
    
    @Override
    public void execute() {
        log.debug("Running TaskQueueCleanerJob...");
        List<Task> tasksNotRunning = queueStatus.cleanTasksNotRunning();
        daoSession.beginTransaction();
        for (Task task : tasksNotRunning) {
            if (task.hasStarted()) {
                task.setFailed();
                taskDao.update(task);
            }
        }
        daoSession.getTransaction().commit();
    }

}
