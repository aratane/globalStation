package info.appteve.radioelectro;

/**
 * Created by info on 04/04/16.
 */
public class RadioItem {

    private String image;
    private String radiourl;
    private String nameradio;
    private String idradio;
    private String image_filez;

    public RadioItem(String idradio, String nameradio, String image, String radiourl, String image_filez) {
        super();
        this.idradio = idradio;
        this.image = image;
        this.nameradio = nameradio;
        this.radiourl = radiourl;
        this.image_filez = image_filez;
    }


    public String getImage(){
        return image;
    }

    public void setImage (String image){
        this.image = image;

    }

    public String getRadiourl(){
        return radiourl;
    }

    public void setRadiourl (String radiourl){
        this.radiourl = radiourl;

    }

    public String getNameradio(){
        return nameradio;
    }

    public void setNameradio (String nameradio){
        this.nameradio = nameradio;

    }

    public String getIdradio(){
        return idradio;
    }

    public void setIdradio (String idradio){
        this.idradio = idradio;

    }

    public String getImage_filez(){
        return image_filez;
    }

    public void setImage_filez (String image_filez){
        this.image_filez = image_filez;

    }


}
