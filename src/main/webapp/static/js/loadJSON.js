function loadJSON(dataUrl){
    var result;
    $.ajax({
        async: false,
        type: "GET",
        url: dataUrl,
        dataType: "json",
        success: (data) => {
            result = data;
        }
    });
    return result;
}