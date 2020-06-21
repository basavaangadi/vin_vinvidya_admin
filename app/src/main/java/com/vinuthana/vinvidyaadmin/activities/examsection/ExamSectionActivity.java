package com.vinuthana.vinvidyaadmin.activities.examsection;

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
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;

public class ExamSectionActivity extends AppCompatActivity {
    String[] title = {"Exam Schedule", "Exam Syllabus", "Exam Marks"};
    int[] images = {R.drawable.ic_calendar_multiple_check_black_36dp, R.drawable.ic_book_open_variant_black_36dp, R.drawable.ic_format_list_numbers_black_36dp};
    ListView examSectionListView;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.examSectiontoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.key_Exam_Section));
        examSectionListView = (ListView) findViewById(R.id.examSectionListView);
        ExamSectionAdapter adapter = new ExamSectionAdapter();
        examSectionListView.setAdapter(adapter);
        if (!connection.netInfo(ExamSectionActivity.this)) {
            connection.buildDialog(ExamSectionActivity.this).show();
        } else {
            examSectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            view.getContext().startActivity(new Intent(view.getContext(), ExamScheduleActivity.class));
                            break;
                        case 1:
                            view.getContext().startActivity(new Intent(view.getContext(), ExamSyllabusActivity.class));
                            break;
                        case 2:
                            view.getContext().startActivity(new Intent(view.getContext(), ExamMarksActivity.class));
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ExamSectionAdapter extends BaseAdapter {

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
