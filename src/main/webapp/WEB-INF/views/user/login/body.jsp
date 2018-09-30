<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<c:if test="${not empty param.login_error}">
	<div class="error">
		<%-- ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} --%>
		<s:message code="error.login.failed"/>
	</div>
</c:if>
<form action="<c:url value="/j_spring_security_check"/>" method='POST'>
    <c:if test="${not empty param.redirect}">
        <input type="hidden" name="spring-security-redirect" value="${param.redirect}" />
    </c:if>
	<table>
		<tr>
			<td><s:message code="label.user"/>:</td>
			<td><input type="text" name="j_username"
				value="${SPRING_SECURITY_LAST_USERNAME}"></td>
		</tr>
		<tr>
			<td><s:message code="label.password"/>:</td>
			<td><input type='password' name='j_password' /></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input name="reset" type="reset" /> <button name="submit" type="submit" ><s:message code="btn.login"/></button></td>
		</tr>
	</table>
</form>
