<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="/WEB-INF/music.tld" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<c:url value="/chord/edit" var="action" />
<%-- action is set manually in order to cast away get parameters --%>
<style type="text/css">
table.notes td {
	width: 20px;
}

.bars td button{
width: 100%;
padding: 0;
}
</style>

<table class="notes">
	<tr>
		<td>C</td>
		<td>C#</td>
		<td>D</td>
		<td>D#</td>
		<td>E</td>
		<td>F</td>
		<td>F#</td>
		<td>G</td>
		<td>G#</td>
		<td>A</td>
		<td>A#</td>
		<td>B</td>
	</tr>

	<tr>
		<td class="noteC">&nbsp;</td>
		<td class="noteC_">&nbsp;</td>
		<td class="noteD">&nbsp;</td>
		<td class="noteD_">&nbsp;</td>
		<td class="noteE">&nbsp;</td>
		<td class="noteF">&nbsp;</td>
		<td class="noteF_">&nbsp;</td>
		<td class="noteG">&nbsp;</td>
		<td class="noteG_">&nbsp;</td>
		<td class="noteA">&nbsp;</td>
		<td class="noteA_">&nbsp;</td>
		<td class="noteB">&nbsp;</td>
	</tr>
</table>

<form:form method="POST" modelAttribute="chord" action="${action}">
	<table>
		<tr>
			<td><s:message code="label.name"/>:</td>
			<td>
				<form:input path="name" size="2" disabled="true"/>
				<form:select path="chordNoteIndex">
					<form:option value="" label=" " />
					<%-- <form:options items="${notes}" itemLabel="name" /> --%>
					<c:forEach var='note' items='${notes}' varStatus='loop' >
						<form:option value="${loop.index}" label="${note.name}" />
					</c:forEach>
				</form:select>
				<form:select path="chordTypeIndex">
					<form:option value="" label=" " />
					<c:forEach var='type' items='${types}' varStatus='loop' >
						<form:option value="${loop.index}" label="${empty type.name ? \"M\" : type.name}" />
					</c:forEach>
				</form:select>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><s:message code="label.frets"/>:</td>
			<td>
				<c:forEach var="i" begin="0" end="5" step="1" varStatus="status">
					<form:input path="strings[${i}]" size="2" />
					<c:if test="${not status.last}"> - </c:if>
				</c:forEach>
			</td>
			<td>
				<button type="submit" name="parse" value="true"><s:message code="btn.apply"/></button>
			</td>
		</tr>
		<tr>
			<td><s:message code="label.notes"/>:</td>
			<td>
				<tiles:insertDefinition name="chord.notes">
					<tiles:putAttribute name="notes" value="${currentNotes}" />
				</tiles:insertDefinition>
			</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<form:errors path="*" />
	<br />
	<m:chord frets="${m:fretsToString(chord.strings)}" type="edit" min="0"
		max="20" id="chord_diagram" />
</form:form>
