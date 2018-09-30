(function($) {
	$.guitar = $.guitar || {};
	
	$.guitar.load = function(dataUrl){
		var result;
		$.ajax({
			async: false,
			type: "GET",
			url: dataUrl,
			dataType: "html",
			success: function(data) {
				result = data;
			}
		});
		return result;
	}
})(jQuery);