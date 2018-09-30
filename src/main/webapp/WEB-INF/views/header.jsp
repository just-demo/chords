<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="noPrint">
	<table class="header">
		<tr>
			<td><a href="<c:url value="/" />"><s:message code="nav.home"/></a></td>
			<td><a href="<c:url value="/song/list" />"><s:message code="nav.songs"/></a></td>
			<td><a href="<c:url value="/chord/list" />"><s:message code="nav.chords"/></a></td>
			<td><a href="<c:url value="/song/edit" />"><s:message code="nav.song.editor"/></a></td>
			<td><a href="<c:url value="/chord/edit" />"><s:message code="nav.chord.editor"/></a></td>
			<td><a href="<c:url value="/storage" />"><s:message code="nav.storage"/></a></td>
			<td class="language">
				<c:forEach var="locale" items="${locales}" varStatus="status">
					<c:set var="language"><s:message code="${locale.value}"/></c:set>
					<c:choose>
						<c:when test="${pageContext.response.locale eq locale.key}">
							${language}
						</c:when>
						<c:otherwise>
							<c:set var="language_key" value="l"/>
							<c:url value="" var="language_url">
								<%-- here taken into account that existing GET parameters should be remained and "l" be added  --%>
								<c:forEach items="${param}" var="parameter">
									<c:if test="${parameter.key ne language_key}">
										<c:param name="${parameter.key}">${parameter.value}</c:param>
									</c:if>
								</c:forEach>						
								<c:param name="${language_key}">${locale.key}</c:param>
							</c:url>
							<a href="${language_url}">${language}</a>
						</c:otherwise>
					</c:choose>
					<c:if test="${not status.last}">|</c:if>
				</c:forEach>
			</td>
			
			<td class="login">
				<security:authorize access="isAnonymous()">
					<%-- <a href="<c:url value="/spring_security_login"/>">Login</a> --%>
					<%--
			 		<c:url value="" var="url_redirect">
						<c:forEach items="${param}" var="parameter">
							<c:param name="${parameter.key}" value="${parameter.value}"/>
						</c:forEach>
					</c:url>
					 --%>
					<%-- request.getAttribute("javax.servlet.forward.servlet_path").toString() --%>
					<%-- url_redirect should not contain /<application_context> prefix + looping throw ${param} would catch POST parameters--%>
					<c:set var="url_redirect" value="${requestScope['javax.servlet.forward.servlet_path']}?${pageContext.request.queryString}"/>
			 		<c:if test="${not empty loginRedirect}"> <%-- it can be set in controller as a model attribute --%>
			 			<c:set var="url_redirect" value="${loginRedirect}"/>
			 		</c:if>
			 		<c:url value="/user/login" var="url_login">
						<c:param name="redirect" value="${url_redirect}"/>
					</c:url>
					<a href="${url_login}"><s:message code="nav.login"/></a>
					<a href="<c:url value="/user/register"/>"><s:message code="nav.register"/></a>
				</security:authorize>
				
				<security:authorize access="isAuthenticated()">
					<c:set var="username"><b><security:authentication property="principal.username"/></b></c:set>
					<s:message code="text.user.invitation" arguments="${username}"/>
					<a class="right" href="<c:url value="/j_spring_security_logout"/>"><s:message code="nav.logout"/></a>
				</security:authorize>
			</td>
		</tr>
	</table>

</div>