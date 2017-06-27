package com.adamzajdlik.firebasedemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adamzajdlik.firebasedemo.R;
import com.adamzajdlik.firebasedemo.content.Message;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by adamzajdlik on 2017-06-26.
 */

public class MessageListAdapter extends ArrayAdapter<Message> {


    public MessageListAdapter(@NonNull Context context, @LayoutRes int resource, List<Message> messages) {
        super(context, resource, messages);
    }

    @NonNull @Override public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView attachmentView = (ImageView) convertView.findViewById(R.id.iv_photo_message);
        TextView messageView = (TextView) convertView.findViewById(R.id.tv_message_preview);
        TextView authorView = (TextView) convertView.findViewById(R.id.tv_message_author);

        Message message = getItem(position);

        boolean hasAttachment = (message.getAttachmentUrl() != null);

        if (hasAttachment) {
            messageView.setVisibility(View.GONE);
            attachmentView.setVisibility(View.VISIBLE);
            Glide.with(attachmentView.getContext()).load(message.getAttachmentUrl()).into(attachmentView);
        } else {
            messageView.setVisibility(View.VISIBLE);
            attachmentView.setVisibility(View.GONE);
            messageView.setText(message.getContent());
        }
        authorView.setText(message.getAuthor());

        return convertView;
    }
}
