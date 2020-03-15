function parseSongsXML(data){
    var singleElement = (node, element) => node.getElementsByTagName(element)[0].childNodes[0].nodeValue;
    return Array.from(new DOMParser().parseFromString(data, "text/xml") .getElementsByTagName("song"))
        .map(song => ({
            name: singleElement(song, "name"),
            text: singleElement(song, "text"),
            performer: singleElement(song, "performer")
        }))
        .sort((a, b) => a.performer < b.performer ? -1 : (a.performer > b.performer ? 1 : 0));
}