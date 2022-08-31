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
 * Created by info on 21/04/16.
 */


public class OfflinePodcastAdapter extends ArrayAdapter {

    private final ArrayList<OfflinePodcastItem> pdcst;
    private final Context context;

    public OfflinePodcastAdapter(Context context, ArrayList<OfflinePodcastItem> pdcst) {
        super(context, R.layout.offpodcast_item, pdcst);

        this.pdcst = pdcst;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.offpodcast_item, parent, false);
        TextView categoryName = (TextView) item.findViewById(R.id.offtrackNameText);
        String maintxt = pdcst.get(position).offtrack_name;
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
