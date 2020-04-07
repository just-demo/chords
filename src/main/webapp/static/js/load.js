function loadJson(url){
    return loadSync(url, 'json');
}

function loadText(url){
    return loadSync(url, 'text');
}

function loadSync(url, dataType){
    let result;
    $.ajax({
        async: false,
        type: "GET",
        url: url,
        dataType: dataType,
        success: (data) => {
            result = data;
        }
    });
    return result;
}