package com.example.splashscreentest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView firstNameTV;
        public TextView lastNameTV;

//        get the references

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
//            set references here
            firstNameTV = itemView.findViewById(R.id.contact_row_first_name_text_view);
            lastNameTV = itemView.findViewById(R.id.contact_row_last_name_text_view);
        }
    }

    private ArrayList<Contact> contactArrayList;

    public ContactsAdapter(ArrayList<Contact> contactArrayList) {
        this.contactArrayList = contactArrayList;
    }

    /**
     * This is where the views are passed
     *
     * @param parent
     * @param viewType
     * @return
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
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact currentContact = contactArrayList.get(position);

        System.out.println(holder.firstNameTV);
        System.out.println(holder.lastNameTV);
        holder.firstNameTV.setText(currentContact.getFirstName());
        holder.lastNameTV.setText(currentContact.getLastName());
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }
}
