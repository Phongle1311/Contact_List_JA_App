package com.example.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.modal.PhoneNumber;
import com.example.contactlist.my_interface.IClickMakeCall;
import com.example.contactlist.my_interface.IClickSendSMS;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {
    private final List<PhoneNumber> mPhoneNumbers;
    private final IClickMakeCall iClickMakeCall;
    private final IClickSendSMS iClickSendSMS;


    public PhoneNumberAdapter(List<PhoneNumber> phoneNumbers, IClickMakeCall listenerCall,
                              IClickSendSMS listenerSMS) {
        mPhoneNumbers = phoneNumbers;
        notifyDataSetChanged();
        iClickMakeCall = listenerCall;
        iClickSendSMS = listenerSMS;
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
        holder.item.setOnClickListener(view-> {
            iClickMakeCall.makeCall(phoneNumber.getPhoneNumber());
        });
        holder.tvPhoneNumber.setText(phoneNumber.getPhoneNumber());
        holder.tvNumberType.setText(phoneNumber.getType());
        holder.btnHangout.setOnClickListener(view ->
                iClickSendSMS.sendSMS(phoneNumber.getPhoneNumber()));

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
}
