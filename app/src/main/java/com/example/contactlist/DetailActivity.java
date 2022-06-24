package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.contactlist.modal.Contact;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton btnBackDetail= findViewById(R.id.btn_back_detail);
        btnBackDetail.setOnClickListener(view -> onBackPressed());

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
        LinearLayout mobilePhone = findViewById(R.id.layout_mobile_phone_detail);
        LinearLayout workPhone = findViewById(R.id.layout_work_phone_detail);
//        LinearLayout personMail = findViewById(R.id.layout_personal_mail_detail);
//        LinearLayout workMail = findViewById(R.id.layout_work_mail_detail);

        ImageButton btnMobileMess = findViewById(R.id.btn_mobile_hangouts_detail);
        ImageButton btnWorkMess = findViewById(R.id.btn_work_hangouts_detail);


        if (contact.getThumbnail() != null)
            imgAvtDetail.setImageURI(Uri.parse(contact.getThumbnail()));
        else
            imgAvtDetail.setImageResource(R.drawable.ic_person);
        tvNameDetail.setText(contact.getName());

        int whiteStarID = getResources().getIdentifier("white_circle_star" ,
                "drawable", getPackageName());
        int pinkStarID = getResources().getIdentifier("pink_circle_star" ,
                "drawable", getPackageName());
        if (contact.getImportant())
            btnImportantDetail.setImageResource(pinkStarID);
        else
            btnImportantDetail.setImageResource(whiteStarID);

        tvMobilePhoneDetail.setText(contact.getMobilePhoneNumber());
        tvWorkPhoneDetail.setText(contact.getWorkPhoneNumber());
        tvPersonalMailDetail.setText(contact.getPersonMail());
        tvWorkMailDetail.setText(contact.getWorkMail());

        mobilePhone.setOnClickListener(view -> makeCall(contact.getMobilePhoneNumber()));

        workPhone.setOnClickListener(view -> makeCall(contact.getWorkPhoneNumber()));

        btnMobileMess.setOnClickListener(view -> sendMessage(contact.getMobilePhoneNumber()));

        btnWorkMess.setOnClickListener(view -> sendMessage(contact.getWorkPhoneNumber()));

        tvPersonalMailDetail.setOnClickListener(view -> sendMessage(contact.getPersonMail()));

        tvWorkMailDetail.setOnClickListener(view -> sendMessage(contact.getWorkMail()));
    }

    private void sendMessage(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        intent.putExtra("sms_body", "Enter your message");
        startActivity(intent);
    }

    private void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(DetailActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
}