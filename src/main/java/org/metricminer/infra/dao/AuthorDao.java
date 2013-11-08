package org.metricminer.infra.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.metricminer.model.Author;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class AuthorDao {

	private Session session;

	public AuthorDao(Session session) {
		this.session = session;
	}
	
	public Long totalAuthors() {
		return (Long) session.createQuery("select count(id) from Author").uniqueResult();
	}

	public Author find(String name) {
		return (Author) session.createCriteria(Author.class).setCacheable(true).add(
                Restrictions.eq("name", name)).uniqueResult();
	}

	public void save(Author author) {
		session.save(author);
	}
	
}
