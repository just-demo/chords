function loadSongs() {
    let songs = [];
    let performers = fetchLinks(loadHTML('data/songs/'));
    performers.forEach(performer => {
        let songNames = fetchLinks(loadHTML('data/songs/' + performer));
        songNames.forEach(name => songs.push({
            performer: performer,
            name: trimExtension(name)
        }));
    });
    return songs;

    function trimExtension(str) {
        return str.replace(/\.[^.]+$/, "");
    }

    function trimTrailingSlash(str) {
        return str.replace(/\/$/, "");
    }

    function fetchLinks(html) {
        const regex = /<a.*?>(.*?)<\/a>/g;
        const links = [];
        let match;
        while ((match = regex.exec(html))) {
            links.push(trimTrailingSlash(match[1]));
        }
        return links;
    }

    function loadHTML(dataUrl){
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
}