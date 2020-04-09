// TODO: hover on chord name on the right side should show all chord variants
// TODO: create selection checkbox
// TODO: create Delete button
function buildSongList(container, songs, chords){
    songs = convertSongs(songs);
    chords = convertChords(chords);
    songs.forEach(song => $(container).append(buildSongView(song)));
    $(container).append("<div class='newSong noPrint'>+</div>");

    $(".songTitle").click(function(){
        let $song = findParentSong(this);
        let $songContent = $(".songContent", $song);
        let isVisible = $songContent.is(":visible");
        $(".songContent").hide('fast');
        $(".song").addClass("noPrint");
        if (!isVisible){
            if (!$song.data("initialized")) {
                let performer = $(".songPerformer", $song).html();
                let name = $(".songName", $song).html();
                let text = httpReadText(songPath(performer, name));
                setViewText($(".songText", $song), text);
                refreshChordDiagrams($song);
                $song.data("saved", text);
                $song.data("initialized", true);
            }
            $songContent.show('fast', $songContent[0].scrollIntoView);
            $song.removeClass("noPrint");
        }
    });

    $(".songText").dblclick(function(){
        let $song = findParentSong(this);
        let $songView = $(this);
        let $songEdit = $(".songEdit", $song);
        let $textarea = $("<textarea>").css({
            "width": $songView.outerWidth() + 10,
            "height": $songView.outerHeight() + 20
        }).val(songHtmlToText($songView.html()));
        $songEdit.html("").append($textarea);
        $songView.hide();
        $songEdit.show();
        $("button").attr("disabled", true);
    });

    $(".songEdit").dblclick(function(){
        let $song = findParentSong(this);
        let $songView = $(".songText", $song);
        let $songEdit = $(this);
        let performer = $(".songPerformer", $song).html();
        let name = $(".songName", $song).html();
        let text = $("textarea", $songEdit).val()
        setViewText($songView, text);
        $songEdit.hide();
        $songView.show();
        $("button", $song).removeAttr("disabled");
        refreshChordDiagrams($song);
        refreshModified($song);
    });

    $(".transpose").click(function(){
        let $song = findParentSong(this);
        let $songText = $(".songText", $song);
        let direction = $(this).data("step") < 0 ? "prev" : "next";
        $(".chord", $songText).each(function(){
            let chord = $(this).html();
            if (chords[chord] && chords[chord][direction]){
                $(this).html(chords[chord][direction].name);
            }
        });
        refreshChordDiagrams($song);
        refreshModified($song);
    });

    $(".revert").click(function(){
        let $song = findParentSong(this);
        $(".revert", $song).attr("disabled", true);
        $(".save", $song).attr("disabled", true);
        let $songView = $(".songText", $song);
        setViewText($songView, $song.data("saved"));
        refreshChordDiagrams($song);
    });

    $(".save").click(function(){
        let $song = findParentSong(this);
        $(".revert", $song).attr("disabled", true);
        $(".save", $song).attr("disabled", true);
        let $songView = $(".songText", $song);
        let performer = $(".songPerformer", $song).html();
        let name = $(".songName", $song).html();
        let text = songHtmlToText($songView.html());
        httpWriteText(songPath(performer, name), text);
        $song.data("saved", text);
    });

    $(".delete").click(function(){
        if (window.confirm("Delete the song?")) {
            let $song = findParentSong(this);
            let performer = $(".songPerformer", $song).html();
            let name = $(".songName", $song).html();
            $song.remove();
            httpDelete(songPath(performer, name));
        }
    });

    $(".printAction").click(function(){
        window.print();
    });

    $(".newSong").click(function() {
        $(container).append(buildSongView({
            name: "Name...",
            performer: "Performer...",
            text: "Text..."
        }));
        // TODO: bind click and others or make them live!
    });

    function buildSongView(song) {
        let $song = $([
            "<div class='song noPrint'>",
                "<div class='songTitle ", song.selected ? "selected" : "", "'>",
                    "<span class='songPerformer'>", song.performer, "</span>",
                    " - ",
                    "<span class='songName'>", song.name, "</span>",
                "</div>",
                "<div class='songContent'>",
                    "<table>",
                        "<tr class='songActions noPrint'>",
                            "<td colspan='3'>",
                                "<button type='button' class='transpose' data-step='-1'>-</button>",
                                "<button type='button' class='transpose' data-step='1'>+</button>",
                                "<button type='button' class='revert right' disabled>Revert</button>",
                                "<button type='button' class='save' disabled>Save</button>",
                                "<button type='button' class='delete'>Delete</button>",
                                "<button type='button' class='printAction'>Print</button>",
                            "</td>",
                        "</tr>",
                        "<tr>",
                            "<td class='songText'></td>",
                            "<td class='songEdit'></td>",
                            "<td class='songChords'></td>",
                        "</tr>",
                    "</table>",
                "</div>",
            "</div>"
        ].join(""));
        return $song;
    }

    function setViewText($songView, text) {
        $songView
            .html(songTextToHTML(text))
            .find(".chord").easyTooltip({
                content: function(){
                    let chordName = $.trim($(this).html());
                    let chordDiagram = chordName in chords ? buildChordDiagram(chords[chordName].frets) : "Unknown<br/>chord";
                    return "<div class='chord_popup'>" + chordDiagram + "</div>";
                }
            });
    }

    function convertChords(chords) {
        let chordMap = {};
        chords.forEach(chord => {
            chordMap[chord.name] = {...chord};
        });


        for (let currentName in chordMap) {
            let nextName = chordMap[currentName].next;
            if (nextName in chordMap) {
                chordMap[currentName].next = chordMap[nextName];
                chordMap[nextName].prev = chordMap[currentName];
            } else {
                chordMap[currentName].next = null;
            }
        }
        return chordMap;
    }

    function convertSongs(songMap) {
        let songList = [];
        for (let [performer, songs] of Object.entries(songMap)) {
            for (let [name, selected] of Object.entries(songs)) {
                songList.push({
                    performer: performer,
                    name: name,
                    selected: selected
                });
            }
        }
        songList.sort((s1, s2) => compare(s1, s2,
            // selected songs go first
            s => !s.selected,
            s => s.performer,
            s => s.name
        ));
        return songList;
    }

    function compare(o1, o2, ...mappers) {
        mappers = mappers.length ? mappers : [o => o];
        for (let mapper of mappers) {
            let v1 = mapper(o1);
            let v2 = mapper(o2);
            let diff = v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
            if (diff) {
                return diff;
            }
        }
        return 0;
    }

    function songPath(performer, name) {
        return `data/songs/${performer}/${name}.txt`;
    }

    function findParentSong(element) {
        let $element = $(element);
        while (!$element.hasClass("song")){
            $element = $element.parent();
        }
        return $element;
    }

    function refreshModified($song) {
        let $songView = $(".songText", $song);
        let $revert = $(".revert", $song);
        let $save = $(".save", $song);
        let text = songHtmlToText($songView.html());
        if (text == $song.data("saved")) {
            $revert.attr("disabled", true);
            $save.attr("disabled", true);
        } else {
            $revert.removeAttr("disabled");
            $save.removeAttr("disabled");
        }
    }

    function refreshChordDiagrams($song){
        let $songText = $(".songText", $song);
        let songChords = distinct($(".chord", $songText)
            .toArray()
            .map(chord => $(chord).html()));

        let buffer = [];
        let columns = 3;
        let rows = Math.ceil(songChords.length / columns);

        buffer.push("<table>");
        for (let i = 0; i < rows; ++i) {
            buffer.push("<tr>");
            for (let j = 0; j < columns; ++j) {
                buffer.push("<td>");
                let k = i * columns + j;
                if (songChords[k]){
                    buffer.push("<div class='chordName'>", songChords[k], ":</div>");
                    buffer.push(songChords[k] in chords ? buildChordDiagram(chords[songChords[k]].frets) : "Unknown<br/>chord");
                }
                buffer.push("</td>");
            }
            buffer.push("</tr>\n");
        }
        buffer.push("</table>");
        $(".songChords", $song).html(buffer.join(""));
    }

    function distinct(array){
        return Array.from(new Set(array))
    }

    function songTextToHTML(text){
        return text
            .replace(new RegExp("(^|\\s|\\()(" + Object.keys(chords).join("|") + ")(?=\\s|\\)|$)", "g"), "$1{$2}")
            .replace(/\n/g, "<br/>")
            .replace(/\s/g, "&nbsp;")
            .replace(/\{([^}]+)\}/g, "<span class='chord'>$1</span>");
    }

    function songHtmlToText(html){
        return html
            .replace(new RegExp("<br.*?>", "g"), "\n")
            .replace(new RegExp("&nbsp;", "g"), " ")
            .replace(/<span.*?>(.+?)<\/span>/g, "{$1}");
    }
}