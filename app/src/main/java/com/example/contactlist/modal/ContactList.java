package com.example.contactlist.modal;

import java.util.ArrayList;
import java.util.List;

public class ContactList {
    private final List<Contact> contacts;
    private int mSize = 0;

    public ContactList() {
        contacts = new ArrayList<>();
    }

    public void add(Contact contact) {
        for (int i = 0; i<contacts.size(); i++) {
            if (contact.compare(contacts.get(i)) < 0) {
                contacts.add(i, contact);
                mSize++;
                updateType(i);
                updateType(i+1);
                return;
            }
        }
        contacts.add(mSize++, contact);
        updateType(mSize -1);
    }

    public void remove (Contact contact) {
        for (int i = 0; i< mSize; i++) {
            if (contact.equal(contacts.get(i))) {
                contacts.remove(i);
                mSize--;
                if (i < mSize)
                    updateType(i);
                return;
            }
        }
    }

    public List<Contact> getList() {
        List<Contact> list = new ArrayList<>();
        for (Contact c : contacts)
            list.add(c.clone());
        return list;
    }

    private void updateType(int i) {
        Contact contact = contacts.get(i);
        if (contact.isFavorite()) {
            if (i==0) contact.setType(3);
            else contact.setType(1);
        }
        else {
            contact.setType(2);
            if (i>0){
                Contact prev_contact = contacts.get(i-1);
                if (contact.getName().toLowerCase().charAt(0) == prev_contact.getName()
                        .toLowerCase().charAt(0) && !prev_contact.isFavorite())
                    contact.setType(1);
            }
        }
    }
}
