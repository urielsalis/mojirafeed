package me.urielsalis.mojirafeed;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * mojirafeed
 * <p>
 * Created by urielsalis on 08/08/17.
 */
public class IgnoreHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
        Main.ignoreList.add(params.get("ignore"));
        String response = "Ignored";
        t.sendResponseHeaders(200, response.getBytes(Charset.forName("UTF-8")).length);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        Main.ignore();
    }


    public Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

}
