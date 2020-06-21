package com.vinuthana.vinvidyaadmin.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

import java.util.List;

public class StaffCheckboxBaseAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    public SparseBooleanArray mCheckStates;
    private List<StaffData> listViewItemDtoList = null;

    private Context ctx = null;

    public StaffCheckboxBaseAdapter(Context ctx, List<StaffData> listViewItemDtoList) {
        this.ctx = ctx;
        this.listViewItemDtoList = listViewItemDtoList;
        mCheckStates = new SparseBooleanArray(listViewItemDtoList.size());
        for (int i = 0; i < listViewItemDtoList.size(); i++) {
            //default true
            mCheckStates.put(i, false);
        }
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
            convertView = View.inflate(ctx, R.layout.checked_list, null);

            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.chkStatus);
            TextView listItemText = (TextView) convertView.findViewById(R.id.tvRollNo);
            TextView listItemName = (TextView) convertView.findViewById(R.id.tvStudName);
            boolean ischecked=listItemCheckbox.isChecked();
            viewHolder = new ListViewItemViewHolder(convertView);
            viewHolder.setItemCheckbox(listItemCheckbox);
            viewHolder.setItemTextView(listItemText);
            viewHolder.setTvName(listItemName);
            viewHolder.uSetChecked(ischecked);

            convertView.setTag(viewHolder);
        }

        StaffData listViewItemDto = listViewItemDtoList.get(position);
        viewHolder.getTvName().setText(listViewItemDto.getName());
        viewHolder.getItemTextView().setText(listViewItemDto.getStaffId());
        viewHolder.getItemCheckbox().setChecked(listViewItemDto.isChecked());
        viewHolder.uIschecked();


        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCheckStates.put((Integer) buttonView.getTag(), isChecked);
    }

    public boolean isChecked(int position) {
        return mCheckStates.get(position, true);
    }
}
