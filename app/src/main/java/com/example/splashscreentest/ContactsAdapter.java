package com.example.splashscreentest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * THIS IS THE NEW ONE!!!
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> implements Filterable {

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        //Todo: Check if these can be private after further implementation
        public TextView firstNameTV;
        public TextView lastNameTV;
        public TextView emailTV;
        public ImageView contactIV;

//        get the references

        /**
         * Gets the references to the view elements
         * @param itemView - the item where the references will be pointing
         */
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
//            set references here
            firstNameTV = itemView.findViewById(R.id.contact_row_first_name_text_view);
            lastNameTV = itemView.findViewById(R.id.contact_row_last_name_text_view);
            emailTV = itemView.findViewById(R.id.contact_row_email_text_view);
            contactIV = itemView.findViewById(R.id.contact_row_image_view);
        }
    }

    private ArrayList<Contact> contactArrayList;
    private ArrayList<Contact> contactArrayListFull;

    /**
     * Constructor
     * store two separate arrayLists
     * @param contactArrayList - The ArrayList of contacts that should be made visible
     */
    public ContactsAdapter(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
        this.contactArrayListFull = new ArrayList<>(contactArrayList);
    }

    /**
     * This is where the views are passed
     *
     * @param parent - The parent ViewGroup
     * @param viewType - The type of view that is being created
     * @return - The view holder to be used with the RecylerView
     */
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        ContactViewHolder cvh = new ContactViewHolder(v);
        return cvh;
    }


    /**
     * Pass info from arrayList to views
     *
     * @param holder - The viewHolder
     * @param position - the position of the contact in the ArrayList
     */
    @Override
    public void onBindViewHolder(final @NonNull ContactViewHolder holder, int position) {
        Contact currentContact = contactArrayList.get(position);

        holder.firstNameTV.setText(currentContact.getFirstName());
        holder.lastNameTV.setText(currentContact.getLastName());
        holder.emailTV.setText(currentContact.getEmail());

        if (currentContact.getIsMale()) {
            holder.contactIV.setImageResource(R.drawable.male);
        } else {
            holder.contactIV.setImageResource(R.drawable.female);
        }
    }

    /**
     * Counts total contacts
     * @return int - count of the total contacts
     */
    @Override
    public int getItemCount() {
        return contactArrayList.size();
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
                filteredList.addAll(contactArrayListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact item : contactArrayListFull) {
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
         * @param filteredResults - the results of the filter
         */
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filteredResults) {
            contactArrayList.clear();
            contactArrayList.addAll((ArrayList<Contact>) filteredResults.values);
            notifyDataSetChanged();
        }
    };
}
