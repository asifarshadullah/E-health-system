<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
<title>IUT E-HEALTH</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="CareMed demo project">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/resources/styles/bootstrap4/bootstrap.min.css">

<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/resources/styles/main_styles.css">
<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/resources/styles/responsive.css">
<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/resources/styles/login.css">

</head>
<body>

<div class="super_container">

	<!-- Header -->

	<header class="header trans_200">

		<!-- Top Bar -->
		<div class="top_bar">
			<div class="container">
				<div class="row">
					<div class="col">
						<div class="top_bar_content d-flex flex-row align-items-center justify-content-start">
						
							<div class="emergencies  d-flex flex-row align-items-center justify-content-start ml-auto">For Emergencies: +563 47558 623</div>
						</div>

					</div>
				</div>
			</div>
		</div>

		<!-- Header Content -->
		<div class="header_container">
			<div class="container">
				<div class="row">
					<div class="col">
						<div class="header_content d-flex flex-row align-items-center justify-content-start">
							<nav class="main_nav ml-auto">
								<ul>
									<li><a href="${ pageContext.request.contextPath }/">Home</a></li>
									<%--<li><a href="about.html">About us</a></li>
									<li><a href="contact.html">Contact</a></li>--%>
									<c:set var="loggedIn" scope="session" value="${ sessionScope.loggedIn }"/>
									<c:set var="patientName" scope="session" value="${ sessionScope.patientName }"/>
                  					<c:choose>
                  						<c:when test="${ loggedIn == 'true' }">
                 							<li><a href="patientHistory?id=${ patientId }">${ patientName }</a></li>
                  							<li><a href="logout">Log out</a><li>
                  						</c:when>
                  						<c:otherwise>
                  							<li><a href="logIn">Login</a></li>
                 							<li><a href="signIn">Signup</a></li>
                  						</c:otherwise>
                  					</c:choose>
								</ul>
							</nav>
							<div class="hamburger ml-auto"><i class="fa fa-bars" aria-hidden="true"></i></div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- Logo -->
		<div class="logo_container_outer">
			<div class="container">
				<div class="row">
					<div class="col">
						<div class="logo_container">
							<a href="#">
								<div class="logo_content d-flex flex-column align-items-start justify-content-center">
									<div class="logo_line"></div>
									<div class="logo d-flex flex-row align-items-center justify-content-center">
										<div class="logo_text">IUT<span>E-HEALTH</span></div>
										<div class="logo_box">+</div>
									</div>
									<div class="logo_sub">Health Care Center</div>
								</div>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>

	</header>

</div>

<body>

<div class="loginslot">

	<c:set var="loginCheck" value="${ loginFailed }"/>
	
	<c:if test="${ loginCheck == 'true' }">
		<p class="others">Login Failed</p>
	</c:if>
	
	<form action="verifyPatient" method="post">
          <input type="text" name="email" placeholder="Username or Email" class="email"><br>
          <input type="password" name="password" placeholder="password" class="password"><br>
          <input type="submit" name="" value="Log In" class="login">
	</form>
	<br/>
	<a href="${pageContext.request.contextPath}/signIn" class="others">Sign In</a><br/>
	<a href="${ pageContext.request.contextPath }" class="others">Back To Homepage</a>
	</div>
</body>
</html>