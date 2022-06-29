package com.example.contactlist.modal;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactList {
    private final List<Contact> contacts;
    private int mSizes = 0;

    public ContactList() {
        contacts = new ArrayList<>();
    }

    public void add(Contact contact) {
        for (int i = 0; i<contacts.size(); i++) {
            if (contact.compare(contacts.get(i)) < 0) {
                contacts.add(i, contact);
                mSizes++;
                updateType(i);
                return;
            }
        }
        contacts.add(mSizes++, contact);
        updateType(mSizes -1);
    }
    public boolean remove (Contact contact) {
        for (int i = 0; i<mSizes; i++) {
            if (contact.hashCode() == contacts.get(i).hashCode()) {
                contacts.remove(i);
//                if (i<mSizes) updateType(i+1);
                mSizes--;
                return true;
            }
        }
        return false;
    }
    public List<Contact> getList() {
        List<Contact> list = new ArrayList<>();
        for (Contact c : contacts)
            list.add(c.clone());
        return list;
    }
    public void sort() {
        Collections.sort(contacts, Contact::compare);
    }
    public int size() {
        return mSizes;
    }
    public Contact get(int i) {
        return contacts.get(i);
    }
    public void set(int i,Contact contact) {
        contacts.set(i, contact);
    }
    private void updateType(int i) {
        Contact contact = contacts.get(i);
        if (i == 0) {
            if (contact.getImportant()){
                contact.setType(3);
                if (mSizes>1) {
                    Contact next = contacts.get(1);
                    if (next.getType() == 3) {
                        next.setType(1);
                        contacts.set(1,next);
                    }
                }
            }
            else
                contact.setType(2);
        }
        else {
            contact.setType(1);

            if (!contact.getImportant() && contact.getName().toLowerCase().charAt(0) !=
                    contacts.get(i-1).getName().toLowerCase().charAt(0))
                contact.setType(2);
        }
    }
}
