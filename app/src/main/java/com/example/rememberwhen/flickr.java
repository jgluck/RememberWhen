package com.example.rememberwhen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import java.lang.Runnable.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kentwills on 5/13/14.
 */
public class flickr extends AsyncTask<String, String, String> {

    private HashMap<String, String> mData = null;// post data
    private ImageView content;
    private Activity activity;
    private static Bitmap flickrImage;

    public flickr(ImageView imv,HashMap<String, String> data, Activity act) {
        mData = data;
        content=imv;
        activity=act;
        lat = data.get("lat");
        lon = data.get("lon");
    }

    public static String api_key = "b79c787dc2f9f079cca1dbc2746e83c8";
    public static String auth_token = "72157644715092963-c0f4d9bc18234bed";
    public static String api_sig = "0e22971829a32eda73aaa08ab36384a8";
    public static String lat = "38.992130";
    public static String lon = "-76.942914";
    public static String radius = "1";
    public static String results_per_page="5";

    @Override
    protected String doInBackground(String... params) {
        if(lat==""||lon.equals("")) {
            JSONObject data = get_geo_photos();
            if (data == null)
                return "failed to get photo list";
            Log.w("flickr", data.toString());
            try {
                //getAllPhotos((JSONArray)((JSONObject) data.get("photos")).get("photo"));
                flickrImage = get_photo((JSONObject) ((JSONArray) ((JSONObject) data.get("photos")).get("photo")).get(1));
                //Log.w("flickr", data.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return ;
            return "success";
        }
        //return ;
        return "failed to get GPS coordinates";
    }



    //https://www.flickr.com/services/api/explore/flickr.photos.search
    public static JSONObject get_geo_photos() {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search"+
                "&api_key=" + api_key +
                "&lat=" + lat +
                "&lon=" + lon +
                "&radius=" + radius +
                "&per_page="+results_per_page+
                "&page=1"+
                "&format=json"+
                "&nojsoncallback=1"+
                "&auth_token="+auth_token+
                "&api_sig="+api_sig;
        Log.w("flickr",url);
        JSONObject json = null;
        try {
            json = readJsonFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //if(!((String)json.get("stat").toString()).equals("ok"))
        //    return null;
        //else
            return json;
    }

    public static void getAllPhotos(JSONArray metadata){
        for(int i=0;i<metadata.length();i++)
            //flickrImage = get_photo("4", "3206", "2929406868", "dc84ae77c0", "test.jpg");
            try {
                flickrImage = get_photo((JSONObject)metadata.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    //https://www.flickr.com/services/api/misc.urls.html
    public static Bitmap get_photo(JSONObject data) {
        String url = null;
        try {
            url = "http://farm" + data.get("farm") + ".staticflickr.com/"
                    + data.get("server") + "/" + data.get("id") + "_" + data.get("secret") + ".jpg";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ImageHandler.LoadPicFromURL(url);
    }

    //https://www.flickr.com/services/api/misc.urls.html
    public static Bitmap get_photo(String farmid, String serverid,String id,String secret,String local_save) {
        String url = "http://farm" + farmid + ".staticflickr.com/" + serverid + "/" + id + "_" + secret + ".jpg";
        return ImageHandler.LoadPicFromURL(url);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * on getting result
     */
    @Override
    protected void onPostExecute(String result) {
        content.setImageBitmap(flickrImage);
    }

}
