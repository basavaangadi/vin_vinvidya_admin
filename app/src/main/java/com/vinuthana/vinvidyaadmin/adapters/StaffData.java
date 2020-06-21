package com.vinuthana.vinvidyaadmin.adapters;

/**
 * Created by Lenovo on 2/21/2018.
 */

public class StaffData {

    private String staffId;
    private String name;
    private boolean isChecked;
    private String staffDetailsId;

    public String getStaffDetailsId() {
        return staffDetailsId;
    }

    public void setStaffDetailsId(String staffDetailsId) {
        this.staffDetailsId = staffDetailsId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
