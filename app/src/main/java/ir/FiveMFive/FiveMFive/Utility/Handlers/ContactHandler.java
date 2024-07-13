package ir.FiveMFive.FiveMFive.Utility.Handlers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import ir.FiveMFive.FiveMFive.Java.Contact;
import ir.FiveMFive.FiveMFive.Utility.Checkers.PhoneNumberFormatChecker;

public class ContactHandler {
    private Context c;
    public ContactHandler(Context c) {
        this.c = c;
    }
    public ArrayList<Contact> readContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            Cursor phoneNumbers = c.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null);
            while (phoneNumbers.moveToNext()) {
                @SuppressLint("Range") String contactName = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String contactMobile = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                if(PhoneNumberFormatChecker.checkNumberFormat(contactMobile)) {
                    Contact contact = new Contact(contactName, contactMobile);
                    contacts.add(contact);
                }
            }
            phoneNumbers.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contacts;
    }

}
