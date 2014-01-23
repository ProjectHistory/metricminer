<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<link rel="stylesheet" href="<c:url value='/js/codemirror/lib/codemirror.css' />" />
	<c:import url="../import/head.jsp" />
	<title>Metric Miner</title>
	<style type="text/css">
		form {
			margin: 20px 0;
			float: left;
		}
		pre {
			margin: 20px 0;
			width: 500px;
			white-space: normal;
		}
		.clear {
			clear: both;
		}
		.stacktrace {
			display: none;
		}
	</style>
</head>

<body>
	<div id="hld">
		<div class="wrapper">		<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			<div class="block">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>Query</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
				
					<c:if test="${included}">
					<div class="message success"><p>Your query was added and will be executed as soon as possible!</p></div>
					</c:if>

					<c:if test="${scheduledToRun}">
						<div class="message success"><p>Query scheduled to run, you will receive a email when the results are ready.</p></div>
					</c:if>

					<h2>${query.name}</h2>
					
					<div id="sql-query">
						<textarea name="query.sqlQuery" id="sqlQuery">${query.sqlQuery }</textarea>
					</div>
					
					<c:if test="${scheduledToRun eq false}">
					
						<form method="post" action="<c:url value="/query/run" />">
							<input type="submit" class="submit small" value="Run again" />
							<input type="hidden" name="queryId" value="${query.id}" />
						</form>

						<c:if test="${allowedToEdit}">
							<form method="get"
								action="<c:url value="/query/edit/${query.id}" />">
								<input type="submit" class="submit small" value="Edit" />
							</form>
						</c:if>

					</c:if>

					
					
					<h3 class="clear">Results:</h3>
					<table class="clear results">
						<tr>
							<th></th>
							<th>Status</th>
							<th>Result</th>
							<th>Date</th>
						</tr>
						<c:if test="${scheduledToRun}">
							<tr>
								<td>#${query.results.size() + 1}</td>
								<td colspan="3">SCHEDULED</td>
							</tr>
						</c:if>
						<c:forEach items="${query.results}" var="result" varStatus="status" >
							<tr>
								<c:if test="${scheduledToRun}">
									<td>#${query.results.size() - status.count + 1}</td>
								</c:if>
								<c:if test="${scheduledToRun eq false}">
									<td>#${query.results.size() - status.count + 1}</td>
								</c:if>
								<td>${result.status}</td>
									<c:if test="${result.hasFailed() eq false}">
										<td>
											<a href="<c:url value="/query/download/${result.id}/zip" />">
												ZIP
											</a> |
											<a href="<c:url value="/query/download/${result.id}/csv" />">
												CSV
											</a>
										</td>
									</c:if>
									<c:if test="${result.hasFailed()}">
										<td>
											<a href="#" data-result-id="${result.id}" id="${result.id}-error" class="error">
												See the error stacktrace
											</a>
										</td>
										<td style="width:800px" id="${result.id}-stacktrace" class="stacktrace">
											<pre style="width:800px;word-break: break-word;">
												${result.status.message}
											</pre>
										</td>
									</c:if>
								<td><fmt:formatDate value="${result.executedDate.time}" pattern="yyyy/MM/dd - HH:mm:ss"/></td>
							</tr>
						</c:forEach>
					</table>
				</div>		<!-- .block_content ends -->
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>		<!-- .block ends -->
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
	<script type="text/javascript">
		$(document).ready(function() {
			$(".error").click(function() {
				var id = $(this).attr("data-result-id");
				$("#" + id + "-stacktrace").show();
				$(this).parent().remove();
				return false;
			});
		});
	</script>
	<script src="<c:url value='/js/codemirror/lib/codemirror.js' />"></script>
	<script src="<c:url value='/js/codemirror/mode/sql/sql.js' />"></script>
	<script>
	  var editor = CodeMirror.fromTextArea(document.getElementById("sqlQuery"), {
	    mode: "text/x-mariadb",
	    lineNumbers: true,
	    readOnly: true
	  });
	</script>
</body>
</html>

