function httpReadJson(url){
    return httpRead(url, 'json');
}

function httpReadText(url){
    return httpRead(url, 'text');
}

function httpRead(url, dataType){
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

function httpWriteText(url, text){
    $.ajax({
        async: false,
        type: "POST",
        url: url,
        dataType: 'text',
        data: text
    });
}