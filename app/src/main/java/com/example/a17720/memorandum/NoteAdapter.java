package com.example.a17720.memorandum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * ListView的adapter
 * Created by 17720 on 2017/12/9.
 */

public class NoteAdapter extends ArrayAdapter<Data> {
    private int resourceId;

    public NoteAdapter(@NonNull Context context, int resource, @NonNull List<Data> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Data data = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.noteName);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvTitle.setText(data.getTitle());

        return view;
    }

    class ViewHolder{
        TextView tvTitle;
    }
}
