package com.example.contactlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.Mail;

import java.util.List;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder>{
    private Context mContext;
    private final List<Mail> mMails;

    public MailAdapter(Context context, List<Mail> mMails) {
        mContext = context;
        this.mMails = mMails;
        notifyDataSetChanged();
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
        holder.item.setOnClickListener(view ->sendEmail(mail.getMail()));
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

    public void release() {
        mContext = null;
    }

    private void sendEmail(String mailAddress) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailAddress);
//        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            mContext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, "There is no email client installed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
