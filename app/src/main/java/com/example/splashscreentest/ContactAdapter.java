package com.example.splashscreentest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter implements Filterable {
    private ArrayList<Contact> contactsList;
    private ArrayList<Contact> contactsListFull;

    public ContactAdapter(Context context, ArrayList<Contact> resource) {
        super(context, R.layout.contact_row, resource);
        this.contactsList = resource;
//        to create separate object in memory
        this.contactsListFull = new ArrayList<>(resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View contactView = inflater.inflate(R.layout.contact_row, parent, false);

        Contact currentContact = (Contact) getItem(position);
        final ImageView contactImage = (ImageView) contactView.findViewById(
                R.id.contact_row_image_view);
        TextView contactFirstName = (TextView) contactView.findViewById(
                R.id.contact_row_first_name_text_view);
        TextView contactLastName = (TextView) contactView.findViewById(
                R.id.contact_row_last_name_text_view);
        contactFirstName.setText(currentContact.getFirstName());
        contactLastName.setText(currentContact.getLastName());
        if (currentContact.getIsMale()) {
            contactImage.setImageResource(R.drawable.male);
        } else {
            contactImage.setImageResource(R.drawable.female);
        }

        /**
         * This will make an image request via volley
         * Then it will add it to the request queue
         * On success it will add the image to the ImageView
         * On fail it does NOTHING.
         */
        ImageRequest request = new ImageRequest(currentContact.getPictureUrlString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        contactImage.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
        return contactView;
    }

    @Override
    public Filter getFilter() {
        return firstNameFilter;
    }

    /**
     * A filter to see if the contacts first name contains the users text
     */
    private Filter firstNameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                System.out.println(contactsListFull);
                filteredList.addAll(contactsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                System.out.println(filterPattern);

                for (Contact item : contactsListFull) {
                    if (item.getFirstName().toLowerCase().trim().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        /**
         * Publishes the results of the filter to the UI
         * @param charSequence
         * @param filterResults - the results of the filter
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            contactsList.clear();
            contactsList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };

}