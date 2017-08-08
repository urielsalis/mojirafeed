package me.urielsalis.mojirafeed;

import com.sun.net.httpserver.HttpServer;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * mojirafeed
 * <p>
 * Created by urielsalis on 08/08/17.
 */
public class Main {
    public static ArrayList<Feed> feeds = new ArrayList<Feed>();

    public static long secondsToPause;
    public static List<String> ignoreList = new ArrayList<String>();

    public static void main(String[] args) throws IOException, FeedException, InterruptedException {
        if(args.length < 2) {
            System.out.println("Arguments: <seconds to sleep> <url of feed>");
            System.exit(0);
        }
        secondsToPause = Long.parseLong(args[0]); //seconds to sleep
        String url = args[1]; //URL of feed

        startWebServer();

        System.out.println("http://localhost:8000/feed");
        while(true) {
            readFeed(url);
            TimeUnit.SECONDS.sleep(secondsToPause);
        }
    }

    private static void readFeed(String url) throws IOException, FeedException {
        URL feedSource = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        List<SyndEntryImpl> entries = feed.getEntries();
        for(SyndEntryImpl entry: entries) {
            Feed feedObj = new Feed(entry);
            if(!feeds.contains(feedObj) && !shouldIgnore(feedObj)) {
                feeds.add(feedObj);
            }
        }
    }

    private static boolean shouldIgnore(Feed feedObj) {
        for(String str: ignoreList) {
            if(feedObj.toHTML().contains(str)) {
                return true;
            }
        }
        return false;
    }

    private static void startWebServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0); //Create http server
        server.createContext("/feed", new FeedHandler()); //show feed
        server.createContext("/ignore", new IgnoreHandler());

        server.start(); //start it

    }

    public static String getResponse(long secondsToPause) {
        StringBuffer buffer = new StringBuffer("<html><head><title>Feed</title><base target=\"_blank\"><meta http-equiv=\"refresh\" content=\""+secondsToPause+"\" >    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <!-- load MUI -->\n" +
                "    <link href=\"//cdn.muicss.com/mui-0.9.21/css/mui.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "    <script src=\"//cdn.muicss.com/mui-0.9.21/js/mui.min.js\"></script></head><body>    <div class=\"mui-container\">\n" +
                "      <div class=\"mui-appbar\"><div class=\"mui--text-display2\"><center>Mojira feed</center></div></h1></div>\n");
        for(Feed feed: feeds) {
            buffer.append(feed.toHTML()).append("<div class=\"mui-divider\"></div>\n");
        }
        buffer.append("</body></html>");
        return buffer.toString();
    }

    public static void ignore() {
        List<Feed> toDelete = new ArrayList<Feed>();
        for(Feed feed: feeds) {
            if(shouldIgnore(feed)) toDelete.add(feed);
        }
        for(Feed feed: toDelete) {
            feeds.remove(feed);
        }
    }
}
