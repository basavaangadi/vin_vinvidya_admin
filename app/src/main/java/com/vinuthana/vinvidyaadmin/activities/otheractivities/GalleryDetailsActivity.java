package com.vinuthana.vinvidyaadmin.activities.otheractivities;

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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.CheckConnection;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryDetailsActivity extends AppCompatActivity {
    GridView gridImages;
    String strEventID, strStudentId;
    ArrayList<GridItem> gridItems;
    CheckConnection connection = new CheckConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.galleryDetalToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Details");
        if (!connection.netInfo(GalleryDetailsActivity.this)) {
            connection.buildDialog(GalleryDetailsActivity.this).show();
        } else {
            init();

            try {
                strEventID = getIntent().getExtras().getString("eventID");
                strStudentId = getIntent().getExtras().getString("staffId");
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
                outObject.put(getString(R.string.key_otherData), userData);
                Log.e("Tag", "OutObject = " + outObject.toString());
                String responseText = response.getServerResopnse(url, outObject.toString());
                JSONObject inObject = new JSONObject(responseText);

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
                    GridItem item;
                    final HashMap<String,String> url_maps = new HashMap<String, String>();

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject post = jArray.optJSONObject(i);
                        String title = post.optString(getString(R.string.key_Description));

                        String Image = post.optString(getString(R.string.key_Image));
                        item = new GridItem();
                        item.setTitle(title +""+ i);
                        item.setImage(Image);
                        gridItems.add(item);
                        url_maps.put(title+""+ i, Image);
                    }

                    //GridViewAdapter adapter = new GridViewAdapter(GalleryDetailsActivity.this, R.layout.grid_item_layout, gridItems);
                    //gridImages.setAdapter(adapter);

                    gridImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(GalleryDetailsActivity.this, gridImages.toString(), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(GalleryDetailsActivity.this);

                            /*LayoutInflater inflater = getLayoutInflater();
                            View convertView = (View) inflater.inflate(R.layout.list_layout, null);*/
                            LayoutInflater inflater= LayoutInflater.from(GalleryDetailsActivity.this);
                            View customView=inflater.inflate(R.layout.custom_popup_image, null);
                            SliderLayout mDemoSlider = (SliderLayout)customView.findViewById(R.id.slider);

                            for(String name : url_maps.keySet()){
                                TextSliderView textSliderView = new TextSliderView(GalleryDetailsActivity.this);
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra",name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            mDemoSlider.setDuration(4000);
                            builder.setView(customView);
                            builder.setCancelable(true);
                            //builder.setTitle("Absentee list");
                            //builder.setMessage("Data not Found");

                            //builder.setMessage(strAbsntStudentNames);
                            //textView.setText(strAbsntStudentNames);
                            /*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new PutAttendanceActivity.InsertAttendance().execute();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Don't do anything
                                    js_array = new JSONArray();
                                    absenteeNameList.clear();
                                    absenteeRollList.clear();
                                }
                            });*/
                            builder.create().show();

                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
