package com.example.user.contacts_export.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.contacts_export.R;
import com.example.user.contacts_export.adapters.CustomRecyclerViewAdapter;
import com.example.user.contacts_export.data.constants.ApplicationConstants;
import com.example.user.contacts_export.data.db.DataBaseHelper;
import com.example.user.contacts_export.datamodels.Contact;
import com.example.user.contacts_export.datamodels.ITable;
import com.example.user.contacts_export.interfaces.OnRecyclerViewItemListener;
import com.example.user.contacts_export.utils.FileHanlder;
import com.example.user.contacts_export.utils.PhoneNumberHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TemplateActivity implements OnRecyclerViewItemListener {
    RecyclerView contactListRecyclerView;
    List<Contact> contactList;
    FloatingActionButton exportFloatingActionButton;
    CoordinatorLayout contactListCoordinatorLayout;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    DataBaseHelper localDataBaseHelper = new DataBaseHelper(this);
    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        contactListRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_contact_list);
        contactList = new ArrayList<Contact>();
        exportFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        contactListCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinate_layout_contact_list);
    }

    @Override
    public void loadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            localDataBaseHelper.deleteRows(new Contact());
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            PhoneNumberHandler phoneNumberHandler = new PhoneNumberHandler(phoneNo);
                            if (phoneNumberHandler.isBangladeshiNumber()){
                                Contact contact = new Contact(name,phoneNumberHandler.toBangladeshiFormat());
                                if (localDataBaseHelper.insertRow(contact)){
                                    contactList.add(new Contact(name, phoneNo));
                                }
                            }
                        }
                        pCur.close();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadData();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void initializeView() {
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactListRecyclerView.setAdapter(new CustomRecyclerViewAdapter(this, contactList.size(),R.layout.card_contact));
    }

    @Override
    public void listenView() {
        exportFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Mail", "What?");
                List<ITable> rows = localDataBaseHelper.selectRows(new Contact());
                contactList = new ArrayList<Contact>();
                for (ITable row: rows){
                    contactList.add((Contact) row);
                }
                if (FileHanlder.createContactListFile(contactList)) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType(ApplicationConstants.EMAIL_TYPE);
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, ApplicationConstants.EMAIL_TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, ApplicationConstants.EMAIL_TO_CC);
                    emailIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(ApplicationConstants.filePath)));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, ApplicationConstants.EMAIL_SUBJECT);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, ApplicationConstants.EMAIL_BODY);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        Log.d("Indicator", "Finished sending email...");
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Flag", "OKAY");
                } else {
                    final Snackbar snackbar = Snackbar.make(contactListCoordinatorLayout,"Error in Exporting Contacts",Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                    Log.d("Flag", "NO");
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void listenRecyclerViewItem(View view, int position) {

        TextView personName, personPhoneNumber;

        personName = (TextView) view.findViewById(R.id.text_view_card_contact_person_name);
        personPhoneNumber = (TextView) view.findViewById(R.id.text_view_card_contact_person_number);

        personName.setText(contactList.get(position).personName);
        personPhoneNumber.setText(contactList.get(position).personNumber);
    }
}
