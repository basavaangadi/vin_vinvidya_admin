package com.vinuthana.vinvidyaadmin.adapters;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClsswiseFeesRecyclerViewAdapter extends  RecyclerView.Adapter<ClsswiseFeesRecyclerViewAdapter.MyViewHolder> {
    JSONArray jsonArray;
    Context mContext;
    String strFeesType;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRollNum,tvStudName, tvTotalAmt,tvBalAmt;


        //androidx.cardview.widget.CardView CardView;

        public MyViewHolder(View view) {
            super(view);

            tvRollNum = view.findViewById(R.id.tvRollNum);
            tvStudName = view.findViewById(R.id.tvStudName);
            tvTotalAmt = view.findViewById(R.id.tvTotalAmt);
            tvBalAmt = view.findViewById(R.id.tvBalAmt);
            //CardView = view.findViewById(R.id.CardView);
            if(strFeesType.equalsIgnoreCase("Admission Fees")){
                tvBalAmt.setVisibility(View.VISIBLE);
            }
            else {
                tvBalAmt.setVisibility(View.INVISIBLE);
            }
            tvStudName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudName.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
    public ClsswiseFeesRecyclerViewAdapter(JSONArray jsonArray, Context context,String strFeesType)
    {
        this.jsonArray = jsonArray;
        this.mContext=context;
        this.strFeesType=strFeesType;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classwise_feeslist,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object= jsonArray.getJSONObject(position);
            String strRollNum=object.getString("RollNo");
            String strStudName=object.getString("Studentname");
            String strTotalAmt=object.getString("Total_amount");
            String strBalAmt=object.getString("Balance");
            Log.e("jsonObject is ","@ "+String.valueOf(position)+" "+object.toString() );
            String rollNum = "<font color=#FF8C00>Roll No  : </font> <font color=#000000>"+strRollNum+"</font>";
            holder.tvRollNum.setText(Html.fromHtml(rollNum));
            String studName = "<font color=#FF8C00>Name : </font> <font color=#000000>"+strStudName+"</font>";
            holder.tvStudName.setText(Html.fromHtml(studName));


            String Total = "<font color=#FF8C00>Total  : </font> <font color=#000000>"+strTotalAmt+"</font>";
            holder.tvTotalAmt.setText(Html.fromHtml(Total));
            String Balance = "<font color=#FF8C00>Balance  : </font> <font color=#000000>"+strBalAmt+"</font>";
            holder.tvBalAmt.setText(Html.fromHtml(Balance));

        } catch (Exception e) {
            Log.e("onBindviewholder"," recylerView adpater  execption "+e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

}
