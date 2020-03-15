function parseChordsXML(data){
    var singleElement = (node, element) => node.getElementsByTagName(element)[0].childNodes[0].nodeValue;
    var multipleElements = (node, element) => Array.from(node.getElementsByTagName(element))
        .map(el => el.childNodes[0].nodeValue);

    var chords = Array.from(new DOMParser().parseFromString(data, "text/xml").getElementsByTagName("chord"))
        .map(chord => ({
            name: singleElement(chord, "name"),
            next: singleElement(chord, "next"),
            frets: multipleElements(chord, "fret")
        }))
        .reduce((chords, chord) => {
           chords[chord.name] = chord;
           return chords;
        }, {});

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