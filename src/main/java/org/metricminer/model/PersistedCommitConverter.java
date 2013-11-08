package org.metricminer.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.metricminer.infra.dao.ArtifactDao;
import org.metricminer.infra.dao.AuthorDao;
import org.metricminer.infra.dao.CommitDao;
import org.metricminer.infra.dao.ModificationDao;
import org.metricminer.infra.dao.SourceCodeDao;
import org.metricminer.scm.CommitData;
import org.metricminer.scm.DiffData;


public class PersistedCommitConverter {

    private HashMap<String, Author> savedAuthors;
	private AuthorDao authorDao;
	private CommitDao commitDao;
	private ArtifactDao artifactDao;
	private ModificationDao modificationDao;
	private SourceCodeDao sourceCodeDao;
	private Session session;

	public PersistedCommitConverter(AuthorDao authorDao, CommitDao commitDao, 
			ArtifactDao artifactDao, ModificationDao modificationDao, 
			SourceCodeDao sourceCodeDao, Session session) {
    	this.authorDao = authorDao;
		this.commitDao = commitDao;
		this.artifactDao = artifactDao;
		this.modificationDao = modificationDao;
		this.sourceCodeDao = sourceCodeDao;
		this.session = session;
		savedAuthors = new HashMap<String, Author>();
    }

    public Commit toDomain(CommitData data, Project project) throws ParseException {

		Author author = convertAuthor(data);
        Commit commit = convertCommit(data, author, project);

        for (DiffData diff : data.getDiffs()) {
            Artifact artifact = convertArtifact(project, diff);
            Modification modification = createModification(commit, diff, artifact);
            if (artifact.isSourceCode()) {
                SourceCode sourceCode = new SourceCode(modification, diff.getFullSourceCode());
                sourceCodeDao.save(session, sourceCode);
            }
        }

        return commit;
    }

	private Modification createModification(Commit commit, DiffData diff,
			Artifact artifact) {
		Modification modification = new Modification(diff.getDiff(), commit, artifact, diff
		        .getModificationKind());
		artifact.addModification(modification);
		commit.addModification(modification);
		modificationDao.save(modification);
		return modification;
	}

	private Artifact convertArtifact(Project project, DiffData diff) {
		Artifact artifact = searchForPreviouslySavedArtifact(diff.getName(), project);

		if (artifact == null) {
		    artifact = new Artifact(diff.getName(), diff.getArtifactKind(), project);
		    artifactDao.save(artifact);
		}
		return artifact;
	}

	private Commit convertCommit(CommitData data, Author author, Project project)
			throws ParseException {
		Commit commit = new Commit(data.getCommitId(), author, convertDate(data),
                new CommitMessage(data.getMessage()), new Diff(data.getDiff()), data.getPriorCommit(), project);
		commitDao.save(commit);
		return commit;
	}

	private Author convertAuthor(CommitData data) {
		Author author = searchForPreviouslySavedAuthor(data.getAuthor());
        if (author == null) {
            author = new Author(data.getAuthor(), data.getEmail());
            savedAuthors.put(data.getAuthor(), author);
            authorDao.save(author);
        }
		return author;
	}

    private Author searchForPreviouslySavedAuthor(String name) {
    	if (savedAuthors.containsKey(name))
    		return savedAuthors.get(name);
    	Author author = authorDao.find(name);
        savedAuthors.put(name, author);
        return author;
    }

    private Artifact searchForPreviouslySavedArtifact(String name, Project project) {
        return artifactDao.find(name, project);
    }

    private Calendar convertDate(CommitData data) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(data.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
