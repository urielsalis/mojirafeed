package me.urielsalis.mojirafeed;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * mojirafeed
 * <p>
 * Created by urielsalis on 08/08/17.
 */
public class FeedHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = Main.getResponse(Main.secondsToPause);
        t.sendResponseHeaders(200, response.getBytes(Charset.forName("UTF-8")).length);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
