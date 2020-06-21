package com.vinuthana.vinvidyaadmin.activities.dayatoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;


public class DayToDayActivity extends AppCompatActivity {
    ListView daytodayListView;
    DayToDayAdapter dayToDayAdapter;
    Toolbar toolbar;
    String [] title = {"Attendance","Home Work","Home Work Feedback","Time Table"};
    int[] images = {R.drawable.ic_account_edit_black_48dp,R.drawable.home_work,R.drawable.ic_comment_outline_black_48dp,R.drawable.ic_calendar_clock_black_24dp};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_to_day);
        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Day To Day");
        dayToDayAdapter = new DayToDayAdapter();
        daytodayListView.setAdapter(dayToDayAdapter);
        allEvents();
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.dayToDaytoolbar);
        daytodayListView = (ListView) findViewById(R.id.daytodayListView);
    }

    public void allEvents() {
        daytodayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        view.getContext().startActivity(new Intent(view.getContext(),AttendanceActivity.class));
                        break;
                    case 1:
                        view.getContext().startActivity(new Intent(view.getContext(),HomeWorkActivity.class));
                        break;
                    case 2:
                        view.getContext().startActivity(new Intent(view.getContext(),HomeWorkFeedbackActivity.class));
                        break;
                    case 3:
                        view.getContext().startActivity(new Intent(view.getContext(),TimeTableActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class DayToDayAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.day_to_day, null);
            ImageView imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            TextView tvActivityTitle = (TextView) convertView.findViewById(R.id.tvActivityTitle);
            imageViewIcon.setImageResource(images[position]);
            tvActivityTitle.setText(title[position]);
            return convertView;
        }
    }
}
