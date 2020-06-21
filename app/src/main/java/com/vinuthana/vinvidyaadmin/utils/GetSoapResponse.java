package com.vinuthana.vinvidyaadmin.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class GetSoapResponse {
    public  static  final  String NAMESPACE= "http://tempuri.org/";
    SoapObject request= null;
    public String getSOAPStringResponse(String url, String methodName, List<PropertyInfo> param){
        String respText = "";
        try{
             request = new SoapObject(NAMESPACE, methodName);
            for (int i =0;i<param.size();i++){
                request.addProperty(param.get(i));

            }
            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            // Log.e("TAG, SOAP", "Envelop: "+envelope.);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
            androidHttpTransport.call(NAMESPACE+methodName, envelope);
            respText = envelope.getResponse().toString();
            Log.e("TAG","GetSoapResponse, getSOAPStringResponse, respText "+respText);
        }catch (Exception ee){

            Log.e("TAG","GetSoapResponse, getSOAPStringResponse, Exception "+ee.toString());
            Log.e("Request was, ",request.toString());

        }
        return respText;
    }
}
