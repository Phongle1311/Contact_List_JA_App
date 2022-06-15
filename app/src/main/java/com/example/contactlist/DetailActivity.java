package com.example.contactlist;

import static java.util.ResourceBundle.getBundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.contactlist.module.Contact;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        Contact contact = (Contact) bundle.get("object_contact");
        TextView textView = findViewById(R.id.tv_name_detail);
        textView.setText(contact.getName());
    }
}