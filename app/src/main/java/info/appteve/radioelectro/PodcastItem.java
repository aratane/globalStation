package info.appteve.radioelectro;

/**
 * Created by info on 20/04/16.
 */
public class PodcastItem {


    String track_name;
    String track_file;
    String file;

    public PodcastItem(String track_name, String track_file, String file ) {

        super();
        this.track_name = track_name;
        this.track_file = track_file;
        this.file = file;

    }

    public String getTrack_name(){
        return track_name;
    }

    public void setTrack_name (String track_name){
        this.track_name = track_name;

    }

    public String getTrack_file(){
        return track_file;
    }

    public void setTrack_file(String track_file){
        this.track_file = track_file;

    }
    public String getFileCast(){
        return file;
    }

    public void setFileCast(String file){
        this.file = file;

    }


}
