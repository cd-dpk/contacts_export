package com.example.user.contacts_export.utils;

public class PhoneNumberHandler{

    public String phoneNumber;

    public PhoneNumberHandler(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        deleteUnnecessaryCharacters();
    }

    public void deleteUnnecessaryCharacters(){
        String modifiedPhoneNumber="";
        for (int i = 0; i < phoneNumber.length(); i++) {
            char ch = phoneNumber.charAt(i);
            switch (ch){
                case '+':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    modifiedPhoneNumber += ch;
                    break;
                default: break;
            }
        }
        phoneNumber=modifiedPhoneNumber;
    }
    public boolean isBangladeshiNumber(){
        if (phoneNumber.startsWith("+88") && phoneNumber.length() == 14) {
            return true;
        }
        else if(phoneNumber.startsWith("0088") && phoneNumber.length() == 15){
            return true;
        }
        else if (phoneNumber.startsWith("011")||phoneNumber.startsWith("012")||phoneNumber.startsWith("013")||phoneNumber.startsWith("014")||phoneNumber.startsWith("015")||phoneNumber.startsWith("016")||phoneNumber.startsWith("017")||phoneNumber.startsWith("018")||phoneNumber.startsWith("019")){
            if (phoneNumber.length() == 11){
                return true;
            }
        }
        return false;
    }

    public String toBangladeshiFormat(){
        if (phoneNumber.startsWith("+88"))  {
            return phoneNumber;
        }
        else if(phoneNumber.startsWith("0088")){
            phoneNumber ="+"+phoneNumber.substring(phoneNumber.indexOf('8'));
            return phoneNumber;
        }
        else {
            return "+88" + phoneNumber;
        }
    }
}