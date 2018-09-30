<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="<c:url value="/resources/easyTooltip.js" />"></script>

<c:url value="/chord/popup" var="url_to_chord"/>

<script type="text/javascript">
	$(document).ready(function(){
		$(".song.view .chord").easyTooltip({
			content: function(){
				var chordName = escape($.trim($(this).html()));
        		return "<iframe class=\"chord_popup\" src=\"${url_to_chord}?name=" + chordName + "\" />";
			}
	    });
	});
</script>