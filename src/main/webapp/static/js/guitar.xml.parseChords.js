(function($) {
	$.guitar = $.guitar || {};
	$.guitar.xml = $.guitar.xml || {};
	
	$.guitar.xml.parseChords = function(data){
		var chords = {};
		var parser = new DOMParser();
		var chordList = parser.parseFromString(data, "text/xml");
		chordList = chordList.getElementsByTagName("chord");
		
		var fields = ["name", "next"];
		for (var i = 0; i < chordList.length; ++i){
			var chord = {};
			for (var j = 0; j < fields.length; ++j){
				chord[fields[j]] = chordList[i].getElementsByTagName(fields[j])[0].childNodes[0].nodeValue;
			}
			var fretsList = [];
			var frets = chordList[i].getElementsByTagName("fret");
			for (var j = 0; j < frets.length; ++j){
				fretsList.push(frets[j].childNodes[0].nodeValue);
			}
			chord.frets = fretsList;
			chords[chord.name] = chord;
		}
		
		for (var currentName in chords) {
			var nextName = chords[currentName].next;
			delete chords[currentName].next;
			if (nextName in chords) {
				chords[currentName].next = chords[nextName];
				chords[nextName].prev = chords[currentName];
			}
		}
		return chords;
	}
})(jQuery);