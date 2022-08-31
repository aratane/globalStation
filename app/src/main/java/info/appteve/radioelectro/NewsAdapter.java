package info.appteve.radioelectro;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import info.appteve.radioelectro.constants.Constants;

/**
 * Created by info on 05/04/16.
 */
public class NewsAdapter extends ArrayAdapter implements Constants {

    private final ArrayList<NewsItem> newss;
    private final Context context;

    public NewsAdapter(Context context, ArrayList<NewsItem> newss) {
        super(context, R.layout.trend_item, newss);

        this.newss = newss;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.news_item, parent, false);
        TextView categoryName = (TextView) item.findViewById(R.id.newsNameText);
        categoryName.setText(newss.get(position).nameNews);

        CircleImageView categoryImage = (CircleImageView) item.findViewById(R.id.newsImage);

        String urlImage  = GENERAL_API_URL + UPLOADS_FOLDER + newss.get(position).imnews_file;
        System.out.println(urlImage);

        Glide.with(getContext())
                .load(urlImage)
                .placeholder(R.drawable.defimage)
                .dontAnimate()
                .centerCrop()
                .into(categoryImage);
        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }


}
