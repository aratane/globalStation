package info.appteve.radioelectro;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by info on 20/04/16.
 */
public class PodcastAdapter extends ArrayAdapter {

    private final ArrayList<PodcastItem> pdcst;
    private final Context context;

    public PodcastAdapter(Context context, ArrayList<PodcastItem> pdcst) {
        super(context, R.layout.podcast_item, pdcst);

        this.pdcst = pdcst;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.podcast_item, parent, false);
        TextView categoryName = (TextView) item.findViewById(R.id.trackNameText);

        String maintxt = pdcst.get(position).track_name;
        String txtOut = maintxt.replaceFirst("\\.mp3$", "");

        categoryName.setText(txtOut);


        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }


}