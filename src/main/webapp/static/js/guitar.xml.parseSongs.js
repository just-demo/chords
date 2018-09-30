(function($) {
	$.guitar = $.guitar || {};
	$.guitar.xml = $.guitar.xml || {};
	
	$.guitar.xml.parseSongs = function(data){
		var songs = [];
		var parser = new DOMParser();
		var songList = parser.parseFromString(data, "text/xml");
		songList = songList.getElementsByTagName("song");
		
		var fields = ["name", "text", "performer"];
		for (var i = 0; i < songList.length; ++i){
			var song = {};
			for (var j = 0; j < fields.length; ++j){
				song[fields[j]] = songList[i].getElementsByTagName(fields[j])[0].childNodes[0].nodeValue;
			}
			songs.push(song);
		}
		songs.sort(function(a, b){
			return a.performer < b.performer ? -1 : (a.performer > b.performer ? 1 : 0);
		});
		return songs;
	}
})(jQuery);