package com.example.mad_practice_6;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_PERMISSION = 1;
    ListView contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_READ_PERMISSION: {
                if (!hasPermission()) {
                    Toast.makeText(getApplicationContext(),
                            "Не получено разрешения для вывода списка контактов!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                contactList = findViewById(R.id.contactList);
                ArrayList<String> contacts = (ArrayList<String>) getContacts();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, contacts);
                contactList.setAdapter(adapter);
            }
        }
    }

    private void askPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasPermission()) {
                this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_ID_READ_PERMISSION);
            }
        }
    }

    private boolean hasPermission() {
        int readPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        return readPermission == PackageManager.PERMISSION_GRANTED;
    }

    //метод для получения контактов через Contact API
    private Iterable<String> getContacts() {
        ArrayList<String> contacts = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
                String contact = cursor.getString(id);
                contacts.add(contact);
            }
            cursor.close();
        }
        return contacts;
    }
}