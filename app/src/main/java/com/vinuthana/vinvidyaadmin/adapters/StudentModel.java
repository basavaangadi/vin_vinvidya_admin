package com.vinuthana.vinvidyaadmin.adapters;

/**
 * Created by anupamchugh on 29/05/17.
 */

public class StudentModel {
    //private static final long serialVersionUID = 1L;
    public String strName, strRollNo, strStudentId;
    boolean isChecked;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrRollNo() {
        return strRollNo;
    }

    public void setStrRollNo(String strRollNo) {
        this.strRollNo = strRollNo;
    }

    public String getStrStudentId() {
        return strStudentId;
    }

    public void setStrStudentId(String strStudentId) {
        this.strStudentId = strStudentId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
