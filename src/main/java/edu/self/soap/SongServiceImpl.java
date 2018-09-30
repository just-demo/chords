package edu.self.soap;

import edu.self.soap.model.Song;

import javax.jws.WebService;

@WebService(endpointInterface = "edu.self.soap.SongService"
/*, serviceName = "SSSNN", name = "NNN", targetNamespace = "ttttnnnn" */
)
public class SongServiceImpl implements SongService {
    @Override
    public String parseSimple(String song, String data) {
        String somePrefix = "v1";
        return data + somePrefix + song;
    }

    @Override
    public Song parse(Song song, String search, String replacement){
        song.setText(song.getText().replaceAll(search, replacement));
        return song;
    }
}
