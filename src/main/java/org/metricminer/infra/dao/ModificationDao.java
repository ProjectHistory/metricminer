package org.metricminer.infra.dao;

import org.hibernate.Session;
import org.metricminer.model.Modification;

public class ModificationDao {
	private Session session;

	public ModificationDao(Session session) {
		this.session = session;
	}

	public void save(Modification modification) {
		session.save(modification);
	}

}
