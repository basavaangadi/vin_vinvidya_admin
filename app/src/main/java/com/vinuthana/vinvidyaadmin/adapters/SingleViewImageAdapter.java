package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class SingleViewImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private JSONArray jsonArray;
    int pos;
    public SingleViewImageAdapter(Context context,JSONArray jsonArray,int pos) {
        this.context = context;
        this.pos= pos;
        this.jsonArray = jsonArray;
    }


    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_single_view_image,container,false);
        ImageView iv_image = (ImageView)view.findViewById(R.id.iv_image);
        TextView tview_title = (TextView)view.findViewById(R.id.tview_title);
        try {
            JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(position);
            Picasso.with(context).load(jsonObject.getString(context.getString(R.string.key_Image))).into(iv_image);
            tview_title.setText(jsonObject.getString(context.getString(R.string.key_Description)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
