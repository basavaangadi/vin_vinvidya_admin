package com.vinuthana.vinvidyaadmin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListViewItemViewHolder extends RecyclerView.ViewHolder {

    private CheckBox itemCheckbox;

    private TextView itemTextView, tvName;

    public ListViewItemViewHolder(View itemView) {
        super(itemView);
    }

    public CheckBox getItemCheckbox() {
        return itemCheckbox;
    }

    public void setItemCheckbox(CheckBox itemCheckbox) {
        this.itemCheckbox = itemCheckbox;
    }

    public TextView getItemTextView() {
        return itemTextView;
    }

    public void setItemTextView(TextView itemTextView) {
        this.itemTextView = itemTextView;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    //Basu wrote this code to manually check the listbox
    public Boolean uIschecked() {
        return itemCheckbox.isChecked();
    }

    //Basu wrote this code to manually select wheather the checkbox is selected listbox
    public void uSetChecked(Boolean state) {
        itemCheckbox.setChecked(state);
    }
}
