<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<c:url value="/storage/song" var="url_storage_song" />
<c:url value="/storage/chord" var="url_storage_chord" />

<form:form method="POST" action="${url_storage_song}">
	<s:message code="label.export.songs"/>: <button type="submit" name="export" value="true"><s:message code="btn.export"/></button>
</form:form>
<form:form method="POST" action="${url_storage_chord}">
	<s:message code="label.export.chords"/>: <button type="submit" name="export" value="true"><s:message code="btn.export"/></button>
</form:form>

<form:form method="POST" action="${url_storage_song}" enctype="multipart/form-data">
	<s:message code="label.import.songs"/>: <input type="file" name="file"/> <button type="submit" name="import" value="true"><s:message code="btn.import"/></button>
</form:form>