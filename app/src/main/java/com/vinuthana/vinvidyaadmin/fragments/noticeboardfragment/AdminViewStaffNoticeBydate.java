package com.vinuthana.vinvidyaadmin.fragments.noticeboardfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinuthana.vinvidyaadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminViewStaffNoticeBydate extends Fragment {


    public AdminViewStaffNoticeBydate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_view_staff_notice_bydate, container, false);
    }

}
