function httpReadJson(url){
    return httpRead(url, 'json');
}

function httpReadText(url){
    return httpRead(url, 'text');
}

function httpRead(url, dataType){
    let result;
    $.ajax({
        url: url,
        type: "GET",
        async: false,
        dataType: dataType,
        success: (data) => {
            result = data;
        }
    });
    return result;
}

function httpWriteText(url, text){
    $.ajax({
        url: url,
        type: "POST",
        async: false,
        dataType: 'text',
        data: text
    });
}

function httpDelete(url){
    $.ajax({
        url: url,
        type: "DELETE",
        async: false
    });
}