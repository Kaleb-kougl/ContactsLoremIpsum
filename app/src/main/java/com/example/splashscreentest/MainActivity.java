package com.example.splashscreentest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * This is used to log the error
     */
    private static final String TAG = "MainActivity";
    private JSONObject contactJSON;
    private ArrayList<Contact> contactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //An event listener for the '+' btn
        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView textView = (TextView) findViewById(R.id.first_text_view);

                //Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//              TODO: Put this in a resources file
                final String url = "https://randomuser.me/api/";

                //TODO: Search to see if there is a better way to format this section here
                //Request a string response from the provided url
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    contactJSON = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                openDialog(contactJSON);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: BAD HTTP REQUEST" + error.toString(), error);
//                        TODO: Put this a resources file
                        textView.setText("That didn't work!");
                    }
                });
                //Add the request to the RequestQueue
                queue.add(stringRequest);
            }
        });
    }

    /**
     * Should take text to put in the dialog upon opening
     * @param contactJSON - The JSON received from the API
     */
    public void openDialog(JSONObject contactJSON) {
        ContactDialog dialog = new ContactDialog(MainActivity.this, contactJSON);
        dialog.show();
    }
}
