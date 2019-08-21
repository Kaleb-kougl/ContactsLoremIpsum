package com.example.splashscreentest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
     * Used to log the error
     */
    private static final String TAG = "MainActivity";
    private JSONObject contactJSON;
    private ArrayList<Contact> contactsList = new ArrayList<>();
    private Contact currentContact;
    private DataBaseHelper dbHandler;

    /**
     * VARS FOR RECYCLERVIEW
     */
    private RecyclerView contactsRecyclerView;
    private ContactsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //launch screen then switch to app screen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DataBaseHelper(this, null, null, 1);
        if (dbHandler.hasContacts()) {
            getDbContacts();
        }

        setUpRecyclerView();

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
                                Bundle bundle = currentContact.getBundle();
                                bundle.putBoolean("error", false);
                                showNewContactDialog(bundle);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: BAD HTTP REQUEST" + error.toString(), error);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact toDelete = adapter.getContact(viewHolder.getAdapterPosition());
                Toast.makeText(MainActivity.this, "Contact Deleted", Toast.LENGTH_SHORT).show();
                contactsList.remove(toDelete);
                dbHandler.deleteContact(toDelete);
                adapter.notifyDataSetChanged();
                checkListSize();
            }
        }).attachToRecyclerView(contactsRecyclerView);

    }

    private void setUpRecyclerView() {
        contactsRecyclerView = findViewById(R.id.contacts_recycler_view);
        contactsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ContactsAdapter(contactsList, new ContactsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                Bundle bundle = contact.getBundle();
                bundle.putBoolean("hideSave", true);
                showNewContactDialog(bundle);
            }
        });
        checkListSize();

//        pass everything to the recycler view
        contactsRecyclerView.setLayoutManager(layoutManager);
        contactsRecyclerView.setAdapter(adapter);
    }

    private void checkListSize(){
        if (adapter.getItemCount() == 0){
            findViewById(R.id.contacts_recycler_view).setVisibility(View.GONE);
        } else {
            findViewById(R.id.contacts_recycler_view).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Presents a dialog to the user with a new contact using bundle passed
     * @param bundle - A contact bundle
     */
    private void showNewContactDialog(Bundle bundle) {
        FragmentManager fm = getSupportFragmentManager();
        ContactDialogFragment contactDialogFragment = ContactDialogFragment.newInstance();
        contactDialogFragment.setArguments(bundle);
        contactDialogFragment.show(fm, "fragment_contact");
    }

    /**
     * Save the new contact to the ArrayList and DB
     */
    @Override
    public void onSave() {
        insert(currentContact);
        dbHandler.addContact(currentContact);
    }

    /**
     * Retrieve all of the contacts from the DB
     */
    private void getDbContacts() {
        Contact[] dbContacts = dbHandler.databaseToArray();
        for (Contact currentContact : dbContacts) {
            contactsList.add(currentContact);
        }
        Collections.sort(contactsList);

    }

    /**
     * Inserts a new contact into arrayList in Alphabetical order
     *
     * @param newContact - contact to be inserted
     */
    private void insert(Contact newContact) {
        int pos = Collections.binarySearch(contactsList, newContact);
        if (pos < 0) {
            contactsList.add(-pos - 1, newContact);
        }
        adapter.notifyDataSetChanged();
        checkListSize();
    }

    /**
     * options menu with search functionality
     *
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
                adapter.getFilter().filter(s);
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
