package com.example.splashscreentest;

import android.os.Bundle;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Contact implements Comparable<Contact>{
    private String firstName;
    private String lastName;
    private String email;
    private String number;
    private String address;
    private SimpleDateFormat birthday;
    private String birthday_string;
    private boolean isMale;
    private String pictureUrlString;
    private Bundle contactBundle = new Bundle();

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
        return birthday_string;
    }

    public String getPictureUrlString() {
        return pictureUrlString;
    }

    public boolean getIsMale() {
        return isMale;
    }

    /**
     * Creates a bundle from Contact data
     * @return Bundle
     */
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

    /**
     * Constructor that accepts json
     * @param json
     */
    public Contact(@NonNull JSONObject json) {
        parseJson(json);
    }

    /**
     * Constructor that accepts every field of Contact object
     * @param firstName
     * @param lastName
     * @param email
     * @param number
     * @param address
     * @param birthday
     * @param pictureUrl
     * @param isMale
     */
    public Contact(@NonNull String firstName,
                   @NonNull String lastName,
                   @NonNull String email,
                   @NonNull String number,
                   @NonNull String address,
                   @NonNull String birthday,
                   @NonNull String pictureUrl,
                   @NonNull String isMale) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.address = address;
        this.birthday = new SimpleDateFormat(birthday);
        this.pictureUrlString = pictureUrl;
        this.isMale = isMale.equals("1");
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
            this.birthday_string = dateFormat.format(new Date());

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

    /**
     * Allows Contact objects to be compared with other Contact objects
     * Compares the firstName of Contact object
     * @param contact
     * @return
     */
    @Override
    public int compareTo(Contact contact) {
        return this.firstName.compareTo(contact.getFirstName());
    }
}
