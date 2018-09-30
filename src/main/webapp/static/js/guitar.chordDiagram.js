(function($) {
	$.guitar = $.guitar || {};
	
	$.guitar.chordDiagram = function(frets){
		if (!$.isArray(frets)) {
			frets = decodeFrets(frets);
		}
		var min = null;
		var max = null;
		for (var i = 0; i < frets.length; ++i){
			var fret = decodeFret(frets[i]);
			frets[i] = fret;
			if (fret != null){
				if (fret < min || min === null){
					min = fret;
				}
				if (fret > max || max === null){
					max = fret;
				}
			}
		}
		
		var bar = hasBar(frets) ? min : null;
		
		min = min === null ? 0 : min;
		max = max === null ? 1 : max;
		
		var maxLength = Math.max(("" + max).length, 3);
		
		//expand min/max to -1/+1
		min = Math.max(0, min - 1);
		max = Math.max(max + 1, min + 5 - (min > 0 ? 1 : 0));
		
		var buffer = [];
		buffer.push("<table border='0' cellpadding='0' cellspacing='0'>");
		for (var i = 0; i < frets.length; ++i){
			var fret = frets[i];
			buffer.push("<tr>");
			buffer.push("<td>", leftPad(encodeFret(fret), maxLength, " ").replace(/\s/g, "&nbsp;"), "</td><td>|</td>");
			for (var j = Math.max(min, 1); j <= max; ++j){
				buffer.push("<td>" + center(j === fret || j === bar ? (fret !== null ? j : "x") : "-", maxLength, "-") + "</td><td>|</td>");
			}
			buffer.push("</tr>\n");
		}
		buffer.push("</table>");
		return buffer.join("");
		
		function decodeFrets(frets) {
			frets = frets.split("-");
			for (var i = 0; i < frets.length; ++i){
				frets[i] = decodeFret(frets[i]);
			}
		}

		function encodeFret(fret){
			return fret === null ? "x" : "" + fret;
		}
		
		function decodeFret(fret){
			return "x" === fret || "" === fret ? null : parseInt(fret, 10);
		}
		
		function repeat(length, character){
			return length > 0 ? (new Array(length + 1).join(character)) : "";
		}
		
		function leftPad(string, length, character){
			string += "";
			return repeat(length - string.length, character) + string;
		}
		
		function rigthPad(string, length, character){
			string += "";
			return string + repeat(length - string.length, character);
		}
		
		function center(string, length, character){
			string += "";
			var left = Math.ceil((length - string.length) / 2);
			var right = (length - string.length - left);
			return repeat(left, character) + string + repeat(right, character);
		}
	
		function hasBar(frets){
			var hasClosed = false;
			for (var i = 0; i < frets.length; ++i){
				var fret = frets[i];
				if (fret === null){
					hasClosed = true;
				}
				else if (hasClosed){
					return false;
				}
			}
			return true;
		}
	}
})(jQuery);