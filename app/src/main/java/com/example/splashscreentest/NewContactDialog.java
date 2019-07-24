package com.example.splashscreentest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Date;

public class NewContactDialog extends AppCompatDialogFragment {
    private boolean error = false;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Date birthday;

    NewContactDialog(boolean error) {
        this.error = true;
    }

    NewContactDialog(String firstName, String lastName, String email, String phoneNumber, String address, Date birthday){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthday = birthday;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        upon clicking the button nothing is going to happen
//                        hence this being empty
//                        so upon clicking it will just be dismissed
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Nothing is going to happen when you click this button
//                        at least for now.
                    }
                });
        return builder.create();
    }
}
