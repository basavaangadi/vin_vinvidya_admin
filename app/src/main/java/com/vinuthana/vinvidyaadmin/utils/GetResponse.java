package com.vinuthana.vinvidyaadmin.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.ContentValues.TAG;

/**
 * Created by basava on 5/8/17.
 */


public class GetResponse {
    public String getServerResopnse(String url, String JSONObjectBody) {
        String responseText = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            JSONObject jsonObject = new JSONObject(JSONObjectBody);
            StringEntity entity = new StringEntity(jsonObject.toString());
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);

            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                InputStream is = null;
                HttpEntity getResponseEntity = httpResponse.getEntity();

                is = getResponseEntity.getContent();
                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();

                String line;
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                responseText = sb.toString();
            }
            /// Log.e("TAG","GetHttpResponseUtility, getServerResopnse, responseText = "+responseText);
        } catch (Exception ee) {
            Log.e(TAG, ee.toString());
        }
        return responseText;
    }
}
