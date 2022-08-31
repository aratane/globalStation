package info.appteve.radioelectro;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class XmlParser {
    // No namespace.
    private static final String ns = null;


    public List<Item> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List Items = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the item tag
            if (name.equals("item")) {
                Items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return Items;
    }

    // This class represents a single item (post) in the XML feed.
    // It includes the data members "title", "link" and "image".
    public static class Item {
        public final String title;
        public final String url;
        public final String image;
        public final String rid;
        public Item(String title, String url, String image, String rid) {
            this.title = title;
            this.url = url;
            this.image = image;
            this.rid = rid;
        }
    }

    // Parses the contents of an entry. If it encounters a title or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String url = null;
        String image = null;
        String rid = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("url")) {
                url = readUrl(parser);
            } else if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("image")) {
                image = readImage(parser);
            } else if (name.equals("rid")) {
                rid = readId(parser);
            } else {
                skip(parser);
            }
        }
        return new Item(title, url, image,rid);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "url");
        String url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "url");
        return url;
    }

    private String readId(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "rid");
        String rid = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "rid");
        return rid;
    }

    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "image");
        String image = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "image");
        return image;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
