package com.example.contactlist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Spinner;

import com.example.contactlist.adapter.CategoryAdapter;
import com.example.contactlist.adapter.ContactAdapter;
import com.example.contactlist.modal.Category;
import com.example.contactlist.modal.Contact;

import com.example.contactlist.modal.ContactList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 100;
    private ContactAdapter contactAdapter;
    private ContactList mListContacts = new ContactList();
    private SearchView searchView;
    private RecyclerView rcvContact;

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

        rcvContact = findViewById(R.id.rcv_contact);
        requestPermissions();

        Spinner spnCategory = findViewById(R.id.spn_category);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,
                R.layout.item_category_selected, getListCategory());
        spnCategory.setAdapter(categoryAdapter);



        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (contactAdapter != null)
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

    private ContactList getContacts() {
        String contactId;
        String displayName;
        String phoneThumb;
        String phoneNumber;
        String phoneType;
        String mailAddress;
        String mailType;
        ContactList list = new ContactList();

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
                    }
                    phoneCursor.close();

                    Cursor mailCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId}, null);
                    while (mailCursor.moveToNext()) {
                        mailAddress = mailCursor.getString(mailCursor.getColumnIndexOrThrow
                                (ContactsContract.CommonDataKinds.Email.DATA));
                        mailType = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel
                                (this.getResources(), mailCursor.getInt(mailCursor
                                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds
                                                .Email.TYPE)), "");
                        contact.addMail(mailAddress, mailType);
                    }
                    mailCursor.close();

                    contact.setFavorite(false);
                    if (phoneThumb != null)
                        contact.setThumbnail(phoneThumb);
                    contact.setType(1);
                    list.add(contact);
                }
            }
        }
        cursor.close();
        return list;
    }

    public void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                new FetchContacts().execute();
            }
            else {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new FetchContacts().execute();
            } else {
//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                showSettingsDialog();
            }
        }
    }

//    public void requestPermissions() {
//        Dexter
//                .withActivity(this)
//                .withPermissions(
//                        Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.CALL_PHONE,
//                        Manifest.permission.SEND_SMS,
//                        Manifest.permission.WRITE_CONTACTS)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport
//                                                             multiplePermissionsReport) {
//                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
//                            Toast.makeText(MainActivity.this, "All the permissions" +
//                                    " are granted", Toast.LENGTH_SHORT).show();
//                            new FetchContacts().execute();
//                        }
//                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
//                            showSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown
//                            (List<PermissionRequest> list, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//                    }
//                })
//                .withErrorListener(error -> Toast.makeText(getApplicationContext(),
//                        "Error occurred! ", Toast.LENGTH_SHORT).show())
//                .onSameThread()
//                .check();
//    }

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

    private class FetchContacts extends  AsyncTask<Void, Void, ContactList> {
        @Override
        protected ContactList doInBackground(Void... voids) {
            return getContacts();
        }

        @Override
        protected void onPostExecute(ContactList contactList) {
            super.onPostExecute(contactList);
            mListContacts = contactList;
            contactAdapter = new ContactAdapter(mListContacts.getList(), this::onClickToDetailPage);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            rcvContact.setLayoutManager(linearLayoutManager);
            rcvContact.setAdapter(contactAdapter);
        }

        public void onClickToDetailPage(Contact contact) {
            mListContacts.remove(contact);

            Bundle bundle = new Bundle();
            bundle.putSerializable("object_contact", contact);

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtras(bundle);

            mActivityResultLauncher.launch(intent);
        }
    }
}