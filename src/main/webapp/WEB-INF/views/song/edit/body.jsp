<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="/WEB-INF/music.tld" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<c:url value="/song/edit" var="action" />
<form:form method="POST" modelAttribute="song" action="${action}">
	<div class="print" id="song_title"></div>
	<div class="noPrint">
		<form:hidden path="textProcess" />
		<form:hidden path="id" />
		<c:forEach items="${song.rollBack}" var="index">
			<input type="hidden" name="rollBack" value="${index}" />
		</c:forEach>
		<table>
			<tr>
				<td><s:message code="label.performer"/>:</td>
				<td><form:input path="performerName" size="30" /></td>
				<td>
					<c:if test="${not empty performers}">
						<form:select path="performerNameSelected">
							<form:option value="" label="" />
							<form:options items="${performers}" itemLabel="name" itemValue="name" />
						</form:select>
					</c:if>
				</td>
				<td><form:errors path="performerName" /></td>
			</tr>
			<tr>
				<td><s:message code="label.song"/>:</td>
				<td><form:input path="name" size="30" /></td>
				<td>&nbsp;</td>
				<td><form:errors path="name" /></td>
			</tr>
			<tr>
				<td><s:message code="label.tags"/>:</td>
				<td><form:input path="tags" size="30" /></td>
				<td>
					<c:if test="${not empty tags}">
						<form:select path="" id="tagSelection">
							<form:option value="" label="" />
							<form:options items="${tags}" itemLabel="name" itemValue="name"/>
						</form:select>
					</c:if>
				</td>
				<td><form:errors path="tags" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><form:errors path="text" /></td>
			</tr>
		</table>
	</div>
	<table>
		<tr>
			<td class="noPrint"><form:textarea path="textInitial" rows="20" cols="50"/></td>
			<c:if test="${not empty song.textProcess}">
			<td class="noPrint song process" valign="top"><m:song>${song.textProcess}</m:song></td>
			<td class="song view" valign="top"><m:song>${song.text}</m:song></td>
			</c:if>
		</tr>
		<tr class="noPrint">
			<td><button type="submit" name="parse" value="true"><s:message code="btn.parse"/></button></td>
			<c:if test="${not empty song.textProcess}">
			<td>
				<button type="submit" name="transpose" value="-1">-</button>&nbsp;
				<button type="submit" name="transpose" value="1">+</button>&nbsp;
				<!-- 
				<button type="button" id="new_chord" data-url="<c:url value="/chord/edit" />"><s:message code="btn.new.chord"/></button>
				 -->
			</td>
			<td class="noPrint">
				<button type="button" id="print"><s:message code="btn.print"/></button>
				<security:authorize access="isAuthenticated()">
					<button type="submit" name="save" value="true"><s:message code="btn.save"/></button>&nbsp;
				</security:authorize>
			</td>
		</c:if>
		</tr>
	</table>
</form:form>
