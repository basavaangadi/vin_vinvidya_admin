package com.vinuthana.vinvidyaadmin.fragments.daytodayfragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.fragments.daytodayfragment.StaffTimeTableActivity;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.Session;

import java.util.HashMap;


public class MyTimeTableFragment extends Fragment {

    private Session session;
    View view;
    String strSchoolId, strClassId, strClass, strStaffId, strNoteTitle, strStatus, strNoticeId, strDate;
    ProgressDialog progressDialog;
    boolean isFragmentLoaded = false;
    CheckConnection connection = new CheckConnection();

    public void init() {
        session = new Session(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!connection.netInfo(getActivity())) {
            connection.buildDialog(getActivity()).show();
        } else {
            init();
            //allEvents();
            //new GetClass().execute();
            HashMap<String, String> user = session.getUserDetails();

            strSchoolId = user.get(Session.KEY_SCHOOL_ID);
            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                Intent intent = new Intent(getActivity(), StaffTimeTableActivity.class);
                intent.putExtra("StaffId", strStaffId);
                startActivity(intent);
                getActivity().finish();
                isFragmentLoaded = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_time_table, container, false);

        return view;
    }

}
