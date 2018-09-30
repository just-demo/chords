<script type="text/javascript">
$(document).ready(function(){
	$("td.target", "#chord_diagram").click(function(){
		var string = $(this).attr("string");
		var fret = $(this).attr("fret");
		//var input = $("#string" + string);
		var input =$("input[name=\"strings[" + (string - 1) + "]\"]", "#chord");
		input.val(input.val() == fret ? "0" : fret);
		$("#chord").submit();
	});

	$("tr", "#chord_diagram").each(function(){
		var note = $(this).attr("note");
		if (note){
			var clazz = "note" + note.replace("#", "_");
			$("td:first-child", this).addClass(clazz); //.attr("title", note);
		}
	});
	
	$("#chordNoteIndex, #chordTypeIndex").change(function(){
		if ($("#chordNoteIndex").val() != "" && $("#chordTypeIndex").val() != ""){
			$("#chord").append("<input type=\"hidden\" name=\"selectChord\" value=\"true\"/>");
			$("#chord").submit();
		}
	});
	
	$(".bars td button", "#chord_diagram").click(function(){
		$("input[name^=\"strings[\"]", "#chord").val($(this).val());
		$("#chord").submit();
	});
});
</script>