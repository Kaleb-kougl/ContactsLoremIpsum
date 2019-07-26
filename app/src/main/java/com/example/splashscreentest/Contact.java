package com.example.splashscreentest;

import android.os.Bundle;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Contact {
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String address;
    private SimpleDateFormat birthday;
    private boolean isMale;
    private String pictureUrlString;
    private final String TAG = "Something went wrong retrieving the picture";
    private Bundle contactBundle = new Bundle();

    public Bundle getBundle() {
        contactBundle.putString("firstName", getFirstName());
        contactBundle.putString("lastName", getLastName());
        contactBundle.putString("email", getEmail());
        contactBundle.putString("number", getNumber());
        contactBundle.putString("address", getAddress());
        contactBundle.putString("birthday", getBirthday());
        contactBundle.putBoolean("isMale", getIsMale());
        contactBundle.putString("pictureUrl", getPictureUrlString());
        return contactBundle;
    }


//    private final String JSON_PARSING_TAG = "Error occurred when parsing the contact JSON";

    public String getFirstName() {
        return StringFormatter.capitalizeWord(firstName);
    }

    public String getLastName() {
        return StringFormatter.capitalizeWord(lastName);
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return StringFormatter.capitalizeWord(address);
    }

    public String getBirthday() {
        final String BIRTHDAY_STRING = birthday.format(new Date());
        return BIRTHDAY_STRING;
    }

    public String getPictureUrlString() {
        return pictureUrlString;
    }

    public boolean getIsMale() {
        return isMale;
    }

//    public Drawable getPicture() {
//        retur
//    }

    public Contact(@NonNull JSONObject json) {
        parseJson(json);
    }

    /**
     * This method is going to take the JSON that you got back from the api and retrieve
     * just the data we want to present to the user
     *
     * @param json - This should be the json object you got back from the api
     */
    private void parseJson(JSONObject json) {
        try {
            JSONArray contactArray = json.getJSONArray("results");
            JSONObject contactObject = contactArray.getJSONObject(0);

            this.isMale = contactObject.getString("gender").equals("male");
            this.email = contactObject.getString("email");
            this.number = contactObject.getString("phone");

            JSONObject nameObject = contactObject.getJSONObject("name");
            this.firstName = nameObject.getString("first");
            this.lastName = nameObject.getString("last");

            JSONObject dobObject = contactObject.getJSONObject("dob");
            //To parse ISO 8601 Date format must use this patern and set timeZone to GMT
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            dateFormat.parse(dobObject.getString("date"));
            dateFormat.applyPattern("MM/dd/YYYY");
            this.birthday = dateFormat;

            JSONObject addressObject = contactObject.getJSONObject("location");
            this.address = addressObject.getString("street");

            JSONObject pictureObject = contactObject.getJSONObject("picture");
            this.pictureUrlString = pictureObject.getString("thumbnail");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
