function loadXML(dataUrl){
    var result;
    $.ajax({
        async: false,
        type: "GET",
        url: dataUrl,
        dataType: "html",
        success: (data) => {
            result = data;
        }
    });
    return result;
}