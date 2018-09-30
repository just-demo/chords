<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>

<s:message code="text.song.delete.question" var="confirmation_question"/>
<script type="text/javascript">
	$(document).ready(function() {
		$(".delete").click(function() {
			return confirm("${confirmation_question}");
		});
		
		$("#performerNameSelected").change(function(){
			$("#performerName").val($(this).val());
		});
	});
</script>
<tiles:insertDefinition name="head.chord.popup" />
