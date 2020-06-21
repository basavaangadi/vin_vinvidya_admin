package com.vinuthana.vinvidyaadmin.adapters.ExamAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinuthana.vinvidyaadmin.R;

public class MySpinnerArrayAdapter {/* extends ArrayAdapter<MyModel> {

    public MySpinnerArrayAdapter(Context context, List<MyModel> items) {
        super(context, R.layout.my_spinner_row, items);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        view.setText(R.string.select_one);
        int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller);
        view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.my_spinner_row, parent, false);

        position = position - 1; // Adjust for initial selection item
        MyModel item = getItem(position);

        // ... Resolve views & populate with data ...

        return row;
    }*/

}
