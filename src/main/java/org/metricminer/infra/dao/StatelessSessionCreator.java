package org.metricminer.infra.dao;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class StatelessSessionCreator implements ComponentFactory<StatelessSession> {
	
	private SessionFactory sf;
	private StatelessSession session;

	public StatelessSessionCreator(SessionFactory sf) {
		this.sf = sf;
	}
	
	@PostConstruct
	public void init() {
		session = sf.openStatelessSession();
	}

	@Override
	public StatelessSession getInstance() {
		return session;
	}

}
