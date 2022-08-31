package info.appteve.radioelectro;

/**
 * Created by info on 21/04/16.
 */


public class OfflinePodcastItem {


    String offtrack_name;
    String offtrack_file;


    public OfflinePodcastItem(String offtrack_name, String offtrack_file ) {

        super();
        this.offtrack_name = offtrack_name;
        this.offtrack_file = offtrack_file;

    }

    public String getOfftrack_name(){
        return offtrack_name;
    }

    public void setOfftrack_name (String offtrack_name){
        this.offtrack_name = offtrack_name;

    }

    public String getOfftrack_file (){
        return offtrack_file;
    }
    public void setOfftrack_file (String offtrack_file){
        this.offtrack_file = offtrack_file;

    }



}

