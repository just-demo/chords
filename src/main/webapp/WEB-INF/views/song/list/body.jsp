<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/song/list" var="action" />
<form:form method="POST" action="${action}" modelAttribute="searchForm">
	<form:input path="text" size="30"/>
	<button name="search" value="true" type="submit" ><s:message code="btn.search"/></button>
</form:form>

<div>
	<s:message code="label.tags" />
	:
	<c:forEach items="${tags}" var="tag" varStatus="status">
		<c:url value="/song/list" var="url_to_tag">
			<c:param name="tag" value="${tag.name}" />
		</c:url>
		<a href="${url_to_tag}">${tag.name}</a>
		<c:if test="${not status.last }">, </c:if>
	</c:forEach>
</div>
<br/>

<table class="songs">
	<tr>
		<th><s:message code="label.performer" /></th>
		<th><s:message code="label.song" /></th>
		<th><s:message code="label.tags" /></th>
		<security:authorize access="isAuthenticated()">
		<th>&nbsp;</th>
		</security:authorize>
	</tr>
	<c:forEach items="${songs}" var="song">
		<c:url value="/song/view/${song.id}" var="url_to_song" />
		<c:url value="/song/delete/${song.id}" var="url_delete" />
		<c:url value="/song/list" var="url_to_performer">
			<c:param name="performer" value="${song.performer.name}" />
		</c:url>
		<tr>
			<td><a href="${url_to_performer}">${song.performer.name}</a></td>
			<td><a href="${url_to_song}">${song.name}</a></td>
			<td>
				<c:forEach items="${song.tags}" var="tag" varStatus="status">
					<c:url value="/song/list" var="url_to_tag">
						<c:param name="tag" value="${tag.name}" />
					</c:url>
					<a href="${url_to_tag}">${tag.name}</a>
					<c:if test="${not status.last }">, </c:if>
				</c:forEach>
			</td>
			<security:authorize access="isAuthenticated()">
			<td>
				<a class="delete" href="${url_delete}"><s:message code="btn.delete" /></a>
			</td>
			</security:authorize>
		</tr>
	</c:forEach>
</table>

<c:if test="${not empty pageScroller}">
	<br/>
	<div class="pageScroller">
		<c:set var="page" value="${pageScroller.first}" />
		<c:choose>
			<c:when test="${page ne pageScroller.current}">
				<c:url value="/song/list" var="url_to_page">
					<c:param name="page" value="${page}" />
				</c:url>
				<a href="${url_to_page}">${page}</a>
			</c:when>
			<c:otherwise>
				<span>${page}</span>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${(pageScroller.min - pageScroller.first) gt 1}">
			<span>...</span>
		</c:if>
		
		<c:forEach begin="${pageScroller.min}" end="${pageScroller.max}" var="page">
			<c:choose>
				<c:when test="${page ne pageScroller.current}">
					<c:url value="/song/list" var="url_to_page">
						<c:param name="page" value="${page}" />
					</c:url>
					<a href="${url_to_page}">${page}</a>
				</c:when>
				<c:otherwise>
					<span>${page}</span>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
		<c:if test="${(pageScroller.last - pageScroller.max) gt 1}">
			<span>...</span>
		</c:if>
		
		<c:set var="page" value="${pageScroller.last}" />
		<c:choose>
			<c:when test="${page ne pageScroller.current}">
				<c:url value="/song/list" var="url_to_page">
					<c:param name="page" value="${page}" />
				</c:url>
				<a href="${url_to_page}">${page}</a>
			</c:when>
			<c:otherwise>
				<span>${page}</span>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>
