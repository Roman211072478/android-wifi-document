package com.fiki.app.wifi.wifidocumentation.src.domain.activitives;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.fiki.app.wifi.wifidocumentation.R;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ReceipentsModel;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.email.EmailFindService;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.receipt.ReceiptFindService;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.receipt.RecieptCrudService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.gmail.model.*;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EmailMe extends Activity  implements EasyPermissions.PermissionCallbacks,ServiceResultReceiver.Receiver {

    //Components
    private Button btnSend;
    private TextView myOutputNotic,tvOuput,tvMyEmail;
    private EditText txSubject,txBcc;
    private AutoCompleteTextView txToEmail;

    //library object
    GoogleAccountCredential myCredential;

    ProgressDialog myProgress;

    //Things taken from web which is needed
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;


    //Variables and objects
    private static String ACCOUNT_NAME = "";
    private Intent chooser;
    private MyData emailData;
    private ServiceResultReceiver receiver;
    private EmailDetails emailObject;
    private String choice = "";
    private String[] emailList;

    //where startService is called, do remember that all the returned outcomes is handled in the onRecieveResult override method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_me);
        Bundle bundle = getIntent().getExtras(); // get values passed from previous object

        //my custom made result receiver
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);

        //call my email find services
        Intent service = new Intent(EmailMe.this, EmailFindService.class);
        service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
        service.putExtra("dtoTag", emailObject);
        service.putExtra("receiverTag", receiver);
        startService(service);
        choice = "myEmail";
        ///

        if(bundle.containsKey("emailData"))
        {
            emailData =(MyData) bundle.getSerializable("emailData");
        }

        initCompon();
        btnSend = (Button) findViewById(R.id.emailButton);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation())
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto"));

                    //bbc not needed
                    if(checkIfTheIsBcc())
                    {
                        String[] bcc ={txBcc.getText().toString()};//list to who||the main receiver won't see this
                        intent.putExtra(Intent.EXTRA_BCC,bcc); //xml
                    }

                    String[] to ={txToEmail.getText().toString()};
                    intent.putExtra(Intent.EXTRA_EMAIL,to);//set xml
                    intent.putExtra(Intent.EXTRA_SUBJECT,txSubject.getText().toString());//get from
                    intent.putExtra(Intent.EXTRA_TEXT,setBody());
                    intent.setType("message/rfc822");
                    chooser = Intent.createChooser(intent,"SendEmail");
                    startActivity(chooser);

                    //saveDataOnEmailHistory
                    saveEmails();
                }
            }
        });
    }

    private void setAutoForTo()
    {
        //cool feature
        //this will set the auto-correct while inserting your email or email list you have used before

        if(emailList == null)
        {

        }
        else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, emailList);
            //Find TextView control
            //Set the number of characters the user must type before the drop down list is shown
            txToEmail.setThreshold(1);
            //Set the adapter
            txToEmail.setAdapter(adapter);
        }
    }

    //used to save data
    private void saveEmails() {
        ReceipentsModel emailStuff = new ReceipentsModel.Builder()
                .email(txToEmail.getText().toString())
                .build();
        Intent service = new Intent(EmailMe.this, RecieptCrudService.class);
        service.putExtra("requestTag", StringValues.requestText.CREATE);
        service.putExtra("dtoTag", emailStuff);
        service.putExtra("receiverTag", receiver);
        startService(service);
        choice = "emailList";
    }
    //returns email receipts
    private void findAllList()
    {
       try {
           ReceipentsModel object = new ReceipentsModel.Builder().build();
           Intent service = new Intent(EmailMe.this, ReceiptFindService.class);
           service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
           service.putExtra("dtoTag",object);
           service.putExtra("receiverTag", receiver);
           startService(service);
           choice = "emailList";
       }
       catch(Exception e)
       {

       }
    }


    private boolean checkIfTheIsBcc() {
        if(txBcc.getText().toString().trim().equals(""))
        {
            return false;
        }
        else{
            return true;
        }
    }

    //common validation
    private boolean validation() {

        if(txToEmail.getText().toString().trim().equals("") || txSubject.getText().toString().trim().equals(""))
        {
            //toEmail Empty strings
            Toast.makeText(this, "Please enter in a subject and an email", Toast.LENGTH_SHORT).show();
        }
        else{
            // it pass return true
            return true;
        }
        return false;
    }

    //initializing components
    private void initCompon() {

       myOutputNotic = (TextView)findViewById(R.id.textView15email_me_notifi);
        tvOuput = (TextView)findViewById(R.id.email_me_output);
        tvMyEmail = (TextView)findViewById(R.id.email_me_myemail);
        txSubject = (EditText) findViewById(R.id.email_me_subject);
        txToEmail = (AutoCompleteTextView)findViewById(R.id.email_me_to);
        txBcc = (EditText)findViewById(R.id.email_me_bcc);

        tvOuput.setText(setBody().toString());
    }

    //this would be the body of the email. Change/modify this for better outcomes in emails
    private String setBody()
    {
        String str = "Mac Address: "+emailData.getMacAddress()+
                "\nSerial No: "+emailData.getSerialNo()+
                "\nPort No: "+emailData.getPortNo()+
                "\nSwitch No:"+emailData.getSwitchNo()+
                "\nLocation: "+emailData.getLocation();

        return str;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        //Handle results of finished  services.

        ResultDTO resultDTOFound = null;
        resultDTOFound = (ResultDTO) resultData.getSerializable("ServiceTag");

        //find emails for sending
        if(choice.equals("emailList"))
        {
            if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
            {
                if(resultDTOFound.getSResult().equals("-1"))
                {
                    //no data found, but this shudnt happen
                }
                else{

                    String[] tempString = new String[resultDTOFound.getResult().size()];

                    ReceipentsModel object;
                    for(int i = 0; i<resultDTOFound.getResult().size();i++)
                    {
                        object = (ReceipentsModel) resultDTOFound.getResult().get(i);
                        tempString[i] = object.getEmail();
                    }


                    emailList = tempString;

                    setAutoCorrect();
                }
            }
        }
        else{

            if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
            {
                if(resultDTOFound.getSResult().equals("-1"))
                {
                    //no data found, but this shudnt happen
                }
                else{
                    emailObject = (EmailDetails) resultDTOFound.getResult().get(0);

                    ACCOUNT_NAME = emailObject.getEmail();

                    tvMyEmail.setText(ACCOUNT_NAME);

                    findAllList();
                }
            }
        }

    }

    private void setAutoCorrect() {
        setAutoForTo();
    }


    //everything below was used for using the api, which should not apply..
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (myCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            myOutputNotic.setText("No network connection available.");
        } else {
            new MakeRequestTask(myCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(ACCOUNT_NAME, null);
            if (accountName != null) {
                myCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        myCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    myOutputNotic.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(ACCOUNT_NAME, accountName);
                        editor.apply();
                        myCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                EmailMe.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }


        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }


        private List<String> getDataFromApi() throws IOException {
            // Get the labels in the user's account.
            String user = "romanrafiq456@gmail.com";
            List<String> labels = new ArrayList<String>();
            ListLabelsResponse listResponse =
                    mService.users().labels().list(user).execute();
            for (Label label : listResponse.getLabels()) {
                labels.add(label.getName());
            }
            return labels;
        }


        @Override
        protected void onPreExecute() {
            myOutputNotic.setText("");
            myProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            myProgress.hide();
            if (output == null || output.size() == 0) {
                myOutputNotic.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Gmail API:");
                myOutputNotic.setText(TextUtils.join("\n", output));
            }
        }

        @Override
        protected void onCancelled() {
            myProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            EmailMe.REQUEST_AUTHORIZATION);
                } else {
                    myOutputNotic.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                myOutputNotic.setText("Request cancelled.");
            }


        }

    }
}
