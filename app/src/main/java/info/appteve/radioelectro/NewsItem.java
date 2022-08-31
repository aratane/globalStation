package info.appteve.radioelectro;

/**
 * Created by info on 05/04/16.
 */
public class NewsItem {


    String imageNews;
    String nameNews;
    String textNews;
    String imnews_file;

    public NewsItem(String imageNews, String nameNews, String textNews, String imnews_file ) {
        super();
        this.imageNews = imageNews;
        this.nameNews = nameNews;
        this.textNews = textNews;
        this.imnews_file = imnews_file;

    }

    public String getImageNews(){
        return imageNews;
    }

    public void setImageNews (String imageNews){
        this.imageNews = imageNews;

    }

    public String getNameNews(){
        return nameNews;
    }

    public void setNameNews (String nameNews){
        this.nameNews = nameNews;

    }

    public String getTextNews(){
        return textNews;
    }

    public void setTextNews (String textNews){
        this.textNews = textNews;

    }

    public String getImnews_file(){
        return imnews_file;
    }

    public void setImnews_file (String imnews_file){
        this.imnews_file = imnews_file;

    }

}
