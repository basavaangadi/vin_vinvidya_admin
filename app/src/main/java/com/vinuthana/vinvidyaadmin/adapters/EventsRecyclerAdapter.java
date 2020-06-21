package com.vinuthana.vinvidyaadmin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.vinuthana.vinvidyaadmin.R;
import com.vinuthana.vinvidyaadmin.utils.AD;
import com.vinuthana.vinvidyaadmin.utils.GetResponse;
import com.vinuthana.vinvidyaadmin.utils.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Krish on 20-11-2017.
 */

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.MyViewHolder> {
    JSONArray eventsArray;

    String eventTitle, strEventDate, strEventDiscription, strEventTitle;
    ArrayList<String> eventIdList = new ArrayList<>();
    ArrayList<String> eventDescriptionList = new ArrayList<>();
    ArrayList<String> eventDateList = new ArrayList<>();
    ArrayList<String> eventTitleList = new ArrayList<>();
    String alrtMsg, strEventId;
    Activity activity;
    private Session session;
    String strStaffId;

    public EventsRecyclerAdapter(JSONArray eventsArray, Activity activity) {
        this.eventsArray = eventsArray;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.event_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsRecyclerAdapter.MyViewHolder holder, int position) {
        session = new Session(activity);
        HashMap<String, String> user = session.getUserDetails();

        //strStaffId = user.get(Session.KEY_STAFFDETAILS_ID);

        try {
            holder.tvEventsDate.setText(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_Day)));
            holder.txtEventsMonth.setText(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_Month)));
            holder.tvEventsTitle.setText(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_eventtitle)));
            eventIdList.add(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_eventId)).toString());
            eventDescriptionList.add(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_EventDescription)).toString());
            eventDateList.add(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_eventDate)).toString());
            eventTitleList.add(eventsArray.getJSONObject(position).getString(activity.getString(R.string.key_eventtitle)).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return eventsArray.length();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventsDate, txtEventsMonth, tvEventsTitle;
        CardView cardEvents;

        public MyViewHolder(final View itemView) {
            super(itemView);
            tvEventsDate = (TextView) itemView.findViewById(R.id.tvEventsDate);
            txtEventsMonth = (TextView) itemView.findViewById(R.id.txtEventsMonth);
            tvEventsTitle = (TextView) itemView.findViewById(R.id.tvEventsTitle);
            cardEvents = (CardView) itemView.findViewById(R.id.cardEvents);
            cardEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        strEventId = eventsArray.getJSONObject(getAdapterPosition()).getString(activity.getString(R.string.key_eventId)).toString();
                        eventIdList.add(eventsArray.getJSONObject(getAdapterPosition()).getString(activity.getString(R.string.key_eventId)).toString());
                        eventDescriptionList.add(eventsArray.getJSONObject(getAdapterPosition()).getString(activity.getString(R.string.key_EventDescription)).toString());
                        eventDateList.add(eventsArray.getJSONObject(getAdapterPosition()).getString(activity.getString(R.string.key_eventDate)).toString());
                        eventTitleList.add(eventsArray.getJSONObject(getAdapterPosition()).getString(activity.getString(R.string.key_eventtitle)).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new EventsDescription().execute();
                }
            });
        }
    }

    class EventsDescription extends AsyncTask<String, JSONArray, Void> {

        String url = AD.url.base_url + "otherOperation.jsp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                JSONObject outObject = new JSONObject();
                outObject.put(activity.getString(R.string.key_OperationName), activity.getString(R.string.web_getStaffEventDetails));
                JSONObject otherData = new JSONObject();
                otherData.put(activity.getString(R.string.key_eventId), strEventId);
                //otherData.put("eventId",strEventId);
                outObject.put(activity.getString(R.string.key_otherData), otherData);
                Log.e("TAG", "EventsDescription, doInBackground, otherData = " + outObject.toString());
                GetResponse response = new GetResponse();
                String respText = response.getServerResopnse(url, outObject.toString());
                Log.e("TAG", "EventsDescription, doInBackground, respText = " + respText);
                JSONObject inObject = new JSONObject(respText);
                JSONArray result = inObject.getJSONArray(activity.getString(R.string.key_Result));
                publishProgress(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(JSONArray... values) {
            super.onProgressUpdate(values);
            try {
                JSONArray jsonArray = new JSONArray(values[0].toString());
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String eventTitle = jsonObject.getString(activity.getString(R.string.key_eventtitle));
                String eventDescription = jsonObject.getString(activity.getString(R.string.key_EventDescription));
                String eventVenue = jsonObject.getString(activity.getString(R.string.key_EventVenue));
                String eventDate = jsonObject.getString(activity.getString(R.string.key_eventDate));

                AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyAlertDialogStyles);
                LinearLayout layout = new LinearLayout(activity);
                TextView tvEventTitle = new TextView(activity);
                TextView tvEventDescription = new TextView(activity);
                TextView tvEventVenue = new TextView(activity);
                TextView tvEventDate = new TextView(activity);
                TextView tvEventCreated = new TextView(activity);
                tvEventTitle.setText("Event: " + eventTitle);
                tvEventTitle.setTextColor(Color.WHITE);
                tvEventTitle.setPadding(0, 25, 0, 25);
                tvEventTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                tvEventDate.setText("Event Date: " + eventDate);
                tvEventDate.setTextColor(Color.WHITE);
                tvEventDate.setPadding(0, 25, 0, 25);
                tvEventDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                tvEventVenue.setText("Event Venue: " + eventVenue);
                tvEventVenue.setTextColor(Color.WHITE);
                tvEventVenue.setPadding(0, 25, 0, 25);
                tvEventVenue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                tvEventDescription.setText("Event Description: " + eventDescription);
                tvEventDescription.setTextColor(Color.WHITE);
                tvEventDescription.setPadding(0, 25, 0, 25);
                tvEventDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                layout.addView(tvEventTitle);
                layout.addView(tvEventDate);
                layout.addView(tvEventVenue);
                layout.addView(tvEventDescription);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 40);
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}