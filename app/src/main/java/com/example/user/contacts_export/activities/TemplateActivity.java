package com.example.user.contacts_export.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public abstract class TemplateActivity extends AppCompatActivity {

     /**
     *  init the view
     */
    public abstract void  initView();
    /**
     * load the data
     */
    public abstract void loadData();
    /**
     * it initializes the view with loading data
     */
    public abstract void initializeView();

    /**
     * it listen the view
     */
    public abstract void listenView();

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CustomAsyncTask().execute();
    }

    class CustomAsyncTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog progressDialog = new ProgressDialog(TemplateActivity.this);

        @Override
        protected void onPreExecute() {
            initView();
            progressDialog.setTitle("Messasge");
            progressDialog.setMessage("Initializing view");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            loadData();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            initializeView();
            listenView();
        }
    }

}
