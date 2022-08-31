package info.appteve.radioelectro;

public class ImageItem {
    private String id;
    private String name;
    private String image;
    private String filez;

    public ImageItem(String id, String name, String image, String filez) {
        super();
        this.id = id;
        this.image = image;
        this.name = name;
        this.filez = filez;
    }



    public String getName(){
        return name;
    }

    public void setName (String name){
        this.name = name;

    }
    public String getId(){
        return id;
    }

    public void setId (String id){
        this.id = id;

    }

    public String getImagee(){
        return image;
    }

    public void setImage (String image){
        this.image = image;

    }

    public String getFilez(){
        return filez;
    }

    public void setFilez (String filez) {
        this.filez = filez;

    }





}
