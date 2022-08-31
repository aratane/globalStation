package info.appteve.radioelectro;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;


public class XmlWriter {

    private List<XmlParser.Item> items;
    private Context context;

    public XmlWriter(List<XmlParser.Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void writeXmlList(){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String filename = "favourites";
        String xmlText;
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "rss");
            serializer.attribute("", "version", "2.0");
            serializer.startTag("", "channel");
            if(items != null) {
                for (XmlParser.Item item : items) {
                    serializer.startTag("", "item");

                    serializer.startTag("", "url");
                    serializer.text(item.url);
                    serializer.endTag("", "url");

                    serializer.startTag("", "title");
                    serializer.text(item.title);
                    serializer.endTag("", "title");

                    serializer.startTag("", "image");
                    serializer.text(item.image);
                    serializer.endTag("", "image");

                    serializer.startTag("", "rid");
                    serializer.text(item.rid);
                    serializer.endTag("", "rid");

                    serializer.endTag("", "item");
                }
            }
            serializer.endTag("","channel");
            serializer.endTag("", "rss");
            serializer.endDocument();
            //return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        xmlText = writer.toString();
        Log.d("OH MY", xmlText);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(xmlText.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeXmlListClean(){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        String filename = "favourites";
        String xmlText;
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "rss");
            serializer.attribute("", "version", "2.0");
            serializer.startTag("", "channel");

            serializer.endTag("","channel");
            serializer.endTag("", "rss");
            serializer.endDocument();

            Log.d("FILE","OK MAKE2");
            //return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        xmlText = writer.toString();
        Log.d("OH MY", xmlText);
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(xmlText.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

