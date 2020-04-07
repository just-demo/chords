function buildSongList(container, songs, chords){
	    songs = sortSongs(songs);
	    chords = convertChords(chords);
        songs.forEach(song => $(container).append(buildSongView(song)));
        $(container).append("<div class='newSong noPrint'>+</div>");

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

		$(".songText").dblclick(function(){
		    var $song = findParentSong(this);
            var $songView = $(this);
            var $songEdit = $(".songEdit", $song);
            var $textarea = $("<textarea>").css({
                "width": $songView.outerWidth() + 10,
                "height": $songView.outerHeight() + 20
            }).val(songHtmlToText($songView.html()));
            $songEdit.html("").append($textarea);
            $songView.hide();
            $songEdit.show();
            $("button").attr("disabled", true);
		});

        $(".songEdit").dblclick(function(){
            var $song = findParentSong(this);
            var $songView = $(".songText", $song);
            var $songEdit = $(this);
            setViewText($songView, $("textarea", $songEdit).val());
            $songEdit.hide();
            $songView.show();
            $("button").removeAttr("disabled");
            refreshChordDiagrams($song);
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

		$(".newSong").click(function() {
		    $(container).append(buildSongView({
                name: "Name...",
                performer: "Performer...",
                text: "Text..."
            }));
            // TODO: bind click and others or make them live!
		});

		function buildSongView(song) {
		    var $song = $([
                "<div class='song noPrint'>",
                    "<div class='songTitle'>",
                        "<span class='songPerformer'>", song.performer, "</span>", " - ", "<span class='songName'>", song.name, "</span>",
                    "</div>",
                    "<div class='songContent'>",
                        "<table>",
                            "<tr class='songActions noPrint'>",
                                "<td colspan='3'>",
                                    "<button type='button' class='stanspose' data-step='-1'>-</button>",
                                    "<button type='button' class='stanspose' data-step='1'>+</button>",
                                    "<button type='button' class='printAction'>Print</button>",
                                "</td>",
                            "</tr>",
                            "<tr>",
                                "<td class='songText'>",
                                "</td>",
                                "<td class='songEdit'>",
                                "</td>",
                                "<td class='songChords'>",
                                "</td>",
                            "</tr>",
                        "</table>",
                    "</div>",
                "</div>"
            ].join(""));
            setViewText($(".songText", $song), song.text);
            return $song;
		}

        function setViewText($songView, text) {
            $songView
                .html(songTextToHTML(text))
                .find(".chord").easyTooltip({
                    content: function(){
                        var chordName = $.trim($(this).html());
                        var chordDiagram = chordName in chords ? buildChordDiagram(chords[chordName].frets) : "Unknown<br/>chord";
                        return "<div class='chord_popup'>" + chordDiagram + "</div>";
                    }
                });
        }

		function convertChords(chords) {
		    var chordMap = {};
            chords.forEach(chord => {
                chordMap[chord.name] = {...chord};
            });


            for (var currentName in chordMap) {
                var nextName = chordMap[currentName].next;
                if (nextName in chordMap) {
                    chordMap[currentName].next = chordMap[nextName];
                    chordMap[nextName].prev = chordMap[currentName];
                } else {
                    chordMap[currentName].next = null;
                }
            }
            return chordMap;
		}

		function sortSongs(songs) {
		    songs = [...songs];
		    songs.sort((a, b) => a.performer < b.performer ? -1 : (a.performer > b.performer ? 1 : 0));
            return songs;
		}
		
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
			var songChords = distinct($(".chord", songTextElement)
			    .toArray()
			    .map(chord => $(chord).html()));

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
						buffer.push(songChords[k] in chords ? buildChordDiagram(chords[songChords[k]].frets) : "Unknown<br/>chord");
					}
					buffer.push("</td>");
				}
				buffer.push("</tr>\n");
			}
			buffer.push("</table>");
			$(".songChords", songElement).html(buffer.join(""));
		}

		function distinct(array){
			return Array.from(new Set(array))
		}

		function songTextToHTML(text){
			return text
				.replace(new RegExp("(^|\\s|\\()(" + Object.keys(chords).join("|") + ")(?=\\s|\\)|$)", "g"), "$1{$2}")
				.replace(/\n/g, "<br/>")
				.replace(/\s/g, "&nbsp;")
				.replace(/\{([^}]+)\}/g, "<span class='chord'>$1</span>");
		}

        function songHtmlToText(html){
            return html
                .replace(new RegExp("<br.*?>", "g"), "\n")
                .replace(new RegExp("&nbsp;", "g"), " ")
                .replace(/<span.*?>(.+?)<\/span>/g, "{$1}");
        }
}