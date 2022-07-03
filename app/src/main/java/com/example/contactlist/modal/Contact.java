package com.example.contactlist.modal;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable {
    private String Name;
    private String thumbnail;
    private List<String> phoneNumbers = new ArrayList<>();
    private List<String> phoneTypes = new ArrayList<>();
    private List<String> mails = new ArrayList<>();
    private List<String> mailTypes = new ArrayList<>();
    private Boolean favorite;
    private int type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
        result = prime * result + ((mails == null) ? 0 : mails.hashCode());
        result = prime * result + ((Name == null) ? 0 : Name.hashCode());
        return result;
    }

    public Contact(String name) { this.Name = name; }

    public Contact(Contact contact) {
        Name = contact.Name;
        thumbnail = contact.thumbnail;
        phoneNumbers = contact.phoneNumbers;
        phoneTypes = contact.phoneTypes;
        mails = contact.mails;
        mailTypes = contact.mailTypes;
        favorite = contact.favorite;
        type = contact.type;
    }

    @NonNull
    public Contact clone() { return new Contact(this); }

    public String getName() { return Name; }

    public void setName(String name) { Name = name; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public List<String> getPhoneNumbers() { return phoneNumbers; }

    public List<String> getPhoneTypes() { return phoneTypes; }

    public List<String> getMails() { return mails; }

    public List<String> getMailTypes() { return mailTypes; }

    public Boolean isFavorite() { return favorite; }

    public void setFavorite(Boolean important) { favorite = important; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public void addPhoneNumber(String number, String type) {
        phoneNumbers.add(number);
        phoneTypes.add(type);
    }

    public void addMail(String mail, String type) {
        mails.add(mail);
        mailTypes.add(type);
    }

    public int compare(Contact contact) {
        if (isFavorite() && !contact.isFavorite()) return -1;
        if (!isFavorite() && contact.isFavorite()) return 1;
        return getName().toLowerCase().compareTo(contact.getName().toLowerCase());
    }

    public boolean equal(Contact contact) { return this.hashCode() == contact.hashCode(); }
}