<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<head>
	<c:import url="../import/head.jsp" />
	<style type="text/css">
	.block_content a {
		float: right;
	} 
	</style>
	
	<title>Metric Miner</title>
	</head>
	<body>
		
		<div id="hld">
			<div class="wrapper">		<!-- wrapper begins -->
				<div class="block small center login">
					<div class="block_head">
						<div class="bheadl"></div>
						<div class="bheadr"></div>
						<h2>MetricMiner - Login</h2>
					</div>		<!-- .block_head ends -->
					<div class="block_content">
						<c:if test="${message != null && !message.isEmpty()}">
							<div class="message info"><p>${message}</p></div>
						</c:if>

						<c:forEach var="error" items="${errors}">
							<div class="message errormsg"><p>${error.message}</p></div>
						</c:forEach>
						
						
						<form action='<c:url value="/login" />' method="post">
							<p>
								<label>Email:</label> <br />
								<input type="text" class="text" name="email" />
							</p>
							<p>
								<label>Password:</label> <br />
								<input type="password" class="text" name="password" />
							</p>
							<p>
								<input type="submit" class="submit" value="Login" />
								<a href="<c:url value='/signup' />">Create an account</a> 
							</p>
							<input type="hidden" name="redirectUrl" value="${redirectUrl}" />
						</form>
					</div>		<!-- .block_content ends -->
					<div class="bendl"></div>
					<div class="bendr"></div>
				</div>		<!-- .login ends -->
			</div>						<!-- wrapper ends -->
			
		</div>		<!-- #hld ends -->
		<!-- #hld ends -->
		<c:import url="../import/footer.jsp" />
		<c:import url="../import/javascripts.jsp" />
		<script src='<c:url value="/js/project/detail.js"/>'></script>
	</body>

</html>