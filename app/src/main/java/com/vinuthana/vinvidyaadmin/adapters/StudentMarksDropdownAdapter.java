package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentMarksDropdownAdapter  extends BaseAdapter {
    private Context context;
    public static JSONArray jsonArray;
    ArrayList<String>strGradeList=new ArrayList<String>();
    ArrayList<String>strGradeValueList=new ArrayList<String>();
    //HashMap<String,String>strGradeValue=new HashMap<String,String>();
    int max;
    Float min;
    JSONArray gradeArray;
    String strSchoolId;
    SpinnerGradeDropdownAdapter dropdownItem;
    //public StudentMarksDropdownAdapter(Context context,JSONArray gradeArray) {
    public StudentMarksDropdownAdapter(Context context,ArrayList<String>strGradeList,ArrayList<String>strGradeValueList) {
        this.context = context;
     //   this.gradeArray=gradeArray;
        this.strGradeList=strGradeList;
        this.strGradeValueList=strGradeValueList;
        //getGradeValue();*/
    }
/*public void getGradeValue(){
        for(int i=0;i<strGradeValue.size();i++){
            String strGrade=strGradeValue.get(i);

        }
}*/
    public void setData(JSONArray jsonArray,int max,float min,String strSchoolId) {
        this.jsonArray = jsonArray;
        this.max = max;
        this.min = min;
        this.strSchoolId = strSchoolId;
        notifyDataSetChanged();
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.student_grade_adapter_layout, null);
            holder = new ViewHolder();
            holder.tview_rollno = (TextView) view.findViewById(R.id.tview_rollno);
            holder.tview_st_name = (TextView) view.findViewById(R.id.tview_st_name);
            holder.spnrGrades = (Spinner) view.findViewById(R.id.sp_grade);
            view.setTag(holder);
        } else {
            holder = new ViewHolder();
        }
        try {
            holder.tview_rollno.setText(jsonArray.getJSONObject(position).getString("RollNo"));
            holder.tview_st_name.setText(jsonArray.getJSONObject(position).getString("studentname"));
            /*for(int i=0;i<gradeArray.length();i++){
                JSONObject object=gradeArray.getJSONObject(i);
                String strGrade=object.getString("Grade");
                String strGradingValue=object.getString("GradingValue");
                strGradeList.add(strGrade);
                strGradeValueList.add(strGradingValue);
               // strGradeValue.put(String.valueOf(i),strGradingValue);
            }*/
            /*ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Grade");
            if (strSchoolId.equals("2")||strSchoolId.equals("13")) {
                arrayList.add("A+");
                arrayList.add("A");
                arrayList.add("B+");
                arrayList.add("B");
                arrayList.add("C");
                arrayList.add("AB");
            } else {
                arrayList.add("A+");
                arrayList.add("A");
                arrayList.add("B+");
                arrayList.add("B");
                arrayList.add("C+");
                arrayList.add("C");
                arrayList.add("AB");
            }*/


            final HashMap<Integer,String> hashMap = new HashMap<>();
           // hashMap.put(0,"");
            /*if (strSchoolId.equals("2")||strSchoolId.equals("13")) {
                hashMap.put(1,"-23232");
                hashMap.put(2,"-33333");
                hashMap.put(3,"-45454");
                hashMap.put(4,"-55555");
                hashMap.put(5,"-66666");
                hashMap.put(6,"-9999");

            } else {
                hashMap.put(1,"-23232");
                hashMap.put(2,"-33333");
                hashMap.put(3,"-45454");
                hashMap.put(4,"-44444");
                hashMap.put(5,"-67676");
                hashMap.put(6,"-66666");
                hashMap.put(6,"-9999");
            }*/

            dropdownItem = new SpinnerGradeDropdownAdapter(context,strGradeList);
            holder.spnrGrades.setAdapter(dropdownItem);
            holder.spnrGrades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                       // jsonArray.getJSONObject(position).put("marks", hashMap.get(i));
                        String strGrdVal=strGradeValueList.get(i);

                        jsonArray.getJSONObject(position).put("marks", strGradeValueList.get(i));
                        Toast.makeText(context,strGrdVal,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            Log.e("Error at  ","studentMarks Dropdown getView "+e.toString());
          //  e.printStackTrace();
        }


        return view;
    }

    class ViewHolder {
        TextView tview_rollno;
        TextView tview_st_name;
        Spinner spnrGrades;
    }
}

