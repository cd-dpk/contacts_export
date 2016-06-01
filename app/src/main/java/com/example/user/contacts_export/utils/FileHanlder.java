package com.example.user.contacts_export.utils;

import android.util.Log;

import com.example.user.contacts_export.data.constants.ApplicationConstants;
import com.example.user.contacts_export.datamodels.Contact;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by chandradasdipok on 5/31/2016.
 */
public class FileHanlder {

    public static boolean createContactListFile(List<Contact>contactList){
        File newFolder = new File(ApplicationConstants.externalStorageFolder,"contact");
        if (!newFolder.exists()){
            newFolder.mkdir();
        }
        File file = new File(newFolder, ApplicationConstants.fileName);
        Log.d("File",file.getAbsolutePath());

        ApplicationConstants.filePath = file.getAbsolutePath();
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (Contact contact:contactList){
                Log.d("ContactList",contact.personName+":"+contact.personNumber);
                fileWriter.write(contact.personNumber+"\n");
            }
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
