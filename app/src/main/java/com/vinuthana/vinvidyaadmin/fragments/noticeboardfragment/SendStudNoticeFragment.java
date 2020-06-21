package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.NoticeTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffData;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.GetSoapResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class SendStudNoticeFragment extends Fragment {

    Button btnSetStudNt;
    EditText edtSetStudNtTim, edtSetStudNtDt, edtSetStudNotice, edtSetStudNtOthrTitle;
    Spinner spnrSetStudNtTitle, spnrSetStudNtWhomList;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    String strSchoolId, strClass, strStaffId, strMsg, strNotice, strStatus, strToWhom, strSetDate, responseText, strNoteTitle, strNoteTitleId, strOtherTitle;
    private int year, month, day;
    private Session session;
    private ArrayList<StaffData> staffData;
    View view;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDate, strTime, strRole;
    String strMinute = "";
    ListView lstSetTchrNt;
    JSONArray jsonArray;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    JSONObject inObject, noticeBoardData;
    ProgressDialog progressDialog;

    String strAcademicYearId,base64OtherTitle,base64Notice;
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetStudNtDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_send_stud_notice, container, false);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();
            new GetNoteTitle().execute();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());

            edtSetStudNtOthrTitle.setVisibility(View.GONE);

            String[] item = new String[]{"Select One", "All", "All Students"};
            final List<String> itemList = new ArrayList<>(Arrays.asList(item));

            final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, itemList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.WHITE);
                    } else {
                        tv.setTextColor(Color.WHITE);
                    }
                    return view;
                }
            };

            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            spnrSetStudNtWhomList.setAdapter(spinnerAdapter);
        }
    }

    private void init() {
        session = new Session(getActivity());
        btnSetStudNt = getActivity().findViewById(R.id.btnSetStudNt);
        spnrSetStudNtTitle = getActivity().findViewById(R.id.spnrSetStudNtTitle);
        spnrSetStudNtWhomList = getActivity().findViewById(R.id.spnrSetStudNtWhomList);
        edtSetStudNtTim = getActivity().findViewById(R.id.edtSetStudNtTim);
        edtSetStudNtDt = getActivity().findViewById(R.id.edtSetStudNtDt);
        edtSetStudNotice = getActivity().findViewById(R.id.edtSetStudNotice);
        edtSetStudNtOthrTitle = getActivity().findViewById(R.id.edtSetStudNtOthrTitle);
    }

    private void allEvents() {
        edtSetStudNtDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtSetStudNtDt) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetStudNtTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AM_PM = " AM";
                        if (selectedHour >= 12) {
                            AM_PM = " PM";
                            if (selectedHour >= 13 && selectedHour < 24) {
                                selectedHour -= 12;
                            } else {
                                selectedHour = 12;
                            }
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                        }
                        strMinute = String.valueOf(selectedMinute);
                        if (selectedMinute < 10) {
                            strMinute = "0" + strMinute;
                        }
                        edtSetStudNtTim.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        spnrSetStudNtWhomList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*strToWhom = spnrSetStudNtWhomList.getSelectedItem().toString();
                //strNoteTitle = adapterView.getItemAtPosition(i).toString();
                Log.e("Tag", "" + strToWhom);
                Toast.makeText(getActivity(), strToWhom, Toast.LENGTH_SHORT).show();*/
                strToWhom = (String) adapterView.getItemAtPosition(i);
                if (i > 0) {
                    //Toast.makeText(getActivity(), "Selected : " + strToWhom, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSetStudNtTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetStudNtTitle.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strNoteTitleId = adapterView.getItemAtPosition(i).toString();
                strNoteTitle = tmpView.getText().toString();
                Log.e("Tag", "" + strNoteTitle);
                if (strNoteTitle.equals("Other")) {
                    edtSetStudNtOthrTitle.setVisibility(View.VISIBLE);
                } else {
                    edtSetStudNtOthrTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSetStudNt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSetStudNtOthrTitle.getVisibility() == View.VISIBLE) {
                    if (edtSetStudNtOthrTitle.getText().toString().length() < 3 || edtSetStudNtOthrTitle.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Other Title should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudNotice.getText().toString().length() < 3 || edtSetStudNotice.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudNtDt.getText().toString().equalsIgnoreCase(null) || edtSetStudNtDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "select the notice  date", Toast.LENGTH_SHORT).show();
                    } /*else if (edtSetStudNtTim.getText().toString().equalsIgnoreCase(null) || edtSetStudNtTim.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "select the notice  time", Toast.LENGTH_SHORT).show();
                    }*/ else if (spnrSetStudNtWhomList.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "select any one of the option for whom the notice should be sent to ", Toast.LENGTH_SHORT).show();
                    } else {
                        strNotice = edtSetStudNotice.getText().toString();
                        strOtherTitle = edtSetStudNtOthrTitle.getText().toString();
                        base64OtherTitle= StringUtil.textToBase64(strOtherTitle);
                        base64Notice=StringUtil.textToBase64(strNotice);
                        Log.e("onBtnClick "," base 64 notice "+base64Notice+" base 64 otherTitle ");
                        strDate = edtSetStudNtDt.getText().toString();
                        strTime = edtSetStudNtTim.getText().toString();
                        new InsertNotice().execute();
                    }
                } else if (edtSetStudNtOthrTitle.getVisibility() == View.GONE) {
                    if (edtSetStudNotice.getText().toString().length() < 3 || edtSetStudNotice.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetStudNtDt.getText().toString().equalsIgnoreCase(null) || edtSetStudNtDt.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "select the notice  date", Toast.LENGTH_SHORT).show();
                    } /*else if (edtSetStudNtTim.getText().toString().equalsIgnoreCase(null) || edtSetStudNtTim.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "select the notice  time", Toast.LENGTH_SHORT).show();
                    }else if (edtSetStudNtTim.getText().toString().equalsIgnoreCase(null) || edtSetStudNtTim.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "select the notice  time", Toast.LENGTH_SHORT).show();
                    }*/ else if (spnrSetStudNtWhomList.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "select any one of the option for whom the notice should be sent to ", Toast.LENGTH_SHORT).show();
                    } else {
                        strNotice = edtSetStudNotice.getText().toString();
                        strOtherTitle = edtSetStudNtOthrTitle.getText().toString();
                        base64OtherTitle= StringUtil.textToBase64(strOtherTitle);
                        base64Notice=StringUtil.textToBase64(strNotice);
                        Log.e("onBtnClick "," base 64 notice "+base64Notice+" base 64 otherTitle ");

                        strDate = edtSetStudNtDt.getText().toString();
                        strTime = edtSetStudNtTim.getText().toString();
                        new InsertNotice().execute();
                    }

                }

            }
        });
    }

    class GetNoteTitle extends AsyncTask<String, JSONArray, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching the Note Title...");
            progressDialog.show();
        }

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected Void doInBackground(String... params) {

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getNoticeIdBySchoolId));
                noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                Log.e("Tag", "outObject =" + outObject.toString());
                responseText = response.getServerResopnse(url, outObject.toString());
                inObject = new JSONObject(responseText);

                Log.e("Tag", "responseText is =" + responseText);
                strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.custom, null);
                            builder.setView(convertView);
                            builder.setCancelable(true);
                            builder.setNegativeButton("Cancel", null);
                            builder.setTitle("Alert");
                            builder.setMessage("Data not Found");
                            builder.show();
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            NoticeTitleSpinnerAdapter adapter = new NoticeTitleSpinnerAdapter(values[0], getActivity());
            spnrSetStudNtTitle.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    class InsertNotice extends AsyncTask<String, JSONArray, Void> {

        String responseText1;
        String asmx_url=AD.url.asmx_url+"WebService_NoticeBoard.asmx?op=Insert_Notice";
        String methodName="Insert_Notice";
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setMessage("Please wait sending  notice...");
            pDialog.show();
        }


        @Override
        protected Void doInBackground(String... strings) {
            GetSoapResponse soapResponse = new GetSoapResponse();

            JSONArray requestArray= new JSONArray();
            List<PropertyInfo> param = new ArrayList<PropertyInfo>();
            PropertyInfo propertyInfo = new PropertyInfo();
            try{
                JSONObject reqObejct= new JSONObject();
                // reqObejct.put(getString(R.string.key_NoticeId),strNoteId);
                reqObejct.put(getString(R.string.key_SchoolId),strSchoolId);
                reqObejct.put(getString(R.string.key_AcademicYearId),strAcademicYearId);
                reqObejct.put(getString(R.string.key_SentBy),strStaffId);
                reqObejct.put(getString(R.string.key_NoticeCreatedDate),strSetDate);
                reqObejct.put(getString(R.string.key_ToWhom),strToWhom);

                reqObejct.put(getString(R.string.key_NoticeTitleId),strNoteTitleId);

                if(strNoteTitleId.equalsIgnoreCase("1")){
                    reqObejct.put(getString(R.string.key_OtherTitle),base64OtherTitle);
                    reqObejct.put(getString(R.string.key_NoteTitleName),base64OtherTitle);
                }else{
                    String base64NoticceTitle=StringUtil.textToBase64(strNoteTitle);
                    reqObejct.put(getString(R.string.key_NoteTitleName),base64NoticceTitle);
                }


                reqObejct.put(getString(R.string.key_Notice),base64Notice);
                reqObejct.put(getString(R.string.key_NoticeDate),strDate);
                if(strTime.length()<=1){
                    reqObejct.put(getString(R.string.key_NoticeTime),"9999");
                }else{
                    reqObejct.put(getString(R.string.key_NoticeTime),strTime);
                }
                Log.e("reqObject",reqObejct.toString());
                propertyInfo.setName("Insert_NoticeData");
                propertyInfo.setValue(reqObejct.toString());
                //schoolId.setType(Long.class);
                param.add(propertyInfo);

            final String resp = soapResponse.getSOAPStringResponse(asmx_url, methodName, param);
            JSONObject inObject= new JSONObject(resp);
             String strMessage= inObject.getString(getString(R.string.key_Message));
             String strStatus=inObject.getString(getString(R.string.key_Status));

             int resId=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));

            getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resId>0||resId==-1){
                            showAlert(strStatus,strMessage);
                        }else{
                            showAlert("Error","Something ");
                        }
                    }
            });

            }catch (Exception e){
                Log.e("studNoticeError","Exeception "+e.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            //Toast.makeText(getActivity(), responseText1, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
        }
    }

    private void showAlert( String alertTitle,String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(alertMessage);
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
