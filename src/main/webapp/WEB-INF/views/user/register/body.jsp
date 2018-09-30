<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<form:form modelAttribute="user">
	<table>
		<tr>
			<td><s:message code="label.user.name"/>:</td>
			<td><form:input path="username" /> <form:errors path="username" /></td>
		</tr>
		<tr>
			<td><s:message code="label.user.password"/>:</td>
			<td><form:password path="password" /> <form:errors path="password" /></td>
		</tr>
		<tr>
			<td><s:message code="label.confirm.password"/>:</td>
			<td><form:password path="passwordConfirm" /> <form:errors path="passwordConfirm" /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>
				<button type="reset"><s:message code="btn.reset"/></button>
				<button type="submit" name="submit" value="true"><s:message code="btn.submit"/></button>
			</td>
		</tr>
	</table>
</form:form>