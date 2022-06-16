package com.example.contactlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.DetailActivity;
import com.example.contactlist.R;
import com.example.contactlist.modal.Contact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int TYPE_CONTACT = 1;
    private static int TYPE_HEAD_CONTACT = 2;

    private Context mContext;
    private List<Contact> mContactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        mContext = context;
        mContactList = contactList;
        notifyDataSetChanged(); // ...
    }

    // @SuppressLint("NotifyDataSetChanged")
//    public void setData(List<Contact> contactList) {
//        mContactList = contactList;
//        notifyDataSetChanged();     // ...
//    }

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
        Contact contact = mContactList.get(position); // thêm final nếu lỗi
        if (contact == null)
            return;

        if (holder.getItemViewType() == TYPE_CONTACT) {
            ContactViewHolder contactViewHolder;
            contactViewHolder = (ContactViewHolder) holder;
            contactViewHolder.imgBtn.setImageResource(contact.getResourceID()); // chưa đúng, sửa sau
            contactViewHolder.tv.setText(contact.getName());
            contactViewHolder.civ.setImageResource(contact.getResourceID());

            contactViewHolder.lItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDetailClick(contact);
                }
            });
        }
        else if (holder.getItemViewType() == TYPE_HEAD_CONTACT){
            ContactHeadViewHolder contactHeadViewHolder;
            contactHeadViewHolder = (ContactHeadViewHolder) holder;
            contactHeadViewHolder.imgBtnHead.setText(contact.getName().charAt(0) + "");
            contactHeadViewHolder.tvHead.setText(contact.getName());
            contactHeadViewHolder.civHead.setImageResource(contact.getResourceID());

            contactHeadViewHolder.lItemHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDetailClick(contact);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mContactList != null)
            return mContactList.size();
        return 0;
    }

    public void release() {
        mContext = null;
    }


    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lItem;
        private ImageButton imgBtn;
        private TextView tv;
        private CircleImageView civ;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            lItem = (LinearLayout) itemView.findViewById(R.id.layout_item_contact);
            imgBtn = itemView.findViewById(R.id.btn_first_letter);
            tv = itemView.findViewById(R.id.tv_name);
            civ = itemView.findViewById(R.id.civ_avt);
        }
    }

    public static class ContactHeadViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lItemHead;
        private TextView imgBtnHead;
        private TextView tvHead;
        private CircleImageView civHead;

        public ContactHeadViewHolder(@NonNull View itemView) {
            super(itemView);

            lItemHead = (LinearLayout) itemView.findViewById(R.id.layout_item_contact_head);
            imgBtnHead = itemView.findViewById(R.id.tv_first_letter_head);
            tvHead = itemView.findViewById(R.id.tv_name_head);
            civHead = itemView.findViewById(R.id.civ_avt_head);
        }
    }


    private void onDetailClick(Contact contact) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_contact", contact);

        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}