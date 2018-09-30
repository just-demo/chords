<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<tiles:importAttribute name="notes" />
<c:forEach items="${notes}" var="note" varStatus="status">
	${note.name}<c:if test="${not status.last}">, </c:if>
</c:forEach>