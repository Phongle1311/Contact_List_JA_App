package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contactlist.adapter.ContactAdapter;
import com.example.contactlist.module.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvContact;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvContact = findViewById(R.id.rcv_contact);
        contactAdapter = new ContactAdapter(this, getListContacts());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvContact.setLayoutManager(linearLayoutManager);

        //contactAdapter.setData(getListContacts());
        rcvContact.setAdapter(contactAdapter);
    }

    private List<Contact> getListContacts() {
        List<Contact> list = new ArrayList<>();

        Contact contact1 = new Contact("Lê Hoài Phong", R.drawable.ic_save_name,
                "0387671963", "0387671963",
                "hoaiphong13.11.2002@gmail.com", "20120545@student.hcmus.edu.vn", true);
        contact1.setType(2);
        list.add(contact1);

        Contact contact2 = new Contact("Contact 2", R.drawable.ic_save_name,
                "0387671963", "0387671963",
                "hoaiphong13.11.2002@gmail.com", "20120545@student.hcmus.edu.vn", true);
        contact2.setType(1);
        list.add(contact2);

        Contact contact3 = new Contact("Contact 3", R.drawable.ic_save_name,
                "0387671963", "0387671963",
                "hoaiphong13.11.2002@gmail.com", "20120545@student.hcmus.edu.vn", true);
        contact3.setType(1);
        list.add(contact3);

        Contact contact4 = new Contact("Contact 4", R.drawable.ic_save_name,
                "0387671963", "0387671963",
                "hoaiphong13.11.2002@gmail.com", "20120545@student.hcmus.edu.vn", true);
        contact4.setType(2);
        list.add(contact4);

        Contact contact5 = new Contact("Contact 5", R.drawable.ic_save_name,
                "0387671963", "0387671963",
                "hoaiphong13.11.2002@gmail.com", "20120545@student.hcmus.edu.vn", true);
        contact5.setType(1);
        list.add(contact5);

        return list;
    }
}