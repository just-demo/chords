(function($) {
	$.fn.songLists = function(chords, songs){
		for (var i = -1; i < songs.length; ++i){
			var song = songs[i] || {"performer": "new", "name": "new", "text": "<textarea>put here song text...</textarea>"};
			$(this).append([
				"<div class='song noPrint'>",
					"<div class='songTitle'>",
						"<span class='songPerformer'>", song.performer, "</span>", " - ", "<span class='songName'>", song.name, "</span>",
					"</div>",
					"<div class='songContent'>",
						"<table>",
							"<tr class='songActions noPrint'>",
								"<td colspan='2'>",
									"<button type='button' class='stanspose' data-step='-1'>-</button>",
									"<button type='button' class='stanspose' data-step='1'>+</button>",
								"</td>",
							"</tr>",
							"<tr>",
								"<td class='songText'>",
									songToHTML(song.text),
								"</td>",
								"<td class='songChords'>",
								"</td>",
							"</tr>",
							"<tr class='songActions noPrint'>",
								"<td colspan='2'>",
									"<button type='button' class='printAction'>Print</button>",
								"</td>",
							"</tr>",
						"</table>",
					"</div>",
				"</div>"
			].join(""));
		}
		
		//$("textarea").autoResize({limit:10000}).change();

		$(".songTitle").click(function(){
			var songElement = findParentSong(this); //$(this).parent().parent();
			var songContentElement = $(".songContent", songElement);
			var isVisible = songContentElement.is(":visible");
			$(".songContent").hide('fast');
			$(".song").addClass("noPrint");
			if (!isVisible){
				songContentElement.show('fast', songContentElement[0].scrollIntoView);
				refreshChordDiagrams(songElement);
				songElement.removeClass("noPrint");
			}
		});

		$(".chord").easyTooltip({
			content: function(){
				var chordName = $.trim($(this).html());
				var chordDiagram = chordName in chords ? $.guitar.chordDiagram(chords[chordName].frets) : "Unknown<br/>chord";
				return "<div class='chord_popup'>" + chordDiagram + "</div>";
			}
		});
			
		$(".stanspose").click(function(){
			var songElement = findParentSong(this); //$(this).parent().parent();
			var songTextElement = $(".songText", songElement);
			var direction = $(this).data("step") < 0 ? "prev" : "next";
			$(".chord", songTextElement).each(function(index){
				var chord = $(this).html();
				if (chords[chord] && chords[chord][direction]){
					$(this).html(chords[chord][direction].name);
				}
			});
			refreshChordDiagrams(songElement);
		});

		$(".printAction").click(function(){
			window.print();
		});
		
		function findParentSong(element) {
			//return $(element).parent().parent();
			element = $(element);
			while (!element.hasClass("song")){
				element = element.parent();
			}
			return element;
		}
		
		function refreshChordDiagrams(songElement){
			var songTextElement = $(".songText", songElement);
			var songChords = [];
			$(".chord", songTextElement).each(function(index){
				songChords.push($(this).html());
			});
			songChords = unique(songChords);

			var buffer = [];
			var columns = 3;
			var rows = Math.ceil(songChords.length / columns);
			
			buffer.push("<table>");
			for (var i = 0; i < rows; ++i) {
				buffer.push("<tr>");
				for (var j = 0; j < columns; ++j) {
					buffer.push("<td>");
					var k = i * columns + j;
					if (songChords[k]){
						buffer.push("<div class='chordName'>", songChords[k], ":</div>");
						buffer.push(songChords[k] in chords ? $.guitar.chordDiagram(chords[songChords[k]].frets) : "Unknown<br/>chord");
					}
					buffer.push("</td>");
				}
				buffer.push("</tr>\n");
			}
			buffer.push("</table>");
			$(".songChords", songElement).html(buffer.join(""));
		}

		function unique(array){
			return $.grep(array, function(value, index){
				return index == $.inArray(value, array);
			});
		}
			
		function getKeys(map) {
			var keys = [];
		}
		
		function songToHTML(text){
			return text
				.replace(new RegExp("(^|\\s|\\()(" + Object.keys(chords).join("|") + ")(?=\\s|\\)|$)", "g"), "$1{$2}")
				.replace(/\n/g, "<br/>")
				.replace(/\s/g, "&nbsp;")
				.replace(/\{([^}]+)\}/g, "<span class='chord'>$1</span>");
		}
	}
})(jQuery);