package org.metricminer.infra.dao;

import java.util.List;

import org.hibernate.Session;
import org.metricminer.model.Query;
import org.metricminer.model.QueryResult;
import org.metricminer.model.User;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class QueryDao {
    private static final int PAGE_SIZE = 20;
	private Session session;

    public QueryDao(Session session) {
        this.session = session;
    }

    public void save(Query query) {
        session.save(query);
        session.flush();
    }

    public Query findBy(Long id) {
        return (Query) session.load(Query.class, id);
    }

    public void update(Query query) {
        session.update(query);
    }
    
    @SuppressWarnings("unchecked")
    public List<Query> doesntBelongTo(User user, int page) {
    	return session.createQuery("select q from Query q where q.author != :myself order by submitDate desc")
    	.setParameter("myself", user)
	    .setMaxResults(PAGE_SIZE)
	    .setFirstResult((page-1) * PAGE_SIZE)
    	.list();
    }

    public long pagesForDoesntBelongTo(User user) {
    	Long total = (Long)session.createQuery("select count(q) from Query q where q.author != :myself")
    			.setParameter("myself", user)
    			.uniqueResult();
    	
	    double pages = (double) total/(double)  PAGE_SIZE;
        return (long) Math.ceil(pages);
    }

	public void save(QueryResult result) {
		session.save(result);
	}

	@SuppressWarnings("unchecked")
	public List<Query> belongsTo(User user) {
    	return session.createQuery("select q from Query q where q.author = :myself order by submitDate desc")
    	.setParameter("myself", user)
    	.list();
	}

	public void refreshSession(Session session) {
		this.session = session;
		
	}

	public void detach(Query query) {
		session.evict(query);
	}

}
