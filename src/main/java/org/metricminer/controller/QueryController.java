package org.metricminer.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.metricminer.infra.dao.ColumnMetadata;
import org.metricminer.infra.dao.MetricDao;
import org.metricminer.infra.dao.QueryDao;
import org.metricminer.infra.dao.QueryExampleDao;
import org.metricminer.infra.dao.QueryResultDAO;
import org.metricminer.infra.dao.TaskDao;
import org.metricminer.infra.interceptor.LoggedUserAccess;
import org.metricminer.infra.session.UserSession;
import org.metricminer.infra.validator.QueryValidator;
import org.metricminer.model.Artifact;
import org.metricminer.model.Author;
import org.metricminer.model.Commit;
import org.metricminer.model.CommitMessage;
import org.metricminer.model.Modification;
import org.metricminer.model.Query;
import org.metricminer.model.QueryExample;
import org.metricminer.model.QueryResult;
import org.metricminer.model.Task;
import org.metricminer.tasks.metric.cc.CCResult;
import org.metricminer.tasks.metric.fanout.FanOutResult;
import org.metricminer.tasks.metric.invocation.MethodsInvocationResult;
import org.metricminer.tasks.metric.lines.LinesOfCodeResult;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.caelum.vraptor.view.Results;

import com.google.gson.Gson;

@Resource
public class QueryController {

    private final TaskDao taskDao;
    private final Result result;
    private final QueryDao queryDao;
    private final QueryResultDAO queryResultDAO;
    private final QueryValidator queryValidator;
    private final UserSession userSession;
	private Validator validator;
	private QueryExampleDao queryExampleDao;
	private MetricDao metrics;

    public QueryController(TaskDao taskDao, QueryDao queryDao,
            QueryResultDAO queryResultDAO, Result result,
            QueryValidator queryValidator, UserSession userSession, 
            Validator validator, QueryExampleDao queryExampleDao,
            MetricDao metrics) {
        this.taskDao = taskDao;
        this.queryDao = queryDao;
        this.queryResultDAO = queryResultDAO;
        this.result = result;
        this.queryValidator = queryValidator;
        this.userSession = userSession;
		this.validator = validator;
		this.queryExampleDao = queryExampleDao;
		this.metrics = metrics;
    }

    @LoggedUserAccess
    @Get("/queries/new")
    public void queryForm() {
    	List<QueryExample> examples = queryExampleDao.list();
    	result.include("examples", examples);
    }

    @LoggedUserAccess
    @Post("/queries")
    public void save(Query query) {
        queryValidator.validate(query);
        validator.onErrorRedirectTo(QueryController.class).queryForm();
        
        query.setAuthor(userSession.getUser());
        queryDao.save(query);
        taskDao.saveTaskToExecuteQuery(query);
        result.include("included", true);
        result.redirectTo(QueryController.class).detailQuery(query.getId());
    }


    @Get("/queries/{page}")
    @LoggedUserAccess
    public void listQueries(int page) {
        List<Query> otherQueries = queryDao.doesntBelongTo(userSession.getUser(), 1);
        List<Query> myQueries = queryDao.belongsTo(userSession.getUser());
        result.include("others", otherQueries);
        result.include("mine", myQueries);
        
        result.include("totalPages", queryDao.pagesForDoesntBelongTo(userSession.getUser()));
        result.include("currentPage", page);
    }


    @Get("/query/{queryId}")
    public void detailQuery(Long queryId)  {
        Query query = queryDao.findBy(queryId);
        List<Task> tasksToRunThisQuery = taskDao.findTasksScheduledToRunQuery(query);
        
        result.include("query", query);
        result.include("allowedToEdit", query.isAllowedToEdit(userSession.getUser()));
        result.include("scheduledToRun", !tasksToRunThisQuery.isEmpty());
    }

	@Get("/query/download/{resultId}/zip")
	public Download downloadZip(Long resultId) {
		QueryResult result = queryResultDAO.findById(resultId);
		String csvFilename = result.getFilenameAsZip();
		return new FileDownload(new File(csvFilename), "application/zip", "result.zip");
	}
	
	@Get("/query/download/{resultId}/csv")
	public Download downloadCsv(Long resultId) {
		QueryResult result = queryResultDAO.findById(resultId);
		String csvFilename = result.getFilenameAsCsv();
		return new FileDownload(new File(csvFilename), "text/csv", "result.csv");
	}

    @LoggedUserAccess
    @Post("/query/run")
    public void runQuery(Long queryId) {
        Query query = queryDao.findBy(queryId);
        taskDao.saveTaskToExecuteQuery(query);
        result.include("scheduledToRun", true);
        result.redirectTo(this).detailQuery(queryId);
    }
    
    @LoggedUserAccess
    @Get("/query/wizard")
    public void wizard() {
    	Map<String, List<ColumnMetadata>> map = new HashMap<String, List<ColumnMetadata>>();
		map.put("Artifact", metrics.getColumns(Artifact.class));
		map.put("Commit", metrics.getColumns(Commit.class));
		map.put("Author", metrics.getColumns(Author.class));
		map.put("CommitMessage", metrics.getColumns(CommitMessage.class));
		map.put("CCResult", metrics.getColumns(CCResult.class));
		map.put("FanOutResult", metrics.getColumns(FanOutResult.class));
		map.put("LinesOfCodeResult", metrics.getColumns(LinesOfCodeResult.class));
		map.put("MethodsInvocationResult", metrics.getColumns(MethodsInvocationResult.class));
		map.put("Modification", metrics.getColumns(Modification.class));
		
		result.include("entitiesMetadata", new Gson().toJson(map));
    }
    
    @Get("/query/edit/{queryId}")
    public void editQueryForm(Long queryId) {
        Query query = queryDao.findBy(queryId);
        if (!query.isAllowedToEdit(userSession.getUser())) {
            result.use(Results.http()).setStatusCode(403);
        }
        result.include("query", query);
    }
    
    @LoggedUserAccess
    @Post("/query/{queryId}")
    public void updateQuery(Query updatedQuery, Long queryId) {
        Query persistedUpdatedQuery = queryDao.findBy(queryId);
        persistedUpdatedQuery.setSqlQuery(updatedQuery.getSqlQuery());
        queryValidator.validate(persistedUpdatedQuery);
        queryValidator.validateEditByAuthor(persistedUpdatedQuery, userSession.getUser());
        
        queryDao.update(persistedUpdatedQuery);
        
        taskDao.saveTaskToExecuteQuery(persistedUpdatedQuery);
        result.redirectTo(QueryController.class).detailQuery(queryId);
    }
    
    
}