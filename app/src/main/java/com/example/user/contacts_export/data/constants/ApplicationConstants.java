package com.example.user.contacts_export.data.constants;

import android.os.Environment;

/**
 * Created by chandradasdipok on 5/31/2016.
 */
public class ApplicationConstants {

    public static final String externalStorageFolder = Environment.getExternalStorageDirectory().toString();
    public static final String fileName = "contact_list.txt";
    public static String filePath ="";

    public static final String[]  EMAIL_TO = {"taher.jamil@gmail.com"};
    public static final String[]  EMAIL_TO_CC = {"bsse0501@iit.du.ac.bd"};
    public static final String[]  EMAIL_TO_BCC ={};

    public static final String EMAIL_SUBJECT ="Android Mobile Phone Contact List";
    public static final String EMAIL_BODY ="Here is my Mobile Phone Contact List";
    public static final String EMAIL_TYPE ="message/rfc822";



}
