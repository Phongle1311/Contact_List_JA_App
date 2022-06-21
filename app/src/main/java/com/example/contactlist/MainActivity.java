package com.example.contactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.contactlist.adapter.CategoryAdapter;
import com.example.contactlist.adapter.ContactAdapter;
import com.example.contactlist.modal.Category;
import com.example.contactlist.modal.Contact;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvContact;
    private ContactAdapter contactAdapter;
    private List<Contact> mListContacts;
    private SearchView searchView;
    private Spinner spnCategory;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListContacts = new ArrayList<>();
        requestPermissions();

        spnCategory = findViewById(R.id.spn_category);
        categoryAdapter = new CategoryAdapter(this, R.layout.item_category_selected,
                getListCategory());
        spnCategory.setAdapter(categoryAdapter);

        rcvContact = findViewById(R.id.rcv_contact);
        contactAdapter = new ContactAdapter(this, mListContacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvContact.setLayoutManager(linearLayoutManager);
        rcvContact.setAdapter(contactAdapter);

        SearchView searchView = findViewById(R.id.searchView);
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
        list.add(new Category("Favourites"));
        return list;
    }

    // avoid memory leak
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (contactAdapter != null)
            contactAdapter.release();
    }

    private void getContacts() {
        List<Contact> favorites = new ArrayList<Contact>();
        String contactId = "";
        String displayName = "";
        String phoneThumb = "";
        int index;

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
//                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                index = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int hasPhoneNumber = Integer.parseInt(cursor.getString(index));
                if (hasPhoneNumber > 0) {
                    index = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    contactId = cursor.getString(index);
                    index = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    displayName = cursor.getString(index);

                    index = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
                    phoneThumb = cursor.getString(index);

                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},null);
                    if (phoneCursor.moveToNext()) {
                        index = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phoneCursor.getString(index);

                        Contact contact = new Contact(displayName);
                        contact.setMobilePhoneNumber(phoneNumber);
                        contact.setWorkPhoneNumber(phoneNumber);
                        contact.setPersonMail("mail@example.com");
                        contact.setWorkMail("mail@exaple.vn");
                        contact.setImportant(true);
                        if (phoneThumb != null)
                            contact.setThumbnail(phoneThumb);
                        contact.setType(1);
                        if (contact.getImportant()){
                            favorites.add(contact);
                            contact = new Contact(displayName);
                            contact.setMobilePhoneNumber(phoneNumber);
                            contact.setWorkPhoneNumber(phoneNumber);
                            contact.setPersonMail("mail@example.com");
                            contact.setWorkMail("mail@exaple.vn");
                            if (phoneThumb != null)
                                contact.setThumbnail(phoneThumb);
                            contact.setImportant(true);
                        }

                        contact.setType(1);
                        mListContacts.add(contact);
                    }
                    phoneCursor.close();
                }
            }
        }
        cursor.close();

        Collections.sort(mListContacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });


        for (int i = 0; i < mListContacts.size(); i++) {
            Contact contact = mListContacts.get(i);
            contact.setType(2);
            if (i != 0) {
                if (contact.getName().toLowerCase().charAt(0) == mListContacts.get(i-1)
                        .getName().toLowerCase().charAt(0)) {
                    contact.setType(1);
                }
            }
            mListContacts.set(i, contact);
        }

        if (favorites.size()>0) {
            Contact c = favorites.get(0);
            c.setType(3);
            favorites.set(0, c);
            favorites.addAll(mListContacts);
            mListContacts = favorites;
        }

        //loadingPB.setVisibility(View.GONE);
        //contactRVAdapter.notifyDataSetChanged();
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
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
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
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. " +
                "You can grant them later in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
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
//                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                        String num = "";
//                        if (Integer.valueOf(hasNumber) == 1) {
//                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//                            if (numbers.getCount() > 1) {
//                                int i = 0;
//                                String[] phoneNum = new String[numbers.getCount()];
//                                String[] type = new String[numbers.getCount()];
//                                String name = "";
//                                while (numbers.moveToNext()) {
//                                    name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                                    phoneNum[i] = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                    type[i] = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), numbers.getInt(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)), ""); // insert a type string in front of the number
//                                    i++;
//                                }
//                                makeDialogForMultipleNumbers(phoneNum, type, name);
//                            } else {
//                                while (numbers.moveToNext()) {
//                                    num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
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
