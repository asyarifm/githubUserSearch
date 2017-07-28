package usersearch.github.com.githubusersearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<List> {

    private Context context;
    private ArrayList<List> list;

    TextView username_text_view;
    ImageView avatar_image_view;
    private URL url = null;
    Bitmap bmp;

    public ListAdapter(Context context, ArrayList<List> list) {
        super(context, R.layout.activity_main, list);

        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Get rowView from inflater
        View rowView = inflater.inflate(R.layout.user_list, parent, false);

        //Get the two text view from the rowView
         username_text_view = (TextView) rowView.findViewById(R.id.username_text_view);
         avatar_image_view = (ImageView) rowView.findViewById(R.id.avatar_image_view);

        //Set the text for textView
        username_text_view.setText(list.get(position).get(0).toString());

        //set image for imageview

        try {
            url = new URL(list.get(position).get(1).toString());
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            avatar_image_view.setImageBitmap(bmp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rowView;
    }

}
