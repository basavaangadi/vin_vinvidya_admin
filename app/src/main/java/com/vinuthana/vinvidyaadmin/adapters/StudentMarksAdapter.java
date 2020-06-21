package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;

public class StudentMarksAdapter  extends BaseAdapter {
    private Context context;
    String str;
    public static JSONArray jsonArray;
    int max;
    float min;
    public StudentMarksAdapter(Context context) {
        this.context = context;
    }

    public void setData(JSONArray jsonArray,int max,float min) {
        this.jsonArray = jsonArray;
        this.max = max;
        this.min = min;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public int getViewTypeCount() {
        return jsonArray.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.student_marks_adapter_layout, parent,false);
            holder = new ViewHolder();
            holder.tview_rollno = (TextView) view.findViewById(R.id.tview_rollno);
            holder.tview_st_name = (TextView) view.findViewById(R.id.tview_st_name);
            holder.edt_marks = (EditText) view.findViewById(R.id.edt_marks);
            view.setTag(holder);
        } else {
            holder = new ViewHolder();
        }
        try {
            holder.tview_rollno.setText(jsonArray.getJSONObject(position).getString("RollNo"));
            holder.tview_st_name.setText(jsonArray.getJSONObject(position).getString("studentname"));
            String strBefore="",strChanged="",strFinal="";
            holder.edt_marks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    str = charSequence.toString();
                    if (!str.equals("")) {
                        boolean numeric = true;
                        Double num = 0.0;
                        try {
                            num = Double.parseDouble(str);
                        } catch (NumberFormatException e) {
                            numeric = false;
                        }
                        if (numeric) {
                            if (num>max) {
                                Toast.makeText(context, "Entered more than max", Toast.LENGTH_SHORT).show();
                                holder.edt_marks.setText("");
                            }
                            if (num<0) {
                                Toast.makeText(context, "Entered less than zero", Toast.LENGTH_SHORT).show();
                                holder.edt_marks.setText("");
                            }
                            String[] splitter = str.split("\\.");
                            if (splitter.length>1) {
                                int count = splitter[1].length();
                                if (count > 2) {
                                    Toast.makeText(context, "you entered wrong data", Toast.LENGTH_SHORT).show();
                                    holder.edt_marks.setText("");
                                }
                            }

                        }else if ((!numeric) && ((!str.equalsIgnoreCase("A")) &&(!str.equalsIgnoreCase("AB")))) {

                            Toast.makeText(context, "you entered wrong data", Toast.LENGTH_SHORT).show();
                            holder.edt_marks.setText("");
                        }
                    }
                    try {
                        if(str.equalsIgnoreCase("AB")) {
                            jsonArray.getJSONObject(position).put("marks", "-9999");
                        }/*else if(str.equalsIgnoreCase("A")){
                            Toast.makeText(context, "you entered wrong data", Toast.LENGTH_SHORT).show();
                            holder.edt_marks.setText("");
                        }*/ else {
                            jsonArray.getJSONObject(position).put("marks", str);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    class ViewHolder {
        TextView tview_rollno;
        TextView tview_st_name;
        EditText edt_marks;
    }
}

