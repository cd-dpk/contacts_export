package com.example.user.contacts_export.datamodels;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

/**
 * Created by chandradasdipok on 5/30/2016.
 */
public class Contact  implements ITable{
    public String personName;
    public String personNumber;

    WhereClause whereClause = new WhereClause();

    public Contact() {
    }

    public Contact(String personName, String personNumber) {
        this.personName = personName;
        this.personNumber = personNumber;
    }

    @Override
    public String tableName() {
        return "Contacts";
    }

    @Override
    public String toCreateTableString() {
        return "create table if not exists "+tableName()+" (" +
                ""+Variable.PHONE_NUMBER+" text primary key," +
                ""+Variable.PHONE_PERSON+" text)";
    }

    @Override
    public String toSelectString() {
        return "select * from "+tableName()+" where "+getWhereClauseString();
    }

    @Override
    public String toDeleteSingleRowString() {
        return "select * from "+tableName()+" where "+getWhereClauseString();
    }

    @Override
    public String toDeleteRows() {
        return "select * from "+tableName();
    }

    @Override
    public String toSelectSingleRowString() {
        return "select * from "+tableName()+" where "+getWhereClauseString();
    }

    @Override
    public ITable toITableFromCursor(Cursor cursor) {
        Contact contact = new Contact();
        if (cursor.getColumnIndex(Variable.PHONE_NUMBER)!=-1){
            contact.personNumber = cursor.getString(cursor.getColumnIndex(Variable.PHONE_NUMBER));
        }
        if (cursor.getColumnIndex(Variable.PHONE_PERSON)!=-1){
            contact.personName = cursor.getString(cursor.getColumnIndex(Variable.PHONE_PERSON));
        }
        return contact;
    }

    @Override
    public boolean isCloned(ITable iTable) {
        return false;
    }

    @Override
    public ITable toClone() {
        return null;
    }

    @Override
    public ContentValues getInsertContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Variable.PHONE_NUMBER, this.personNumber);
        contentValues.put(Variable.PHONE_PERSON, this.personName);
        return contentValues;
    }

    @Override
    public void setUpdateContentValues(ContentValues updateContentValues) {

    }

    @Override
    public ContentValues getUpdateContentValues() {
        return null;
    }

    @Override
    public String getWhereClauseString() {
        return whereClause.toString();
    }

    public static class Variable {
        public static final String PHONE_NUMBER="phone_number";
        public static final String PHONE_PERSON="person_name";
    }
}
