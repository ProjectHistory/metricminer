package org.metricminer.tasks.query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.metricminer.config.MetricMinerConfigs;
import org.metricminer.infra.csv.SimpleCSVWriter;
import org.metricminer.infra.dao.QueryDao;
import org.metricminer.model.Task;
import org.metricminer.tasks.RunnableTask;
import org.metricminer.tasks.RunnableTaskFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.PrototypeScoped;
import br.com.caelum.vraptor.simplemail.Mailer;

@Component @PrototypeScoped
public class ExecuteQueryTaskFactory implements RunnableTaskFactory {

    private Mailer mailer;
	private SessionFactory sf;
    
    public ExecuteQueryTaskFactory(Mailer mailer, SessionFactory sf) {
        this.mailer = mailer;
		this.sf = sf;
    }

    @Override
	public RunnableTask build(Task task, Session session, StatelessSession statelessSession,
			MetricMinerConfigs config) {
        return new ExecuteQueryTask(task, 
        		new QueryExecutor(statelessSession, new SimpleCSVWriter(), new QueryProcessor()), 
        		new QueryDao(session), config, mailer, sf);
    }

}
