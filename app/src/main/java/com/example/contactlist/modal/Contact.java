package com.example.contactlist.modal;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Contact implements Serializable {
    private String Name;
    private String thumbnail;
    private String mobilePhoneNumber;
    private String workPhoneNumber;
    private String personMail;
    private String workMail;
    private Boolean isImportant;
    private int type;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mobilePhoneNumber == null) ? 0 : mobilePhoneNumber.hashCode());
        result = prime * result + ((personMail == null) ? 0 : personMail.hashCode());
        result = prime * result + ((Name == null) ? 0 : Name.hashCode());
        return result;
    }

    public Contact(String name) { this.Name = name; }

    public Contact(Contact contact) {
        Name = contact.Name;
        thumbnail = contact.thumbnail;
        mobilePhoneNumber = contact.mobilePhoneNumber;
        workPhoneNumber = contact.workPhoneNumber;
        personMail = contact.personMail;
        workMail = contact.workMail;
        isImportant = contact.isImportant;
        type = contact.type;
    }

    @NonNull
    public Contact clone() {
//        Contact contact = new Contact(Name);
//        contact.thumbnail = thumbnail;
//        contact.mobilePhoneNumber = mobilePhoneNumber;
//        contact.workPhoneNumber = workPhoneNumber;
//        contact.personMail = personMail;
//        contact.workMail = workMail;
//        contact.isImportant = isImportant;
//        contact.type = type;
//        return contact;
        return new Contact(this);
    }

    public String getName() { return Name; }

    public void setName(String name) { Name = name; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getMobilePhoneNumber() { return mobilePhoneNumber; }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getWorkPhoneNumber() { return workPhoneNumber; }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getPersonMail() { return personMail; }

    public void setPersonMail(String personMail) { this.personMail = personMail; }

    public String getWorkMail() { return workMail; }

    public void setWorkMail(String workMail) { this.workMail = workMail; }

    public Boolean getImportant() { return isImportant; }

    public void setImportant(Boolean important) { isImportant = important; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public int compare(Contact contact) {
        if (getImportant() && !contact.getImportant()) return -1;
        if (!getImportant() && contact.getImportant()) return 1;
        return getName().toLowerCase().compareTo(contact.getName().toLowerCase());
    }

    public boolean equal(Contact contact) { return this.hashCode() == contact.hashCode(); }
}