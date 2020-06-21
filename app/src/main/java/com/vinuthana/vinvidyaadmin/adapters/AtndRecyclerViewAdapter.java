package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;

/**
 * Created by Administrator on 12/12/2017.
 */

public class AtndRecyclerViewAdapter extends RecyclerView.Adapter<AtndRecyclerViewAdapter.MyViewHolder> {
    JSONArray atndArray;
    Context ntcContext;

    public AtndRecyclerViewAdapter(JSONArray atndArray, Context ntcContext) {
        this.atndArray = atndArray;
        this.ntcContext = ntcContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) ntcContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.atnd_card_view_layout,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.tvDateAtndCard.setText(atndArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_Date)));
            holder.tvDayAtndCard.setText(atndArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_Day)));
            holder.tvStatusAtndCard.setText(atndArray.getJSONObject(position).getString(ntcContext.getString(R.string.key_Status)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return atndArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateAtndCard, tvDayAtndCard, tvStatusAtndCard;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvDateAtndCard = (TextView) itemView.findViewById(R.id.tvDateAtndCard);
            tvDayAtndCard = (TextView) itemView.findViewById(R.id.tvDayAtndCard);
            tvStatusAtndCard = (TextView) itemView.findViewById(R.id.tvStatusAtndCard);
        }
    }
}
