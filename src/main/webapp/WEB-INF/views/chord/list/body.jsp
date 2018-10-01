<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/music.tld" prefix="m"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<table class="chordList">
	<c:forEach items="${chords}" var="chordsOfNote">
		<tr>
			<c:forEach items="${chordsOfNote}" var="chord">
				<td>${chord.key}</td>
				<td>
					<c:url value="/chord/view" var="url_to_chord">
						<c:param name="name" value="${chord.key}" />
					</c:url>
				</td>
				<td class="delimiter"> </td>
			</c:forEach>
		</tr>
	</c:forEach>
</table>
