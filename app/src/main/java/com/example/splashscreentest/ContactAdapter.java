package com.example.splashscreentest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter {

    public ContactAdapter(Context context, ArrayList<Contact> resource) {
        super(context, R.layout.contact_row, resource);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View contactView = inflater.inflate(R.layout.contact_row, parent, false);

//        TODO: Make an array of contacts in the main activity
        Contact currentContact = (Contact) getItem(position);
        ImageView contactImage = (ImageView) contactView.findViewById(
                R.id.contact_row_image_view);
        TextView contactFirstName = (TextView) contactView.findViewById(
                R.id.contact_row_first_name_text_view);
        TextView contactLastName = (TextView) contactView.findViewById(
                R.id.contact_row_last_name_text_view);
//        Should I make this like a seperate object to handle all these internet requests...
//        contactImage.setImageResource(...)
        contactFirstName.setText(currentContact.getFirstName());
        contactLastName.setText(currentContact.getLastName());
        return contactView;
    }
}