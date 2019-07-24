package com.example.splashscreentest;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ContactDialog extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button save, cancel;

    public ContactDialog(Activity myActivity) {
        super(myActivity);
        this.c = myActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_dialog);
        save = (Button) findViewById(R.id.save_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
//                I think that this may because the finish of the main activty and then closing the app
//                c.finish();
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
}
