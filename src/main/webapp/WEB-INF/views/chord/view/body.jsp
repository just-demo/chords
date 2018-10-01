<%@ taglib uri="/WEB-INF/music.tld" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<b>${chord.name}</b> (<tiles:insertDefinition name="chord.notes">
	<tiles:putAttribute name="notes" value="${chord.notes}" />
</tiles:insertDefinition>)
<br/>
<c:forEach items="${chordVariants}" var="chordFrets">
	<b>${m:fretsToString(chordFrets)}</b>
	<c:choose>
		<%-- just ${chordFrets eq defaultChord} did not work --%>
		<c:when test="${m:fretsToString(chordFrets) eq m:fretsToString(defaultChord)}"><span style="color:#00A000; font-weight: bold;">(<s:message code="label.default"/>)</span></c:when>
		<c:otherwise>
			<security:authorize access="isAuthenticated()">
				<c:url value="/chord/default" var="url_chord_default"/>
				<form action="${url_chord_default}" method="POST" style="display:inline;">
					<input type="hidden" name="name" value="${chord.name}">
					<input type="hidden" name="frets" value="${m:fretsToString(chordFrets)}">
					<button type="submit"><s:message code="btn.make.default"/></button>
				</form>
			</security:authorize>
		</c:otherwise>
	</c:choose>
	<br/>
	<m:chord frets="${m:fretsToString(chordFrets)}" size="5" id="chord_diagram"/>
	<br/>
</c:forEach>
