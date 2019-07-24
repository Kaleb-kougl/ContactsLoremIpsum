package com.example.splashscreentest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    /**
     * This is used to log the error
     */
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * An event listener for the '+' btn
         */
        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent numberIntent = new Intent(MainActivity.this,
//                        NumbersActivity.class);
//                startActivity(numberIntent);

                final TextView textView = (TextView) findViewById(R.id.first_text_view);
                textView.setText("working on the http request meow");

                /**
                 * Instantiate the RequestQueue.
                 */
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://randomuser.me/api/";

                /**
                 * Request a string reponse from the provided url
                 */
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject contactObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * Display the first 500 chars of the response string
                                 */
                                textView.setText("Response is: " + response.substring(0, 500));
                                openDialog();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: BAD HTTP REQUEST" + error.toString(), error);
                        textView.setText("That didn't work!");
                    }
                });
                /**
                 * Add the request to the RequestQueue
                 */
                queue.add(stringRequest);
            }
        });
    }

    /**
     * Should take text to put in the dialog upon opening
     */
    public void openDialog() {
//        NewContactDialog dialog = new NewContactDialog();
//        dialog.show(getSupportFragmentManager(), "This is my dialog tag");
        ContactDialog dialog = new ContactDialog(MainActivity.this);
        dialog.show();
    }
}
