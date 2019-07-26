package com.example.splashscreentest;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class ContactDialog extends Dialog implements View.OnClickListener {

    private Activity c;
    public Dialog d;
    private Button save, cancel;
    private final String TAG = "Error occurred when parsing the contact JSON";
    private Contact mContact;
//    private DialogListener listener;

    public ContactDialog(@NonNull Activity myActivity, @NonNull JSONObject contactJson) {
        super(myActivity);
        this.c = myActivity;
        this.mContact = new Contact(contactJson);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_dialog);
        formatContactDialog(mContact);
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
//                not sure if this is right
//                saveContact(mContact);
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

    private void changeTextView(int id, String newText) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(newText);
    }

    private void formatContactDialog(final Contact mContact) {
        String name = mContact.getFirstName();
        changeTextView(R.id.first_name_text_view, name);

        String lastName = mContact.getLastName();
        changeTextView(R.id.last_name_text_view, lastName);

        String email = mContact.getEmail();
        changeTextView(R.id.email_text_view, email);

        String phoneNumber = mContact.getNumber();
        changeTextView(R.id.phone_number_text_view, phoneNumber);

        String address = mContact.getAddress();
        changeTextView(R.id.address_text_view, address);

        String birthday = mContact.getBirthday();
        changeTextView(R.id.birthday_text_view, birthday);

        final ImageView contactPhotoView = (ImageView) findViewById(R.id.contact_photo_image_view);
        String photoUrl = mContact.getPictureUrlString();

        /**
         * This will make an image request via volley
         * Then it will add it to the request queue
         * On success it will add the image to the ImageView
         * On fail it does NOTHING.
         */
        ImageRequest request = new ImageRequest(photoUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        contactPhotoView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        if (mContact.getIsMale()) {
                            contactPhotoView.setImageResource(R.drawable.male);
                        } else {
                            contactPhotoView.setImageResource(R.drawable.female);
                        }
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }

//    Not sure if this is right. Things works for the AppCompatDialog fragment, but I have subclassed Dialog
//    public interface DialogListener {
//        void saveContact(Contact mContact);
//    }

}
