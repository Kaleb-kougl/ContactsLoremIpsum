package com.example.splashscreentest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements ContactDialogFragment.OnSave {

    /**
     * This is used to log the error
     */
    private static final String TAG = "MainActivity";
    private JSONObject contactJSON;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private Bundle ContactBundle = new Bundle();
    private Contact currentContact;
    private ListView contactsListView;
    private ListAdapter contactsAdapter;
    private DataBaseHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DataBaseHelper(this, null, null, 1);

        this.contactsListView = (ListView) findViewById(R.id.contacts_list_view);
        this.contactsAdapter = new ContactAdapter(this, contactsList);


        //An event listener for the '+' btn
        Button add = (Button) findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView textView = (TextView) findViewById(R.id.first_text_view);

                //Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//              TODO: Put this in a resources file
                final String url = "https://randomuser.me/api/?nat=us";

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
                                currentContact = new Contact(contactJSON);
                                showNewContactDialog(currentContact.getBundle());

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
        if (dbHandler.hasContacts()) {
            getDbContacts();
        }
    }

    private void showNewContactDialog(Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        ContactDialogFragment contactDialogFragment = ContactDialogFragment.newInstance();
        contactDialogFragment.setArguments(bundle);
        contactDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onSave() {
        contactsList.add(currentContact);
        dbHandler.addContact(currentContact);
        updateListView();
    }

    private void hideDefaultText() {
        LinearLayout emptyData = (LinearLayout) findViewById(R.id.empty_text_view);
        emptyData.setVisibility(View.GONE);
    }

    interface SwipeEvents {
        void onSwipeLeft(boolean swiped, int position);
    }

    private void getDbContacts() {
//        ArrayList<Contact> checkme = (ArrayList<Contact>)dbHandler.databaseToArrayList();
        Contact[] dbContacts = dbHandler.databaseToArray();
        for (Contact currentContact : dbContacts) {
            contactsList.add(currentContact);
        }
        Collections.sort(contactsList);

        updateListView();
    }

    private void updateListView() {
        contactsListView.setVisibility(View.VISIBLE);
        contactsListView.setAdapter(contactsAdapter);
        final OnSwipeTouchListener swipeListener = new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "swipe: ", Toast.LENGTH_SHORT).show();
//                return true;
//                return true;
                System.out.println("swipe");
            }
        };
        contactsListView.setOnTouchListener(swipeListener);

        contactsListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                System.out.println("click");
                Bundle bundle = contactsList.get(position).getBundle();
                bundle.putBoolean("hideSave", true);
                showNewContactDialog(bundle);
            }
        });
        hideDefaultText();
//        System.out.println(dbHandler.databaseToArray());
    }
}
