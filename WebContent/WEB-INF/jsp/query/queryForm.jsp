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
					<h2>Execute SQL query</h2>
				</div>		<!-- .block_head ends -->
				
				
				<div class="block_content">
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
							<label for="query.name">Query name: </label> <br />
							<input type="text" value="${query.name}" class="text small" name="query.name" />
						</p>
						<p>
							<label for="query.sqlQuery">SQL Query: </label> <br />
							<textarea type="text" class="text small" name="query.sqlQuery">${query.sqlQuery}</textarea>
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
				</div>		<!-- .block_content ends -->
				<div class="bendl"></div>
				<div class="bendr"></div>
			</div>		<!-- .block ends -->
		</div>						<!-- wrapper ends -->
	</div>		<!-- #hld ends -->
	<c:import url="../import/footer.jsp" />
	<c:import url="../import/javascripts.jsp" />
</body>
</html>

