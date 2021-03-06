package com.example.contactlist.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.Contact;
import com.example.contactlist.my_interface.IClickItemContactListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {
    private static final int TYPE_CONTACT = 1;
    private static final int TYPE_HEAD_CONTACT = 2;
    private static final int TYPE_SPECIAL_CONTACT = 3;
    private List<Contact> mContactList;
    private List<Contact> mContactListOld; // final
    private final IClickItemContactListener iClickItemContactListener;

    @SuppressLint("NotifyDataSetChanged")
    public ContactAdapter(List<Contact> contactList, IClickItemContactListener listener) {
        mContactList = contactList;
        mContactListOld = mContactList;
        notifyDataSetChanged(); // ...
        this.iClickItemContactListener = listener;
    }

    public void setList(List<Contact> contactList) {
        mContactList = contactList;
        mContactListOld = mContactList;
        notifyDataSetChanged(); // ...
    }

    @Override
    public int getItemViewType(int position) {
        Contact contact = mContactList.get(position);
        int type = contact.getType();
        if (type == 1)
            return TYPE_CONTACT;
        else if (type == 2)
            return TYPE_HEAD_CONTACT;
        else if (type == 3)
            return TYPE_SPECIAL_CONTACT;
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CONTACT) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        }
        else if (viewType == TYPE_HEAD_CONTACT) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contact_head, parent, false);
            return new ContactHeadViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_special, parent, false);
        return new ContactSpecialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        if (contact == null) return;

        ContactViewHolder viewHolder;
        if (contact.getType() == TYPE_CONTACT)
            viewHolder = (ContactViewHolder) holder;
        else if (contact.getType() == TYPE_HEAD_CONTACT)
            viewHolder = (ContactHeadViewHolder) holder;
        else
            viewHolder = (ContactSpecialViewHolder) holder;
        viewHolder.bind(contact);
    }

    @Override
    public int getItemCount() {
        if (mContactList == null) return 0;
        return mContactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout item;
        protected TextView name;
        protected CircleImageView avt;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.layout_item_contact);
            name = itemView.findViewById(R.id.tv_name);
            avt = itemView.findViewById(R.id.civ_avt);
        }

        public void bind(Contact contact) {
            name.setText(contact.getName());
            if (contact.getThumbnail() != null )
                avt.setImageURI(Uri.parse(contact.getThumbnail()));
            else
                avt.setImageResource(R.drawable.ic_person);
            item.setOnClickListener(view -> iClickItemContactListener.onDetailClick(contact));
        }
    }

    public class ContactHeadViewHolder extends ContactViewHolder {
        protected TextView letter;

        public ContactHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.tv_first_letter_head);
        }

        public void bind(Contact contact) {
            super.bind(contact);
            letter.setText(Character.toString(contact.getName().charAt(0)));
        }
    }

    public class ContactSpecialViewHolder extends ContactViewHolder {
        public ContactSpecialViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchStr = charSequence.toString();
                if (searchStr.isEmpty()) {
                    mContactList = mContactListOld;
                }
                else {
                    List<Contact> contacts = new ArrayList<>();
                    for (Contact contact : mContactListOld) {
                        if (contact.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                            contacts.add(contact);
                        }
                    }
                    mContactList = contacts;
                }
                
                FilterResults filterResults = new FilterResults();
                filterResults.values = mContactList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mContactList = (List<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}