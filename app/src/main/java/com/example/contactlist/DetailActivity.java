package com.example.contactlist;

import static java.util.ResourceBundle.getBundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        ImageView imgAvtDetail = findViewById(R.id.img_avt_detail);
        TextView tvNameDetail = findViewById(R.id.tv_name_detail);
        ImageButton btnImportantDetail = findViewById(R.id.btn_important_detail);
        TextView tvMobilePhoneDetail = findViewById(R.id.tv_mobile_phone_detail);
        TextView tvWorkPhoneDetail = findViewById(R.id.tv_work_phone_detail);
        TextView tvPersonalMailDetail = findViewById(R.id.tv_personal_mail_detail);
        TextView tvWorkMailDetail = findViewById(R.id.tv_work_mail_detail);


        imgAvtDetail.setImageResource(contact.getResourceID());
        tvNameDetail.setText(contact.getName());
        int whiteStarID = getResources().getIdentifier("white_circle_star" ,
                "drawable", getPackageName());
        int pinkStarID = getResources().getIdentifier("pink_circle_star" ,
                "drawable", getPackageName());
        if (contact.getImportant() == true)
            btnImportantDetail.setImageResource(pinkStarID);
        else
            btnImportantDetail.setImageResource(whiteStarID);
        tvMobilePhoneDetail.setText(contact.getMobilePhoneNumber());
        tvWorkPhoneDetail.setText(contact.getWorkPhoneNumber());
        tvPersonalMailDetail.setText(contact.getPersonMail());
        tvWorkMailDetail.setText(contact.getWorkMail());
    }
}