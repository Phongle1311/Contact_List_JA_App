package com.example.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.Mail;
import com.example.contactlist.my_interface.IClickSendEmail;

import java.util.List;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder>{
    private final IClickSendEmail iClickSendEmail;
    private final List<Mail> mMails;

    public MailAdapter(List<Mail> mMails, IClickSendEmail listener) {
        this.mMails = mMails;
        notifyDataSetChanged();
        iClickSendEmail = listener;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mail, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MailViewHolder holder, int position) {
        Mail mail = mMails.get(position);
        if (mail == null)
            return;
        holder.item.setOnClickListener(view ->iClickSendEmail.sendEmail(mail.getMail()));
        holder.tvMail.setText(mail.getMail());
        holder.tvMailType.setText(mail.getType());
    }

    @Override
    public int getItemCount() {
        if (mMails != null) return mMails.size();
        return 0;
    }

    public static class MailViewHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        TextView tvMail;
        TextView tvMailType;
        public MailViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_mail);
            tvMail = itemView.findViewById(R.id.tv_mail);
            tvMailType = itemView.findViewById(R.id.tv_mail_type);
        }
    }


}
