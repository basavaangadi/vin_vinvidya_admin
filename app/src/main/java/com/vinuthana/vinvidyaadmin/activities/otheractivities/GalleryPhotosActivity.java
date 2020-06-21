package com.vinuthana.vinvidyaadmin.activities.otheractivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.adapters.GalleryCatAdapter;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPhotosActivity extends AppCompatActivity {
    GridView gridImages;
    String strEventID, strStudentId,strStaffId,strAcademicYearId;
    ArrayList<GridItem> gridItems;
    private Session session;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.galleryDetalToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Details");
        if (!connection.netInfo(GalleryPhotosActivity.this)) {
            connection.buildDialog(GalleryPhotosActivity.this).show();
        } else {
            init();

            try {
                strEventID = getIntent().getExtras().getString("eventID");
                strStudentId = getIntent().getExtras().getString("staffId");
                HashMap<String, String> user = session.getUserDetails();

                strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);
                strAcademicYearId = user.get(Session.KEY_ACADEMIC_YEAR_ID);
                new GetGalleryDetails().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        gridImages = (GridView) findViewById(R.id.grid_images);
        gridItems = new ArrayList<GridItem>();
    }

    class GetGalleryDetails extends AsyncTask<String, JSONArray, Void> {
        String url = AD.url.base_url + "otherOperation.jsp";
        GetResponse response = new GetResponse();
        JSONObject outObject = new JSONObject();

        @Override
        protected Void doInBackground(String... params) {
            try {
                outObject.put(getString(R.string.key_OperationName), getString(R.string.web_getStaffGAlleryImagesByEventId));
                JSONObject userData = new JSONObject();
                //userData.put("studentId", studentId);
                userData.put(getString(R.string.key_eventId), strEventID);
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
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            JSONArray jArray;
            try {
                jArray = new JSONArray(String.valueOf(values[0]));
                if (jArray != null) {
                    GridViewAdapter adapter = new GridViewAdapter(GalleryPhotosActivity.this,jArray);
                    gridImages.setAdapter(adapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
