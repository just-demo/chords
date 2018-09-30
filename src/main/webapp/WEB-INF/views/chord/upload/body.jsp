<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<form:form method="POST" modelAttribute="chordUpload">
	<s:message code="label.chords.with.pattern" />
	<br />
	<form:textarea path="chords" rows="20" cols="50" />
	<br />
	<br />
	<s:message code="label.transposition" />:<br />
	<form:textarea path="transpositions" rows="4" cols="50" />
	<br />
	<button type="submit" name="save" value="true">
		<s:message code="btn.submit" />
	</button>
</form:form>
