package org.metricminer.infra.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.metricminer.model.Artifact;
import org.metricminer.model.Project;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class ArtifactDao {
	
	private final Session session;

	public ArtifactDao(Session session) {
		this.session = session;
	}
	
	public Long totalArtifacts() {
		Query query = session.createQuery("select count(id) from Artifact");
		return (Long) query.uniqueResult();
	}

	public void save(Artifact artifact) {
		session.save(artifact);
	}

	public Artifact find(String name, Project project) {
		return (Artifact) session.createCriteria(Artifact.class).setCacheable(true).add(
                Restrictions.eq("name", name)).add(Restrictions.eq("project", project))
                .uniqueResult();
	}

}
