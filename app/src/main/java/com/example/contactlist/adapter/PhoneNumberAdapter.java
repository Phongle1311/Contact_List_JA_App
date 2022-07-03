package com.example.contactlist.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.PhoneNumber;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {
    private Context mContext;
    private List<PhoneNumber> mPhoneNumbers;

    public PhoneNumberAdapter(Context context, List<PhoneNumber> phoneNumbers) {
        mContext = context;
        mPhoneNumbers = phoneNumbers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhoneNumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phone_number, parent, false);
        return new PhoneNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneNumberViewHolder holder, int position) {
        PhoneNumber phoneNumber = mPhoneNumbers.get(position);
        if (phoneNumber == null)
            return;
        holder.item.setOnClickListener(view ->makeCall(phoneNumber.getPhoneNumber()));
        holder.tvPhoneNumber.setText(phoneNumber.getPhoneNumber());
        holder.tvNumberType.setText(phoneNumber.getType());
        holder.btnHangout.setOnClickListener(view ->sendMessage(phoneNumber.getPhoneNumber()));
    }

    @Override
    public int getItemCount() {
        if (mPhoneNumbers != null)
            return mPhoneNumbers.size();
        return 0;
    }

    public static class PhoneNumberViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout item;
        private final TextView tvPhoneNumber;
        private final TextView tvNumberType;
        private final ImageButton btnHangout;

        public PhoneNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_phone_number);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvNumberType = itemView.findViewById(R.id.tv_number_type);
            btnHangout = itemView.findViewById(R.id.btn_hangouts);
        }
    }

    public void release() {
        mContext = null;
    }

    private void sendMessage(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
        intent.putExtra("sms_body", "Enter your message");
        mContext.startActivity(intent);
    }

    private void makeCall(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mContext.startActivity(callIntent);
    }
}
