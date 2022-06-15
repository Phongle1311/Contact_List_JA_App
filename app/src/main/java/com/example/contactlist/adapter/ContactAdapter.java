package com.example.contactlist.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.module.Contact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int TYPE_CONTACT = 1;
    private static int TYPE_HEAD_CONTACT = 2;

    private List<Contact> mContactList;

   // @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Contact> contactList) {
        mContactList = contactList;
        notifyDataSetChanged();     // ...
    }

    @Override
    public int getItemViewType(int position) {
        Contact contact = mContactList.get(position);
        int type = contact.getType();
        if (type == 1)
            return TYPE_CONTACT;
        else if (type == 2)
            return TYPE_HEAD_CONTACT;
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        if (contact == null)
            return;

        if (holder.getItemViewType() == TYPE_CONTACT) {
            ContactViewHolder contactViewHolder;
            contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.imgBtn.setImageResource(contact.getResourceID()); // chưa đúng, sửa sau
            contactViewHolder.tv.setText(contact.getName());
            contactViewHolder.civ.setImageResource(contact.getResourceID());
        }
        else if (holder.getItemViewType() == TYPE_HEAD_CONTACT){
            ContactHeadViewHolder contactHeadViewHolder;
            contactHeadViewHolder = (ContactHeadViewHolder) holder;
            contactHeadViewHolder.imgBtnHead.setImageResource(contact.getResourceID()); // chưa đúng, sửa sau
            contactHeadViewHolder.tvHead.setText(contact.getName());
            contactHeadViewHolder.civHead.setImageResource(contact.getResourceID());
        }
    }

    @Override
    public int getItemCount() {
        if (mContactList != null)
            return mContactList.size();
        return 0;
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imgBtn;
        private TextView tv;
        private CircleImageView civ;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBtn = itemView.findViewById(R.id.btn_first_letter);
            tv = itemView.findViewById(R.id.tv_name);
            civ = itemView.findViewById(R.id.civ_avt);
        }
    }

    public class ContactHeadViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imgBtnHead;
        private TextView tvHead;
        private CircleImageView civHead;

        public ContactHeadViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBtnHead = itemView.findViewById(R.id.btn_first_letter_head);
            tvHead = itemView.findViewById(R.id.tv_name_head);
            civHead = itemView.findViewById(R.id.civ_avt_head);
        }
    }
}
