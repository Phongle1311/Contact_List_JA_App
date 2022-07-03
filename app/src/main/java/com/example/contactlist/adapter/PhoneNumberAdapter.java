package com.example.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.PhoneNumber;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {
    private List<PhoneNumber> mPhoneNumbers;

    public PhoneNumberAdapter(List<PhoneNumber> phoneNumbers) {
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
        holder.tvPhoneNumber.setText(phoneNumber.getPhoneNumber());
        holder.tvNumberType.setText(phoneNumber.getType());
    }

    @Override
    public int getItemCount() {
        if (mPhoneNumbers != null)
            return mPhoneNumbers.size();
        return 0;
    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPhoneNumber;
        private TextView tvNumberType;
        public PhoneNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvNumberType = itemView.findViewById(R.id.tv_number_type);
        }
    }
}
