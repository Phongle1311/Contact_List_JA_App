package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlist.adapter.MailAdapter;
import com.example.contactlist.adapter.PhoneNumberAdapter;
import com.example.contactlist.modal.Contact;
import com.example.contactlist.modal.Mail;
import com.example.contactlist.modal.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Contact contact;
    private RecyclerView rcvPhoneNumber;
    private PhoneNumberAdapter phoneNumberAdapter;
    private RecyclerView rcvMail;
    private MailAdapter mailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton btnBackDetail= findViewById(R.id.btn_back_detail);
//        btnBackDetail.setOnClickListener(view -> onBackPressed());
        btnBackDetail.setOnClickListener(view -> onClickToMain(contact));

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        contact = (Contact) bundle.get("object_contact");
        ImageView imgAvtDetail = findViewById(R.id.img_avt_detail);
        TextView tvNameDetail = findViewById(R.id.tv_name_detail);
        ImageButton btnImportantDetail = findViewById(R.id.btn_important_detail);

        rcvPhoneNumber = findViewById(R.id.rcv_phone_number);
        phoneNumberAdapter = new PhoneNumberAdapter(getPhoneNumber(contact));
        rcvPhoneNumber.setLayoutManager(new LinearLayoutManager(this));
        rcvPhoneNumber.setAdapter(phoneNumberAdapter);

        rcvMail = findViewById(R.id.rcv_mail);
        mailAdapter = new MailAdapter(getMail(contact));
        rcvMail.setLayoutManager(new LinearLayoutManager(this));
        rcvMail.setAdapter(mailAdapter);

        if (contact.getThumbnail() != null)
            imgAvtDetail.setImageURI(Uri.parse(contact.getThumbnail()));
        else
            imgAvtDetail.setImageResource(R.drawable.ic_person);
        tvNameDetail.setText(contact.getName());

        if (contact.isFavorite())
            btnImportantDetail.setImageResource(R.drawable.pink_circle_star);
        else
            btnImportantDetail.setImageResource(R.drawable.white_circle_star);

        btnImportantDetail.setOnClickListener(view -> {
            contact.setFavorite(!contact.isFavorite());
            if (contact.isFavorite())
                btnImportantDetail.setImageResource(R.drawable.pink_circle_star);
            else
                btnImportantDetail.setImageResource(R.drawable.white_circle_star);
        });

//        mobilePhone.setOnClickListener(view -> makeCall(contact.getMobilePhoneNumber()));

//        workPhone.setOnClickListener(view -> makeCall(contact.getWorkPhoneNumber()));

//        btnMobileMess.setOnClickListener(view -> sendMessage(contact.getMobilePhoneNumber()));

//        btnWorkMess.setOnClickListener(view -> sendMessage(contact.getWorkPhoneNumber()));

//        tvPersonalMailDetail.setOnClickListener(view -> sendMessage(contact.getPersonMail()));

//        tvWorkMailDetail.setOnClickListener(view -> sendMessage(contact.getWorkMail()));
    }

    private List<Mail> getMail(Contact contact) {
        List<Mail> list = new ArrayList<>();
        List<String> mail = contact.getMails();
        List<String> type = contact.getMailTypes();
        int size = mail.size();
        for(int i = 0; i< size; i++)
            list.add(new Mail(mail.get(i), type.get(i)));
        return list;
    }

    private List<PhoneNumber> getPhoneNumber(Contact contact) {
        List<PhoneNumber> list = new ArrayList<>();
        List<String> phoneNumbers = contact.getPhoneNumbers();
        List<String> phoneTypes = contact.getPhoneTypes();
        int size = phoneNumbers.size();
        for (int i = 0; i < size; i++)
            list.add(new PhoneNumber(phoneNumbers.get(i), phoneTypes.get(i)));
        return list;
    }

    private void onClickToMain(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("result_contact", contact);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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