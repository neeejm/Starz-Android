package me.neeejm.starz.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import me.neeejm.starz.beans.Star;
import me.neeejm.starz.dao.IDao;

public class StarService implements IDao <Star> {
    private final List<Star> stars;
    private final Context context;
    private static StarService instance;
    private final Gson gson;

    private final String URL = "http://192.168.1.102:8080/stars/";

    private StarService(Context context) {
        this.context = context;
        this.stars = new ArrayList<>();
        this.gson = new Gson();
    }

    public synchronized static StarService getInstance(Context context) {
        if(instance == null)
            instance = new StarService(context);
        return instance;
    }

    @Override
    public boolean create(Star o) {
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("nom", o.getNom());
            object.put("prenom", o.getPrenom());
            object.put("ville", o.getVille());
            object.put("sexe", o.isGender());
            object.put("imageURL", o.getImageURL());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new MyJsonRequest(Request.Method.POST, URL, object,
            response -> {
                Toast.makeText(context, "Star ajouté", Toast.LENGTH_LONG).show();
                stars.add(o);
            }
            , error -> {
                //Log.e("api", String.valueOf(error));
                Toast.makeText(context, "Star n'est pas ajouté", Toast.LENGTH_LONG).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
        return true;
    }

    @Override
    public boolean update(Star o) {
        for (Star s:
             stars) {
            if (s.getId() == o.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Star o) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.DELETE, URL + o.getId()+"", null,
                response -> {
                    stars.remove(o);
                }
                , error -> {
            Log.e("api", String.valueOf(error));
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
        return true;
    }

    @Override
    public Star findById(int id) {
        for(Star s : stars ){
            if(s.getId() == id)
                return s;
        }
        return null;
    }

    @Override
    public List<Star> findAll() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    Log.i("api", "findAll: " + response);
                    stars.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            stars.add(gson.fromJson(response.get(i).toString(), Star.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
                , error -> {
            Log.e("api", String.valueOf(error));
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

        return stars;
    }

    private static class MyJsonRequest extends JsonObjectRequest {
        public MyJsonRequest(String url, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
            super(url, listener, errorListener);
        }

        public MyJsonRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        // handle empty response object
        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                if (response.data.length == 0) {
                    byte[] responseData = "{}".getBytes("UTF8");
                    response = new NetworkResponse(response.statusCode, responseData,
                            response.notModified, response.networkTimeMs, response.allHeaders);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return super.parseNetworkResponse(response);
        }
    }
}
