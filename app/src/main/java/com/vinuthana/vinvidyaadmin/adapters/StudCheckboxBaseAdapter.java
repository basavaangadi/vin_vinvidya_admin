package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import java.util.List;

public class StudCheckboxBaseAdapter extends BaseAdapter {

    private List<StudentData> listViewItemDtoList = null;

    private Context ctx = null;

    public StudCheckboxBaseAdapter(Context ctx, List<StudentData> listViewItemDtoList) {
        this.ctx = ctx;
        this.listViewItemDtoList = listViewItemDtoList;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (listViewItemDtoList != null) {
            ret = listViewItemDtoList.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        Object ret = null;
        if (listViewItemDtoList != null) {
            ret = listViewItemDtoList.get(position);
        }
        return ret;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItemViewHolder viewHolder = null;

        if (convertView != null) {
            viewHolder = (ListViewItemViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(ctx, R.layout.unchecked_list, null);

            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.chkStatus);
            TextView listItemText = (TextView) convertView.findViewById(R.id.tvRollNo);
            TextView listItemName = (TextView) convertView.findViewById(R.id.tvStudName);

            viewHolder = new ListViewItemViewHolder(convertView);
            viewHolder.setItemCheckbox(listItemCheckbox);
            viewHolder.setItemTextView(listItemText);
            viewHolder.setTvName(listItemName);

            convertView.setTag(viewHolder);
        }

        StudentData listViewItemDto = listViewItemDtoList.get(position);
        viewHolder.getTvName().setText(listViewItemDto.getName());
        viewHolder.getItemTextView().setText(listViewItemDto.getRollNo());
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());

        return convertView;
    }
}
