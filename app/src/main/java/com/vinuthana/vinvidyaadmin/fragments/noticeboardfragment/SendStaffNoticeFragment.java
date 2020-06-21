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
import com.vinuthana.vinvidyaadmin.activities.extraactivities.MainActivity;
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


public class SendStaffNoticeFragment extends Fragment {

    Button btnSetNote;
    EditText edtSetNoteTim, edtSetNoteDt, edtSetNote, edtSetNoteOthrTitle;
    Spinner spnrSetNoteTitle, spnrWhomList;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    String strSchoolId, strClass, strStaffId, strNoteTitle, strMsg, strStatus, strNotice;
    String strNoteId, strTime, strSetDate, strToWhom, responseText, strOtherTitle,base64Notice="",base64OtherTitle="";
    private int year, month, day;
    private Session session;
    private ArrayList<StaffData> staffData;
    View view;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDate, strAcademicYearId;
    String strMinute = "";
    ListView lstSetTchrNt;
    JSONArray jsonArray;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    JSONObject inObject, noticeBoardData;
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();
    ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetNoteDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
            datePickerDialog.dismiss();
        }
    };

    String[] item = new String[]{"All", "All Staff", "All Student"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_send_notice, container, false);
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
            session = new Session(getActivity());
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            new GetNoteTitle().execute();
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());

            edtSetNoteOthrTitle.setVisibility(View.GONE);

            String[] item = new String[]{"Select One", "All", "All Staff"};
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
            spnrWhomList.setAdapter(spinnerAdapter);
        }
    }

    private void init() {

        btnSetNote = getActivity().findViewById(R.id.btnSetNote);
        spnrSetNoteTitle = getActivity().findViewById(R.id.spnrSetNoteTitle);
        spnrWhomList = getActivity().findViewById(R.id.spnrWhomList);
        edtSetNoteTim = getActivity().findViewById(R.id.edtSetNoteTim);
        edtSetNoteDt = getActivity().findViewById(R.id.edtSetNoteDt);
        edtSetNote = getActivity().findViewById(R.id.edtSetNote);
        edtSetNoteOthrTitle = getActivity().findViewById(R.id.edtSetNoteOthrTitle);
    }

    private void allEvents() {
        edtSetNoteDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtSetNoteDt) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetNoteTim.setOnClickListener(new View.OnClickListener() {
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
                        edtSetNoteTim.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        spnrWhomList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strToWhom = (String) adapterView.getItemAtPosition(i);
                if (i > 0) {
                    //Toast.makeText(getActivity(), "Selected : " + strToWhom, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnrSetNoteTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetNoteTitle.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strNoteId = adapterView.getItemAtPosition(i).toString();
                strNoteTitle = tmpView.getText().toString();
                Log.e("Tag", "" + strNoteTitle);
                if (strNoteTitle.equals("Other")) {
                    edtSetNoteOthrTitle.setVisibility(View.VISIBLE);
                } else {
                    edtSetNoteOthrTitle.setVisibility(View.GONE);
                }

                //Toast.makeText(getActivity(), strNoteTitle, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSetNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSetNoteOthrTitle.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(getActivity(), "Other Title should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    if (edtSetNoteOthrTitle.getText().toString().length() < 3 || edtSetNoteOthrTitle.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Other Title should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetNote.getText().toString().length() < 3 || edtSetNote.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetNoteDt.getText().toString().equals(null) || edtSetNoteDt.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "select the notice  date", Toast.LENGTH_SHORT).show();
                    }/* else if (edtSetNoteTim.getText().toString().equals(null) || edtSetNoteTim.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "select the notice  time", Toast.LENGTH_SHORT).show();
                    }*/ else if (spnrWhomList.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "select any one of the option for whom the notice should be sent to ", Toast.LENGTH_SHORT).show();
                    } else {
                        strNotice = edtSetNote.getText().toString();
                        base64Notice= StringUtil.textToBase64(strNotice);
                        base64OtherTitle = edtSetNoteOthrTitle.getText().toString();
                        base64OtherTitle=StringUtil.textToBase64(base64OtherTitle);
                        strDate = edtSetNoteDt.getText().toString();
                        strTime = edtSetNoteTim.getText().toString();
                        new InsertNotice().execute();
                    }
                } else if (edtSetNoteOthrTitle.getVisibility() == View.GONE) {
                    if (edtSetNote.getText().toString().length() < 3 || edtSetNote.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtSetNoteDt.getText().toString().equals(null) || edtSetNoteDt.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "select the notice  date", Toast.LENGTH_SHORT).show();
                    } /*else if (edtSetNoteTim.getText().toString().equals(null) || edtSetNoteTim.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "select the notice  time", Toast.LENGTH_SHORT).show();
                    }*/ else if (spnrWhomList.getSelectedItemPosition() == 0) {
                        Toast.makeText(getActivity(), "select any one of the option for whom the notice should be sent to ", Toast.LENGTH_SHORT).show();
                    } else {
                        strNotice = edtSetNote.getText().toString();
                        strOtherTitle = edtSetNoteOthrTitle.getText().toString();
                        base64Notice= StringUtil.textToBase64(strNotice);
                        base64OtherTitle = edtSetNoteOthrTitle.getText().toString();
                        strDate = edtSetNoteDt.getText().toString();
                        strTime = edtSetNoteTim.getText().toString();
                        new InsertNotice().execute();
                    }
                }
            }
        });
    }

    class GetNoteTitle extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Please wait fetching Note title...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getNoticeIdBySchoolId));
                noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                responseText = response.getServerResopnse(url, outObject.toString());
                inObject = new JSONObject(responseText);
                Log.e("Tag", "outObject =" + outObject.toString());
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
            spnrSetNoteTitle.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
    class InsertNotice extends AsyncTask<String, JSONArray, Void> {

        String responseText1;
       // String url = AD.url.base_url + "noticeBoardOperation.jsp";
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

                reqObejct.put(getString(R.string.key_NoticeTitleId),strNoteId);

                if(strNoteId.equalsIgnoreCase("1")){
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
                int resid=Integer.parseInt(inObject.getString(getString(R.string.key_resId)));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resid>0||resid==-1){
                            showAlert(strStatus,strMessage);
                        }else{
                            showAlert("Error","Something went wrong");
                        }
                        //  Toast.makeText(getActivity(), resp, Toast.LENGTH_LONG).show();
                    }
                });
            }catch (Exception e){
                Log.e("Execption",e.toString());
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
