<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="metricminer" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<link rel="stylesheet" href="<c:url value='/js/codemirror/lib/codemirror.css' />" />
	<c:import url="../import/head.jsp" />
	<title>Metric Miner</title>
	<style>
	.hidden {
		display: none;
	}
	.example-details {
		margin-top: 15px;
	}
	#wizard .block_content {
		height: 300px;
	}
	</style>
</head>

<body>
	<div id="hld">
		<div class="wrapper">		<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			
			<metricminer:box title="Wizard" boxId="wizard">
				<div class="wizard-step" id="step1">
					What kind of data you want to extract?
					<ul>
						<li>
							<a href="#" id="commit-query" data-nextstep="step2-commit">
								Commit data
							</a>
						</li>
						<li>
							<a href="#" id="metric-query" data-nextstep="step2-metric">
								Code metrics data
							</a>
						</li>
					</ul>
				</div>
				<div class="hidden" class="wizard-step" id="step2-commit">
					What information you want to relate to commit data?
					<ul>
						<li>
							<label>
								<input type="checkbox" class="step2 commit-query entity-selection" data-columns="cm.message" 
									data-entity="CommitMessage" data-alias="cm" data-joincolumn="message_id"
									data-commitjoincolumn="id"/>
									Commit message
							</label>
						</li>
						<li>
							<label>
								<input type="checkbox" class="step2 commit-query entity-selection" data-columns="author.secret_name, author.secret_email" 
									data-entity="Author" data-alias="author" data-joincolumn="author_id"
									data-commitjoincolumn="id" />
								Author	
							</label>
						</li>
						<li>
							<label>
								<input type="checkbox" class="step2 commit-query entity-selection" data-columns="modification.kind as modification_kind, artifact.name, artifact.kind as artifact_kind" 
									data-entity="Modification" data-alias="modification" data-joincolumn="id"
									data-commitjoincolumn="commit_id" data-extrajoin="JOIN Artifact artifact ON modification.artifact_id=artifact.id"/>
								Modified artifacts
							</label>
						</li>
					</ul>
					<form>
						<input type="submit" class="submit long next-step" value="Next step" />
					</form>
				</div>
				<div class="hidden" class="wizard-step" id="step2-metric" data-nextstep="step3-metric">
					What metric you want to extract?
					<ul>
						<li>
							<a href="#" class="step2 metric-query entity-selection" data-entity="CCResult" data-alias="cc"
								data-columns="cc.avgCc, cc.cc">
								Cyclomatic complexity
							</a>
						</li>
						<li>
							<a href="#" class="step2 metric-query entity-selection" data-entity="FanOutResult" data-alias="fanout"
								data-columns="fanout.fanOut">
								Fan out
							</a>
						</li>
						<li>
							<a href="#" class="step2 metric-query entity-selection" data-entity="LinesOfCodeResult" data-alias="loc"
								data-columns="loc.linesOfCode, loc.methodName">
								Lines of code
							</a>
						</li>
						<li>
							<a href="#" class="step2 metric-query entity-selection" data-entity="MethodsInvocationResult" data-alias="methodsInvocation"
								data-columns="methodsInvocation.methodName, methodsInvocation.methodsInvocation">
								Methods invocation
							</a>
						</li>
					</ul>
				</div>
				<div class="hidden" class="wizard-step" id="step3-metric">
					What information you want to relate to metric data?
					<ul>
						<li>
							<label>
								<input type="checkbox" class="step3 metric-query entity-selection" data-entity="Commit"
									data-columns="commit.commitId, commit.date" data-metricjoincolumn="modification.commit_id"
									data-joincolumn="commit.id" data-alias="commit"/>
									Commit associated
							</label>
						</li>
						<li>
							<label>
								<input type="checkbox" class="step3 metric-query entity-selection" data-entity="Project"
									data-columns="project.name" data-metricjoincolumn="artifact.project_id"
									data-joincolumn="project.id" data-alias="project"/>
									Project associated
							</label>
						</li>
					</ul>
					<form>
						<input type="submit" class="submit long next-step" value="Next step" />
					</form>
				</div>
				<div class="hidden where-clauses">
					Do you want to filter the results with any of these entities?
					<ul>
					</ul>
					<form>
						<input type="submit" id="finish" class="submit long" value="i'm done, finish!" />
					</form>
				</div>
				
				<div class="hidden fields-selection">
					<p>What fields?</p>
					<ul>
					</ul>
					<form>
						<input type="submit" id="fields-selection-submit" class="submit long next-step" value="select other entity" />
					</form>
				</div>
				<div class="hidden condition">
					<p class="desc">Define the restriction:</p>
					<form id="condition-form">
						<input type="text" id="restriction" />
						<p>For example: id &lt; 100</p>
						<input type="submit" class="submit long" value="done" />
					</form>
				</div>
				
			</metricminer:box>
			
			<metricminer:box boxId="generated-query" title="Generated SQL Query">
				<c:if test="${!empty errors}">
					<div class="message errormsg">
						<p>
							<c:forEach var="error" items="${errors}">
						    	${error.category} - ${error.message}<br />
							</c:forEach>
						</p>
					</div>
				</c:if>
				
				<form method="post" action="${linkTo[QueryController].save}">
					<p>
						<label for="query-name">Query name: </label> <br />
						<input id="query-name" type="text" value="Give a name to your query!" class="text small" name="query.name" />
					</p>
					<p>
						<label for="sqlQuery">SQL Query: </label> <br />
						<div id="sql-query">
							<span id="placeholder">loading editor...</span>
							<textarea name="query.sqlQuery" id="sqlQuery"></textarea>
						</div>
					</p>
					<p class="note">
						You can see the database schema <a target="_blank" href="<c:url value='/images/erd.png' />">here</a>.
					</p>
					<p class="note">
						Please, do not get the source code. We are trying to convince industry to put 
						their projects here. So, never try to get their code. Be honest!
					</p>
					<p>
						<input type="submit" class="submit long" value="Save and execute" />
					</p>
				</form>
			</metricminer:box>
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
	<script src="<c:url value='/js/codemirror/lib/codemirror.js' />"></script>
	<script src="<c:url value='/js/codemirror/mode/sql/sql.js' />"></script>
	<script>
		var metadata = ${entitiesMetadata};
	</script>
	<script src="<c:url value='/js/wizard.js' />"></script>
</body>
</html>

