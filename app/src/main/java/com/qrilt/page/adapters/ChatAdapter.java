package com.qrilt.page.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qrilt.page.R;
import com.qrilt.page.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageViewHolder> {
    // Properties
    private Context context;
    private List<Message> messages;
    private Drawable darkBlueBackground, greyBackground;
    private int colorWhite, colorBlack, colorDarkBlue, colorGrey;

    // Constructors
    public ChatAdapter(Context context) {
        messages = new ArrayList<>();
        darkBlueBackground = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_darkblue_filled);
        greyBackground = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_grey_filled);
        colorWhite = ContextCompat.getColor(context, R.color.colorSecondary);
        colorBlack = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        colorDarkBlue = ContextCompat.getColor(context, R.color.colorPrimary);
        colorGrey = ContextCompat.getColor(context, R.color.colorSecondaryDark);
    }

    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        darkBlueBackground = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_darkblue_filled);
        greyBackground = ContextCompat.getDrawable(context, R.drawable.rounded_rectangle_grey_filled);
        colorWhite = ContextCompat.getColor(context, R.color.colorSecondary);
        colorBlack = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        colorDarkBlue = ContextCompat.getColor(context, R.color.colorPrimary);
        colorGrey = ContextCompat.getColor(context, R.color.colorSecondaryDark);
    }

    // Methods
    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_chat_message, parent, false);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.content);
        holder.timeTextView.setText(message.timeStamp.toString());

        if (position % 2 == 0) {
            holder.parentContainer.setGravity(Gravity.END);
            holder.messageContainer.setBackground(darkBlueBackground);
            holder.messageTextView.setTextColor(colorWhite);
            holder.timeTextView.setTextColor(colorGrey);
        } else {
            holder.parentContainer.setGravity(Gravity.START);
            holder.messageContainer.setBackground(greyBackground);
            holder.messageTextView.setTextColor(colorBlack);
            holder.timeTextView.setTextColor(colorDarkBlue);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // View Holder
    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        // Views
        public LinearLayout parentContainer, messageContainer;
        public TextView messageTextView, timeTextView;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            parentContainer = itemView.findViewById(R.id.view_holder_chat_message_parent_container);
            messageContainer = itemView.findViewById(R.id.view_holder_chat_message_container);
            messageTextView = itemView.findViewById(R.id.view_holder_chat_message_main_textview);
            timeTextView = itemView.findViewById(R.id.view_holder_chat_message_time_textview);
        }
    }
}
