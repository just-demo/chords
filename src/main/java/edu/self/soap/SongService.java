package edu.self.soap;

import edu.self.soap.model.Song;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@HandlerChain(file = "ws-handlers.xml")
public interface SongService {
    @WebMethod
    String parseSimple(String song, String data);

    @WebMethod
    Song parse(Song song, String search, String replacement);
}
