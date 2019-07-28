package com.example.splashscreentest;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String firstName, lastName, email, address, phoneNumber, birthday, pictureUrl;
    private Boolean isMale, hideSave, isError;
    private OnSave dataPasser;

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
     *
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
        this.hideSave = bundle.getBoolean("hideSave");
        this.isError = bundle.getBoolean("error");
        return inflater.inflate(R.layout.contact_dialog, container);
    }

    /**
     * gets a dialogue without a title
     *
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
     *
     * @param view               - the view to put the data on
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isError) {
            displayError(view, savedInstanceState);
        } else {
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
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(btnListener);

        Button saveBtn = (Button) view.findViewById(R.id.save_button);
        if (hideSave) {
            saveBtn.setVisibility(View.GONE);
            cancelBtn.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));
        } else {
            saveBtn.setOnClickListener(btnListener);
        }

    }

    private void displayError(View view, @Nullable Bundle savedInstanceState) {
        TextView firstName = (TextView) view.findViewById(R.id.first_name_text_view);
        firstName.setText("ERROR");
        TextView lastName = (TextView) view.findViewById(R.id.last_name_text_view);
        lastName.setVisibility(View.GONE);
//        LinearLayout textWrapper = (LinearLayout) view.findViewById(R.id.text_wrapper_layout);
//        textWrapper.setGravity(Gravity.CENTER);
        TextView email = (TextView) view.findViewById(R.id.email_text_view);
        email.setText("New contacts are currently not available.");
        TextView phoneNumber = (TextView) view.findViewById(R.id.phone_number_text_view);
        phoneNumber.setVisibility(View.GONE);
        TextView address = (TextView) view.findViewById(R.id.address_text_view);
        address.setVisibility(View.GONE);
        TextView birthday = (TextView) view.findViewById(R.id.birthday_text_view);
        birthday.setVisibility(View.GONE);
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_button:
//                    pass data back....
                    dataPasser.onSave();
                    dismiss();
                    break;
                case R.id.cancel_button:
                    dismiss();
                    break;
                default:
                    dismiss();
                    break;
            }
        }
    };

    public interface OnSave {
        void onSave();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnSave) context;
    }

}
