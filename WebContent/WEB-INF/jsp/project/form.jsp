<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<c:import url="../import/head.jsp" />
	<title>Metric Miner</title>
</head>

<body>
	<div id="hld">
		<div class="wrapper">		<!-- wrapper begins -->
			<c:import url="../import/header.jsp" />
			<div class="block">
				<div class="block_head">
					<div class="bheadl"></div>
					<div class="bheadr"></div>
					<h2>New Project</h2>
				</div>		<!-- .block_head ends -->
				
				<div class="block_content">
					<form method="post" action='<c:url value="/projects"></c:url>' '>
						<p>
							<label for="project.name">Name: </label> <br />
							<input type="text" class="text small" name="project.name" />
						</p>
						<p>
							<label for="project.scmUrl">Git url: </label><br />
							<input type="text" class="text small" name="project.scmUrl" />
						</p>
						<p>
							<label for="project.scmRootDirectoryName">Git root directory name: </label><br />
							<input type="text" class="text small" name="project.scmRootDirectoryName" />
						</p>
						
						<h3>Metrics to calculate: </h3>
						<table>
							<tr>
								<th><input type="checkbox" class="check_all"></th>
								<th>Name</th>
							</tr>
							<c:forEach items="${metrics}" var="metric">
								<tr>
									<td>
										<input type="checkbox" id="${metric.name}" name="metrics[${s.index}].metricFactoryClass" value="${metric.metricFactoryClass}" />
									</td> 
									<td>${metric.name}</td>
								</tr>
							</c:forEach>
						</table>
						
						<p>
							<input type="submit" class="submit small" value="Save" />
						</p>
					</form>
				</div>		<!-- .block_content ends -->
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>		<!-- .block ends -->
			<div id="footer">
				<p class="left"><a href="#"></a></p>
			</div>
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/javascripts.jsp" />
</body>
</html>

