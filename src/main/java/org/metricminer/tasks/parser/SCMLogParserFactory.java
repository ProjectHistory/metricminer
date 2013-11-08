package org.metricminer.tasks.parser;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.metricminer.changesets.ChangeSetCollection;
import org.metricminer.changesets.ChangeSetCollectionFactory;
import org.metricminer.config.project.Config;
import org.metricminer.infra.dao.ArtifactDao;
import org.metricminer.infra.dao.AuthorDao;
import org.metricminer.infra.dao.CommitDao;
import org.metricminer.infra.dao.ModificationDao;
import org.metricminer.infra.dao.SourceCodeDao;
import org.metricminer.model.PersistedCommitConverter;
import org.metricminer.model.Project;
import org.metricminer.scm.SCM;
import org.metricminer.scm.SCMFactory;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class SCMLogParserFactory {

	public SCMLogParser basedOn(Config config, Session session, Project project) {
		SCM scm = new SCMFactory().basedOn(config);
		ChangeSetCollection collection = new ChangeSetCollectionFactory(scm)
				.basedOn(config);
		
		AuthorDao authorDao = new AuthorDao(session);
		CommitDao commitDao = new CommitDao(session);
		ArtifactDao artifactDao = new ArtifactDao(session);
		ModificationDao modificationDao = new ModificationDao(session);
		SourceCodeDao sourceCodeDao = new SourceCodeDao();
		PersistedCommitConverter converter = new PersistedCommitConverter(authorDao, commitDao, artifactDao, modificationDao, sourceCodeDao, session);
		return new SCMLogParser(converter, scm,
				collection, session, project);
	}
}
