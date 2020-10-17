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

public class StudwiseFeesRecyclerViewAdapter extends  RecyclerView.Adapter<StudwiseFeesRecyclerViewAdapter.MyViewHolder> {
    JSONArray jsonArray;
    Context mContext;
    String strFeesType;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate,tvChallanNum,tvStudName,tvPaid,tvTotalAmt,tvBalAmt;


       // androidx.cardview.widget.CardView CardView;


        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvChallanNum = view.findViewById(R.id.tvChallanNum);
            tvStudName = view.findViewById(R.id.tvStudName);
            tvPaid = view.findViewById(R.id.tvPaid);
            tvTotalAmt = view.findViewById(R.id.tvTotalAmt);
            tvBalAmt = view.findViewById(R.id.tvBalAmt);
           // CardView = view.findViewById(R.id.CardView);
            if(strFeesType.equalsIgnoreCase("Admission Fees")){
                tvBalAmt.setVisibility(View.VISIBLE);
            }
            else {
                tvBalAmt.setVisibility(View.INVISIBLE);
            }
            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvDate.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvChallanNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvChallanNum.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvStudName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvStudName.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvPaid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvPaid.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvBalAmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvBalAmt.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });
            tvTotalAmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(tvTotalAmt.getText());
                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
    public StudwiseFeesRecyclerViewAdapter(JSONArray jsonArray, Context context,String strFeesType)
    {
        this.jsonArray = jsonArray;
        this.mContext=context;
        this.strFeesType=strFeesType;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.studwise_feeslist,parent,false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            JSONObject object= jsonArray.getJSONObject(position);
            String strDate=object.getString("Date");
            String strChallanNum=object.getString("ReceiptNumber");
            //String Class=object.getString("ClassSection");
            //String RollNum=object.getString("RollNo");
            String strStudName=object.getString("StudentName");
            String strPaid=object.getString("Total");
            String strTotalAmt=object.getString("Total_amount");
            String strBalAmt=object.getString("Balance");
            Log.e("jsonObject is ","@ "+String.valueOf(position)+" "+object.toString() );
            String StudName = "<font color=#000000>"+strStudName+"</font>";
            holder.tvStudName.setText(Html.fromHtml(StudName));
            String Date = "<font color=#FF8C00>Date : </font> <font color=#000000>"+strDate+"</font>";
            holder.tvDate.setText(Html.fromHtml(Date));
            String RecieptNo = "<font color=#FF8C00>Reciept No : </font> <font color=#000000>"+strChallanNum+"</font>";
            holder.tvChallanNum.setText(Html.fromHtml(RecieptNo));
            String Paid = "<font color=#FF8C00>Paid  : </font> <font color=#000000>"+strPaid+"</font>";
            holder.tvPaid.setText(Html.fromHtml(Paid));
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
