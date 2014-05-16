package com.example.rememberwhen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.*;

import static android.support.v4.app.ActivityCompat.startActivity;


public class AsyncMailer extends AsyncTask<String, String, String> {
    private Bitmap picture = null;// post data
    private Activity activity;
    private HashMap<String, String> location = null;// post data
    private String filename;
    /**
     * constructor
     */
    public AsyncMailer(String f,HashMap<String, String> loc,Bitmap b, Activity act) {
        picture = b;
        activity = act;
        location = loc;
        filename = f;
    }

    /**
     * background
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            //myself74types@photos.flickr.com
            GmailSender sender = new GmailSender("glassUMD@gmail.com", "thisisthehcil");
            sender.sendMail("This is Subject",
                    "This is Body",
                    "glassUMD@gmail.com",
                    "myself74types@photos.flickr.com",picture, location,filename);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        //sendEmail(picture);
        return "success";
    }
    /**
     * on getting result
     */
    @Override
    protected void onPostExecute(String result) {
        activity.finish();

    }
}