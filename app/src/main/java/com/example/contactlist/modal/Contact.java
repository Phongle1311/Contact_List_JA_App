package com.example.contactlist.modal;

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
}