package com.example.splashscreentest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity implements ContactDialogFragment.OnSave {

    /**
     * This is used to log the error
     */
    private static final String TAG = "MainActivity";
    private JSONObject contactJSON;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private Contact currentContact;
    private ListView contactsListView;
    private ContactAdapter contactsAdapter;
    private DataBaseHelper dbHandler;

    /**
     * VARS FOR RECYCLERVIEW
     */
    private RecyclerView contactsRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate now running");
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * CODE FOR LISTVIEW MIXED IN BELOW HERE
         */
        dbHandler = new DataBaseHelper(this, null, null, 1);
//
//        this.contactsListView = (ListView) findViewById(R.id.contacts_list_view);
        if (dbHandler.hasContacts()) {
            getDbContacts();
        }

        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
//        this says it will not change in size no matter how many items that are in the recycler view
//        optimizes performance
        contactsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContactsAdapter(contactsList);

//        pass everything to the recycler view
        contactsRecyclerView.setLayoutManager(layoutManager);
        contactsRecyclerView.setAdapter(adapter);


        /**
         * CODE FOR LISTVIEW ONLY (BELOW HERE)
         */
//        this.contactsAdapter = new ContactAdapter(this, contactsList);
//        contactsListView.setAdapter(contactsAdapter);
//
//
//        //An event listener for the '+' btn
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
                                Bundle bundle = currentContact.getBundle();
                                bundle.putBoolean("error", false);
                                showNewContactDialog(bundle);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: BAD HTTP REQUEST" + error.toString(), error);
//                        TODO: Put this a resources file
                        System.out.println("something went wrong" + error);
                        Bundle errorBundle = new Bundle();
                        errorBundle.putBoolean("error", true);
                        errorBundle.putBoolean("hideSave", true);
                        showNewContactDialog(errorBundle);
                    }
                });
                //Add the request to the RequestQueue
                queue.add(stringRequest);
            }
        });

    }

    private void showNewContactDialog(Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        ContactDialogFragment contactDialogFragment = ContactDialogFragment.newInstance();
        contactDialogFragment.setArguments(bundle);
        contactDialogFragment.show(fm, "fragment_contact");
    }

    @Override
    public void onSave() {
//        contactsList.add(currentContact);
        insert(currentContact);
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
        Contact[] dbContacts = dbHandler.databaseToArray();
        for (Contact currentContact : dbContacts) {
            contactsList.add(currentContact);
        }

        /**
         * CODE FOR LISTVIEW
         */
//        updateListView();
    }

    private void updateListView() {
        Collections.sort(contactsList);

        contactsListView.invalidateViews();

        contactsListView.setVisibility(View.VISIBLE);
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
                Bundle bundle = contactsList.get(position).getBundle();
                bundle.putBoolean("hideSave", true);
                showNewContactDialog(bundle);
            }
        });
        hideDefaultText();
    }

    private void insert(Contact newContact) {
        int pos = Collections.binarySearch(contactsList, newContact);
        if (pos < 0) {
            contactsList.add(-pos-1, newContact);
        }
    }

    /**
     * This creates our custom options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * Wont use this one because we want to filter in real-time
             */
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            /**
             * Will use this one to query in real-time
             * @param s
             * @return
             */
            @Override
            public boolean onQueryTextChange(String s) {
                contactsAdapter.getFilter().filter(s);
                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Nothing to do here
                return true; // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery("", true);
                searchView.clearFocus();
                return true; // Return true to collapse action view
            }
        });
        return true;
    }
}
