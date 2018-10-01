<%@ taglib uri="/WEB-INF/music.tld" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<div>
	<c:url value="/song/list" var="url_to_performer">
		<c:param name="performer" value="${song.performerName}" />
	</c:url>
	<b><a href="${url_to_performer}">${song.performerName}</a> - ${song.name}</b>
</div>

<table>
	<tr class="noPrint">
		<td>
			<table style="width: 100%">
				<tr>
					<td align="left">
						<form:form method="GET">
							<button type="submit" name="transposition" value="${transposition - 1}">-</button>&nbsp;
							<button type="submit" name="transposition" value="${transposition + 1}">+</button>
						</form:form>
					</td>
					<td align="right">
						<button type="button" id="print"><s:message code="btn.print"/></button>
					</td>
				</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td valign="top">
			<div class="song view minimized">
				<m:song>${song.text}</m:song>
			</div>
		</td>
		<td valign="top" class="noPrint">
			<table class="chords">
				<c:set var="columnInRow" value="${3}" />
				<c:set var="column" value="${0}" />
				<c:set var="firstRow" value="${true}" />
				<c:forEach items="${chords}" var="chord">
					<c:if test="${column == 0}">
						<c:if test="${!firstRow}">
							</tr>
						</c:if>
						<tr>
						<c:set var="firstRow" value="${false}" />
					</c:if>
					<td class="chord">
						<b>${chord.key}</b>: <b>${m:fretsToString(chord.value)}</b><br />
						<m:chord frets="${m:fretsToString(chord.value)}" styleClass="chord_diagram" size="5" />
					</td>
					<c:set var="column" value="${(column + 1) % columnInRow}" />
				</c:forEach>
				<c:set var="additionCount" value="${(columnInRow - column) % columnInRow}" />
				<c:forEach var="i" begin="1" end="${additionCount}" step="1">
					<td>&nbsp;</td>
				</c:forEach>
				<c:if test="${!firstRow}">
					</tr>
				</c:if>
			</table>
		</td>
	</tr>
	
	<tr class="noPrint">
		<td>
			<table style="width: 100%">
				<tr>
					<td align="left">
						<c:url value="/song/vote/${song.id}" var="url_vote"/>
						<s:message code="label.rate"/>: <span id="positive_rate">+${statistics.positiveRate + 0}</span> / <span id="negative_rate">-${statistics.negativeRate + 0}</span>
						<security:authorize access="isAuthenticated()">
							<c:set var="disabled">${mayVote ? '' : 'disabled="disabled"'}</c:set>
							<button type="button" class="vote" data-rate="1" data-url="${url_vote}" ${disabled}>+</button>
							<button type="button" class="vote" data-rate="-1" data-url="${url_vote}" ${disabled}>-</button>
						</security:authorize>
					</td>
					<td align="right">
						<security:authorize access="isAuthenticated()">
							<c:url value="/song/edit/${song.id}" var="url_edit" />
							<form:form method="GET" action="${url_edit}">
								<button type="submit">Edit</button>
							</form:form>
						</security:authorize>
					</td>
				</tr>
			</table>
		</td>
		<td>&nbsp;</td>
	</tr>
</table>