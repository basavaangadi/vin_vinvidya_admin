package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.GalleryCatAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GalleryActivity extends AppCompatActivity {
    ListView list_main_gallery;
    String strEvenID, strStaffId, strAcademicYearId;
    private Session session;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.galleryToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gallery");
        if (!connection.netInfo(GalleryActivity.this)) {
            connection.buildDialog(GalleryActivity.this).show();
        } else {
            init();

            HashMap<String, String> user = session.getUserDetails();

            strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
            strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
            new GetGalleryCategory().execute();
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

    public void init() {
        session = new Session(getApplicationContext());
        list_main_gallery = (ListView) findViewById(R.id.list_main_gallery);
    }

    class GetGalleryCategory extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "otherOperation.jsp";
        GetResponse response = new GetResponse();
        JSONObject outObject = new JSONObject();

        @Override
        protected Void doInBackground(String... params) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getSatffGAlleryEventList));
                JSONObject userData = new JSONObject();
                userData.put(getString(R.string.key_StaffId), strStaffId);
                userData.put(getString(R.string.key_AcademicYearId), strAcademicYearId);
                outObject.put(getString(R.string.key_otherData), userData);
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);
                Log.e("Tag", "OutObject = " + outObject.toString());
                Log.e("Tag", "respText is = " + responseText);

                String strStatus = inObject.getString(getString(R.string.key_Status));
                if (strStatus.equalsIgnoreCase(getString(R.string.key_Success))) {
                    publishProgress(new JSONObject(responseText).getJSONArray(getString(R.string.key_Result)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final JSONArray... values) {
            super.onProgressUpdate(values);

            GalleryCatAdapter adapter = new GalleryCatAdapter(values[0], GalleryActivity.this);
            list_main_gallery.setAdapter(adapter);

            list_main_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //strImageCateID
                    JSONArray jArray;
                    try {
                        jArray = new JSONArray(String.valueOf(values[0]));
                        strEvenID = jArray.getJSONObject(position).getString("eventId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                Intent galleryDetailIntent = new Intent(GalleryActivity.this, GalleryPhotosActivity.class);
                    galleryDetailIntent.putExtra("eventID", strEvenID);
                    galleryDetailIntent.putExtra("staffId", strStaffId);
                startActivity(galleryDetailIntent);
            }
            });
        }
    }
}
