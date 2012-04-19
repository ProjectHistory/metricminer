package br.com.caelum.revolution.persistence.runner;

import java.text.ParseException;

import model.Project;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import br.com.caelum.revolution.changesets.ChangeSet;
import br.com.caelum.revolution.changesets.ChangeSetCollection;
import br.com.caelum.revolution.domain.PersistedCommitConverter;
import br.com.caelum.revolution.scm.CommitData;
import br.com.caelum.revolution.scm.SCM;

public class SCMLogParser implements PersistenceRunner {

    private Session session;
    private PersistedCommitConverter converter;
    private SCM scm;
    private final ChangeSetCollection collection;
    private static Logger log = Logger.getLogger(SCMLogParser.class);
    private final Project project;

    public SCMLogParser(PersistedCommitConverter converter, SCM scm,
            ChangeSetCollection collection, Session session, Project project) {
        this.converter = converter;
        this.scm = scm;
        this.collection = collection;
        this.session = session;
        this.project = project;
    }

    public void start() {
        for (ChangeSet changeSet : collection.get()) {
            log.info("--------------------------");
            CommitData commitData;
            session.clear();
            Transaction transaction = session.getTransaction();
            if (!transaction.isActive()) {
                session.beginTransaction();
            }
            try {
                commitData = scm.detail(changeSet.getId());
                log.info("Persisting change set " + changeSet.getId());
                log.info("Author: " + commitData.getAuthor() + " on " + commitData.getDate());
                converter.toDomain(commitData, session, project);
                if (Runtime.getRuntime().freeMemory() < 10000l) {
                    throw new OutOfMemoryError("Low memory, unable to commit transaction.");
                }
                session.getTransaction().commit();
            } catch (ParseException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                session.getTransaction().rollback();
                log.error("Too big changeset, out of memory", e);
                commitData = null;
                System.gc();
            } catch (GenericJDBCException e) {
                session.getTransaction().rollback();
                log.error("Too big changeset, unable to persist", e);
                commitData = null;
                System.gc();
            }

        }
        log.info("");
        log.info("--------------------------");
        log.info("Finished persisting commit data.");
    }
}
