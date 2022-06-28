package com.example.contactlist.modal;

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
                return;
            }
        }
        contacts.add(contacts.size(), contact);
        mSizes++;
    }
    public boolean remove (Contact contact) {
        for (int i = 0; i<contacts.size(); i++) {
            if (contact.hashCode() == contacts.get(i).hashCode()) {
                contacts.remove(i);
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
}
