package com.example.contactlist;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.contactlist.adapter.CategoryAdapter;
import com.example.contactlist.adapter.ContactAdapter;
import com.example.contactlist.modal.Category;
import com.example.contactlist.modal.Contact;

import com.example.contactlist.modal.ContactList;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactAdapter contactAdapter;
    private ContactList mListContacts;
    private SearchView searchView;

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null)
                            return;
                        Bundle bundle = intent.getExtras();
                        if (bundle == null)
                            return;
                        Contact contact = (Contact) bundle.get("result_contact");
                        mListContacts.add(contact);
                        contactAdapter.setList(mListContacts.getList());
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListContacts = new ContactList();
        requestPermissions();

        Spinner spnCategory = findViewById(R.id.spn_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,
                R.layout.item_category_selected, getListCategory());
        spnCategory.setAdapter(categoryAdapter);

        RecyclerView rcvContact = findViewById(R.id.rcv_contact);
        contactAdapter = new ContactAdapter(mListContacts.getList(), this::onClickToDetailPage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvContact.setLayoutManager(linearLayoutManager);
        rcvContact.setAdapter(contactAdapter);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchView.clearFocus();
//                contactAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private List<Category> getListCategory() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("All"));
        list.add(new Category("Favorites"));
        return list;
    }

    private void getContacts() {
        String contactId = "";
        String displayName = "";
        String phoneThumb = "";
        String phoneNumber = "";
        String phoneType = "";

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract
                            .Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract
                            .Contacts.DISPLAY_NAME));
                    phoneThumb = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract
                            .CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    Contact contact = new Contact(displayName);

                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId}, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow
                                (ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneType = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel
                                (this.getResources(), phoneCursor.getInt(phoneCursor
                                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds
                                .Phone.TYPE)), "");
                        contact.addPhoneNumber(phoneNumber, phoneType);
//                        Toast.makeText(this, displayName + phoneNumber + phoneType,Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(this, phoneNumber + phoneType,Toast.LENGTH_SHORT).show();
                    contact.setMobilePhoneNumber(phoneNumber);
                    contact.setWorkPhoneNumber(phoneNumber);
                    contact.setPersonMail("mail@example.com");
                    contact.setWorkMail("mail@exaple.vn");
                    contact.setFavorite(false);
                    if (phoneThumb != null)
                        contact.setThumbnail(phoneThumb);
                    contact.setType(1);
                    mListContacts.add(contact);
                    phoneCursor.close();
                }
            }
        }
        cursor.close();
    }


    public void requestPermissions() {
        Dexter
                .withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.WRITE_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport
                                                             multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            getContacts();
                            Toast.makeText(MainActivity.this, "All the permissions" +
                                    " are granted", Toast.LENGTH_SHORT).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown
                            (List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Toast.makeText(getApplicationContext(),
                        "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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

    // logic back-pressed when opening search view
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    public void onClickToDetailPage(Contact contact) {
        mListContacts.remove(contact);

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_contact", contact);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        mActivityResultLauncher.launch(intent);
    }
}

//    @Override
//    public void onActivityResult(int reqCode, int resultCode, Intent data) {
//        super.onActivityResult(reqCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (reqCode) {
//                case SELECT_CONTACT:
//                    Uri contactData = data.getData();
//                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
//                    if (c.moveToFirst()) {
//                        String contactId = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//                        String hasNumber = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                        String num = "";
//                        if (Integer.valueOf(hasNumber) == 1) {
//                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//                            if (numbers.getCount() > 1) {
//                                int i = 0;
//                                String[] phoneNum = new String[numbers.getCount()];
//                                String[] type = new String[numbers.getCount()];
//                                String name = "";
//                                while (numbers.moveToNext()) {
//                                    name = numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                                    phoneNum[i] = numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    type[i] = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), numbers.getInt(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE)), ""); // insert a type string in front of the number
//                                    i++;
//                                }
//                                makeDialogForMultipleNumbers(phoneNum, type, name);
//                            } else {
//                                while (numbers.moveToNext()) {
//                                    num = numbers.getString(numbers.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    if (num.contains("+")) {
//                                        etMobileNumber.setText(num.substring(3, num.length()).replaceAll("[^\\d.]", ""));
//                                    } else if (num.charAt(0) == 0) {
//                                        etMobileNumber.setText(num.substring(1, num.length()).replaceAll("[^\\d.]", ""));
//                                    } else if (num.toString().startsWith("0")) {
//                                        String mContact = num.substring(num.indexOf("0") + 1);
//                                        etMobileNumber.setText(mContact.replaceAll("[^\\d.]", ""));
//                                    } else {
//                                        etMobileNumber.setText(num.replaceAll("[^\\d.]", ""));
//                                    }
//                                    etMobileNumber.clearFocus();
//                                    etMobileNumber.setSelection(etMobileNumber.length());
//                                }
//                            }
//                        }
//                    }
//                    c.close();
//                    break;
//            }
//        }
//
//    }
