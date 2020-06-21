package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.StaffAdapter;
import com.vinuthana.vinvidyaadmin.adapters.TchrNoteTitleSpinnerAdapter;
import com.vinuthana.vinvidyaadmin.adapters.StaffData;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;
import com.vinuthana.vinvidyaadmin.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class SetTeacherNoteFragment extends Fragment {

    Spinner spnrSetTchrNtTitle;
    EditText edtSetTchrNote, edtSetTchrNtDt, edtSetTchrNtDtTim, edtOtherNoteTitle;
    Button btnStaffList;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    String strSchoolId, strClassId, strClass, strStaffId, strNoteTitle, strStatus, strNoticeId;
    String strSetDate, strNote, strOtherNoteTitle,base64Note;
    private int year, month, day;
    private Session session;
    private ArrayList<StaffData> staffData;
    View view;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    String strDate, strTime;
    String strMinute = "";
    ListView lstSetTchrNt;
    JSONArray jsonArray;
    GetResponse response = new GetResponse();
    JSONObject outObject = new JSONObject();
    StaffAdapter adapter;
    JSONArray js_array = new JSONArray();
    ArrayList<String> staffNameList = new ArrayList<String>();
    CheckConnection connection = new CheckConnection();

    DatePickerDialog.OnDateSetListener mlistner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            edtSetTchrNtDt.setText(dayOfMonth + "-" + String.valueOf(month + 1) + "-" + year);
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
        //new GetTeacherNoteTitle().execute();
        view = inflater.inflate(R.layout.fragment_set_teacher_note, container, false);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getActivity(), mlistner, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        return view;
    }

    public void init() {
        session = new Session(getActivity());
        btnStaffList = getActivity().findViewById(R.id.btnStaffList);
        spnrSetTchrNtTitle = getActivity().findViewById(R.id.spnrSetTchrNtTitle);
        edtSetTchrNote = getActivity().findViewById(R.id.edtSetTchrNote);
        edtSetTchrNtDt = getActivity().findViewById(R.id.edtSetTchrNtDt);
        edtSetTchrNtDtTim = getActivity().findViewById(R.id.edtSetTchrNtDtTim);
        edtOtherNoteTitle = getActivity().findViewById(R.id.edtOtherNoteTitle);
        lstSetTchrNt = getActivity().findViewById(R.id.lstSetTchrNt);
        staffData = new ArrayList<StaffData>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            allEvents();

            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            strSetDate = df.format(c.getTime());
            new GetTeacherNoteTitle().execute();
            edtOtherNoteTitle.setVisibility(View.GONE);
        }
    }

    public void allEvents() {
        edtSetTchrNtDt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.edtSetTchrNtDt) {
                    datePickerDialog.show();
                    return true;
                }
                return false;
            }
        });

        edtSetTchrNtDtTim.setOnClickListener(new View.OnClickListener() {
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
                        edtSetTchrNtDtTim.setText(selectedHour + ":" + strMinute + AM_PM);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btnStaffList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOtherNoteTitle = edtOtherNoteTitle.getText().toString();
                strNote = edtSetTchrNote.getText().toString();
                base64Note=StringUtil.textToBase64(strNote);
                //strDate = edtSetTchrNtDt.getText().toString();
                strDate = edtSetTchrNtDt.getText().toString();
                strTime=  edtSetTchrNtDtTim.getText().toString();
                if (edtOtherNoteTitle.getVisibility() == View.VISIBLE) {
                    //Toast.makeText(getActivity(), "Other Title should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    if (edtSetTchrNtDt.getText().toString().equalsIgnoreCase("") || edtSetTchrNtDt.getText().toString().equalsIgnoreCase(null)) {
                        Toast.makeText(getActivity(), "Date cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetTchrNote.getText().toString().length() < 3 || edtSetTchrNote.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else if (edtOtherNoteTitle.getText().toString().length() < 3 || edtOtherNoteTitle.getText().toString().length() > 100) {
                        Toast.makeText(getActivity(), "Other Title should be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), TeacherNoteActivity.class);
                        String base64NoteTitle= StringUtil.textToBase64(strOtherNoteTitle);
                        Bundle bundle = new Bundle();
                        bundle.putString("NoteTitle", base64NoteTitle);
                        bundle.putString("NoteTitleId", strNoticeId);
                        bundle.putString("Note", base64Note);
                        bundle.putString("NoteOnDate", strDate);
                        bundle.putString("NoteOnTime",strTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else if (edtOtherNoteTitle.getVisibility() == View.GONE) {
                    if (edtSetTchrNtDt.getText().toString().equalsIgnoreCase("") || edtSetTchrNtDt.getText().toString().equalsIgnoreCase(null)) {
                        Toast.makeText(getActivity(), "Date cannot be blank", Toast.LENGTH_SHORT).show();
                    } else if (edtSetTchrNote.getText().toString().length() < 3 || edtSetTchrNote.getText().toString().length() > 1000) {
                        Toast.makeText(getActivity(), "Notice should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getActivity(), TeacherNoteActivity.class);
                        Bundle bundle = new Bundle();
                        String base64NoteTitle= StringUtil.textToBase64(strNoteTitle);
                        bundle.putString("NoteTitle", base64NoteTitle);
                        bundle.putString("NoteTitleId", strNoticeId);
                        bundle.putString("Note", base64Note);
                        bundle.putString("NoteOnDate", strDate);
                        bundle.putString("NoteOnTime",strTime);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }

                 /*else if (edtOtherNoteTitle.getText().toString().length() < 3 || edtOtherNoteTitle.getText().toString().length() > 1000) {
                    Toast.makeText(getActivity(), "Other Title should be between 3 and 1000 characters", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        spnrSetTchrNtTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tmpView = (TextView) spnrSetTchrNtTitle.getSelectedView().findViewById(R.id.list);
                tmpView.setTextColor(Color.WHITE);
                strNoticeId = adapterView.getItemAtPosition(i).toString();
                strNoteTitle = tmpView.getText().toString();
                if (strNoteTitle.equals("Other")) {
                    //Toast.makeText(getActivity(), "Selected Text is: " + strRmndrTitle, Toast.LENGTH_SHORT).show();
                    edtOtherNoteTitle.setVisibility(View.VISIBLE);
                } else {
                    //Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_SHORT).show();
                    edtOtherNoteTitle.setVisibility(View.GONE);
                }
                Log.e("Tag", "" + strNoteTitle);
                //Toast.makeText(getActivity(), strRmndrId + " " + strRmndrTitle, Toast.LENGTH_SHORT).show();
                //new GetStaffList().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    class GetTeacherNoteTitle extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "noticeBoardOperation.jsp";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Fetching Teacher Note Title...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getTeachersNoteIdBySchoolId));
                JSONObject noticeBoardData = new JSONObject();
                noticeBoardData.put(getString(R.string.key_SchoolId), strSchoolId);
                outObject.put(getString(R.string.key_noticeBoardData), noticeBoardData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            TchrNoteTitleSpinnerAdapter adapter = new TchrNoteTitleSpinnerAdapter(values[0], getActivity());
            spnrSetTchrNtTitle.setPrompt("Choose Section");
            spnrSetTchrNtTitle.setAdapter(adapter);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
