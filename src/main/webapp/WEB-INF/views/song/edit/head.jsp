<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<script type="text/javascript" src="<c:url value="/resources/autoresize.jquery.js" />"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#textInitial").autoResize({limit:10000}).change();
		$(".song.process .chord").live("click", function(){ //TODO: seems that live is not necessary here
			var index = $(this).attr("index");
			if ($(this).hasClass("rollBack")){
				$(this).removeClass("rollBack");
				$("input[name=\"rollBack\"][value=\"" + index + "\"]", "#song").remove();
			}
			else{
				$(this).addClass("rollBack");
				$("#song").append("<input type=\"hidden\" name=\"rollBack\" value=\"" + index + "\"/>");
			}
			//$(this).mouseleave().mouseenter(); //in order to change tip
			$("#song").submit();
		});
		
		$("input[name=\"rollBack\"]", "#song").each(function(i){
			var index = $(this).val();
			$(".chord[index=\"" + index + "\"]", "#song").addClass("rollBack");
		});

		$("#new_chord").click(function(){
			var chordName = getSelectedText();
			window.open($(this).data("url") + "?name=" + escape(chordName), "new_chord");
		});
		
		$("#print").click(function(){
			$("#song_title").html($("#performerName").val() + " - " + $("#name").val());
			window.print();
		});
		
		$("#performerNameSelected").change(function(){
			$("#performerName").val($(this).val());
		});
		
		$("#tagSelection").change(function(){
			var tag = $(this).val();
			var tagsBuffer = {};
			var tags = $("#tags").val().split(",");
			for (var i in tags){
				tagsBuffer[$.trim(tags[i])] = true;
			}
			tagsBuffer[tag] = true;
			tags = [];
			for (var tag in tagsBuffer){
				if ("" !== tag){
					tags.push(tag);
				}
			}
			$("#tags").val(tags.join(", "));
			$(this).val("")
		});
		
		function getSelectedText() {
			var func = window.getSelection || document.getSelection || document.selection;
			return func ? func() : "";
		}
	});
	
</script>
<tiles:insertDefinition name="head.chord.popup" />
