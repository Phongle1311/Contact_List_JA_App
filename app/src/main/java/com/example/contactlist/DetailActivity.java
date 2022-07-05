package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    private static final int REQUEST_CALL_PHONE = 101;
    private static final int REQUEST_SEND_SMS = 102;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageButton btnBackDetail= findViewById(R.id.btn_back_detail);
        btnBackDetail.setOnClickListener(view -> onClickToMain(contact));

        Bundle bundle = getIntent().getExtras();
        if (bundle == null)
            return;

        contact = (Contact) bundle.get("object_contact");
        ImageView imgAvtDetail = findViewById(R.id.img_avt_detail);
        TextView tvNameDetail = findViewById(R.id.tv_name_detail);
        ImageButton btnImportantDetail = findViewById(R.id.btn_important_detail);

        RecyclerView rcvPhoneNumber = findViewById(R.id.rcv_phone_number);
        PhoneNumberAdapter phoneNumberAdapter = new PhoneNumberAdapter(getPhoneNumber(contact),
                this::requestPermissionAndMakeCall, this::requestPermissionAndSendSMS);
        rcvPhoneNumber.setLayoutManager(new LinearLayoutManager(this));
        rcvPhoneNumber.setAdapter(phoneNumberAdapter);

        RecyclerView rcvMail = findViewById(R.id.rcv_mail);
        MailAdapter mailAdapter = new MailAdapter(getMail(contact), this::sendEmail);
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

    private void requestPermissionAndMakeCall(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                makeCall(number);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
            }
        }
    }

    private void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    private void requestPermissionAndSendSMS(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSMS(number);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            }
        }
    }

    private void sendSMS(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        intent.putExtra("sms_body", "Enter your message");
        startActivity(intent);
    }

    private void sendEmail(String mailAddress) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailAddress);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE || requestCode == REQUEST_SEND_SMS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showSettingsDialog();

            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. " +
                "You can grant them later in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}