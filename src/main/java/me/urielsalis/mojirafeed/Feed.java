package me.urielsalis.mojirafeed;

import com.sun.syndication.feed.synd.SyndEntryImpl; /**
 * mojirafeed
 * <p>
 * Created by urielsalis on 08/08/17.
 */
public class Feed {
    String link;
    String title;

    public Feed(SyndEntryImpl entry) {
        this.link = entry.getLink();
        this.title = entry.getTitle();
    }

    public String toHTML() {
        return "<div class=\"mui-panel\" onclick=\"window.open('"+link+"', '_blank');\"> "+title+"<br></div>\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (!link.equals(feed.link)) return false;
        return title.equals(feed.title);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }
}
