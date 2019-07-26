package com.example.splashscreentest;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;


import java.util.EventListener;

public class ContactDialogFragment extends DialogFragment implements EventListener {
    private Button save, cancel;
    private String firstName, lastName, email, address, phoneNumber, birthday, pictureUrl;
    private Boolean isMale;

    /**
     * empty constructer b/c DialogFragment
     * Use 'newInstance' instead
     */
    public ContactDialogFragment() {
    }

    /**
     * @return ContactDialogFragment -
     */
    public static ContactDialogFragment newInstance() {
        ContactDialogFragment frag = new ContactDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    /**
     * Grabs data from bundle, inflates
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return - Returns the view that the user will see
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        this.firstName = bundle.getString("firstName");
        this.lastName = bundle.getString("lastName");
        this.email = bundle.getString("email");
        this.address = bundle.getString("address");
        this.phoneNumber = bundle.getString("number");
        this.birthday = bundle.getString("birthday");
        this.pictureUrl = bundle.getString("pictureUrl");
        return inflater.inflate(R.layout.contact_dialog, container);
    }

    /**
     * gets a dialogue without a title
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * Set the view to have the data from that bundle
     * Volley request the image to put in the dialogue
     * @param view - the view to put the data on
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
//        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        TextView firstName = (TextView) view.findViewById(R.id.first_name_text_view);
        firstName.setText(this.firstName);
        TextView lastName = (TextView) view.findViewById(R.id.last_name_text_view);
        lastName.setText(this.lastName);
        TextView email = (TextView) view.findViewById(R.id.email_text_view);
        email.setText(this.email);
        TextView phoneNumber = (TextView) view.findViewById(R.id.phone_number_text_view);
        phoneNumber.setText(this.phoneNumber);
        TextView address = (TextView) view.findViewById(R.id.address_text_view);
        address.setText(this.address);
        TextView birthday = (TextView) view.findViewById(R.id.birthday_text_view);
        birthday.setText(this.birthday);
        final ImageView contactPhotoView = (ImageView) view.findViewById(R.id.contact_photo_image_view);
        /**
         * This will make an image request via volley
         * Then it will add it to the request queue
         * On success it will add the image to the ImageView
         * On fail it does NOTHING.
         */
        ImageRequest request = new ImageRequest(this.pictureUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        contactPhotoView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        if (isMale) {
                            contactPhotoView.setImageResource(R.drawable.male);
                        } else {
                            contactPhotoView.setImageResource(R.drawable.female);
                        }
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    public interface NewContactDialogueListener{

    }

}
