package com.fiki.app.wifi.wifidocumentation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiki.app.wifi.wifidocumentation.src.domain.activitives.EmailMe;
import com.fiki.app.wifi.wifidocumentation.src.domain.activitives.MainMenuActivity;
import com.fiki.app.wifi.wifidocumentation.src.domain.activitives.MyDataList;
import com.fiki.app.wifi.wifidocumentation.src.domain.activitives.PutOutData;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.Validations;
import com.fiki.app.wifi.wifidocumentation.src.domain.dialogs.DialogHandler;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.mydata.MyDataCudService;

public class MainActivity extends AppCompatActivity implements ServiceResultReceiver.Receiver{

    private Button sendBtn,editBtn;
    private EditText txtMacAddress, txtPortNo, txtSerialNo, txtSwitchNo,txtLocation;
    private MyData myObject,editObject,sendableObject;
    private String macAddress, portNo, serialNo, switchNo;
    private ServiceResultReceiver receiver;
    private DialogHandler dialog;
    private EmailDetails emailObject;
    private boolean dataFound;
    private boolean isEditing = false;

    private void initComponents() {
        //Buttons
        sendBtn = (Button) findViewById(R.id.btnSend);
        editBtn = (Button) findViewById(R.id.btnEditData);
        //editText
        txtMacAddress = (EditText) findViewById(R.id.txtMacAddress);
        txtPortNo = (EditText) findViewById(R.id.txtPortNumber);
        txtSerialNo = (EditText) findViewById(R.id.txtSerialNo);
        txtSwitchNo = (EditText) findViewById(R.id.txtSwitchNumber);
        txtLocation = (EditText) findViewById(R.id.txtLocation_ip);
        hideEditButton();
    }

    private void disableAll()
    {
        txtMacAddress.setEnabled(false);
        txtPortNo.setEnabled(false);
        txtSerialNo.setEnabled(false);
        txtSwitchNo.setEnabled(false);
        txtLocation.setEnabled(false);
        showEditButon();
    }
    private void enableAll()
    {
        txtMacAddress.setEnabled(true);
        txtLocation.setEnabled(true);
        txtPortNo.setEnabled(true);
        txtSerialNo.setEnabled(true);
        txtSwitchNo.setEnabled(true);
        hideEditButton();
    }

    private void showEditButon()
    {editBtn.setVisibility(View.VISIBLE);}
    private void hideEditButton()
    {editBtn.setVisibility(View.INVISIBLE);}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_data_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.myDataMenu:
                //startActivity(new Intent(this, About.class));
                startActivity(new Intent(this, PutOutData.class));

                return true;
//            case R.id.help:
//                //startActivity(new Intent(this, Help.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        emailObject = (EmailDetails) bundle.getSerializable("emailDetails");
        dialog = new DialogHandler(this);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);

        initComponents();
        setButtons();

        if(bundle.containsKey("editData"))
        {
            disableAll();
            editObject =(MyData) bundle.getSerializable("editData");
            txtMacAddress.setText(editObject.getMacAddress());
            txtPortNo.setText(editObject.getPortNo());
            txtSerialNo.setText(editObject.getSerialNo());
            txtLocation.setText(editObject.getLocation());
            txtSwitchNo.setText(editObject.getSwitchNo());
            isEditing = true;
        }
        else{
            enableAll();
        }
    }

    private void setButtons() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getValues())
                {
                dialog.Confirm("Save", "Do you want to save?",
                        "No", "Yes",
                        new Runnable() { //coolest thing ever
                            @Override
                            public void run() {

                                try
                                {
                                    if( editObject.equals(null))
                                    {}
                                    else{

                                        editObject = new MyData.Builder().copy(editObject)
                                                .macAddress(myObject.getMacAddress())
                                                .portNo(myObject.getPortNo())
                                                .serialNo(myObject.getSerialNo())
                                                .switchNo(myObject.getSwitchNo())
                                                .build();

                                        Intent service = new Intent(MainActivity.this,MyDataCudService.class);

                                        service.putExtra("requestTag", StringValues.requestText.UPDATE);
                                        service.putExtra("dtoTag", editObject);
                                        service.putExtra("receiverTag", receiver);

                                        sendableObject = editObject;
                                        startService(service);
                                        editObject = null;
                                    }
                                }
                                catch (NullPointerException ex)
                                {
                                    Intent service = new Intent(MainActivity.this,MyDataCudService.class);

                                    service.putExtra("requestTag", StringValues.requestText.CREATE);
                                    service.putExtra("dtoTag", myObject);
                                    service.putExtra("receiverTag", receiver);

                                    sendableObject = myObject;
                                    startService(service);
                                }

                            }
                        },
                        new Runnable() {
                            @Override
                            public void run() {
                                //no
                            }
                        }
                );//end of dialog

            }
            else{
                    Toast.makeText(MainActivity.this,"please fill in all fields",Toast.LENGTH_SHORT).show();
//                    dialog.outputMessage("Warning","please fill in all fields");
                }
        }});


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableAll();
            }
        });
    }

    //get data from views
    private boolean getValues() {
        myObject = new MyData.Builder()
                .macAddress(txtMacAddress.getText().toString())
                .serialNo(txtSerialNo.getText().toString())
                .switchNo(txtSwitchNo.getText().toString())
                .portNo(txtPortNo.getText().toString())
                .location(txtLocation.getText().toString())
                .build();

        macAddress = myObject.getMacAddress();
        portNo = myObject.getPortNo();
        serialNo = myObject.getSerialNo();
        switchNo = myObject.getSwitchNo();

        return checkValues();
    }
//chech not null values
    private boolean checkValues()
    {
        if(Validations.checkDataWasEntered(txtMacAddress.getText().toString()))
        {
            if(Validations.checkDataWasEntered(txtPortNo.getText().toString()))
            {
                if(Validations.checkDataWasEntered(txtSerialNo.getText().toString()))
                {
                    if(Validations.checkDataWasEntered(txtSwitchNo.getText().toString()))
                    {
                        if(Validations.checkDataWasEntered(txtLocation.getText().toString()))
                        {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }
    private boolean validations() {

        if (Validations.validateSize(macAddress, 1)) {
            if (Validations.validateSize(portNo, 1)) {
                if (Validations.validateSize(serialNo, 1)) {
                    if (Validations.validateSize(switchNo, 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(isEditing)
        {
            Intent in = new Intent(MainActivity.this, PutOutData.class);
            startActivity(in);
            finish();
        }
        else{
            Intent in = new Intent(MainActivity.this, MainMenuActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        ResultDTO resultDTOFound = null;
        resultDTOFound = (ResultDTO) resultData.getSerializable("ServiceTag");

        if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
        {

        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_BY_ID)){
//            object =(EmailDetails)resultDTOFound.getResult().get(0);
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.UPDATE))
        {
            if(resultDTOFound.getSResult().equals("-1"))
            {
                dialog.outputMessage("Error","The Data wasn't Updated");
            }
            else{
//                dialog.outputMessage("Success","The Data was updated");
                Toast.makeText(this, "Data was saved successfully", Toast.LENGTH_SHORT).show();
                txtMacAddress.setText("");
                txtPortNo.setText("");
                txtSerialNo.setText("");
                txtSwitchNo.setText("");
                txtLocation.setText("");
                //sendEmail
                sendMail(myObject);
            }
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.CREATE) ){

            if(resultDTOFound.getSResult().equals("-1"))
            {
                dialog.outputMessage("Error","The Data wasn't saved");
            }
            else{
                Toast.makeText(this, "Data was saved successfully", Toast.LENGTH_SHORT).show();
//                dialog.outputMessage("Success","The Data was saved");
                txtMacAddress.setText("");
                txtPortNo.setText("");
                txtSerialNo.setText("");
                txtSwitchNo.setText("");
                txtLocation.setText("");

                //sendEmail
                sendMail(myObject);
            }
        }else if(resultDTOFound.getRequest().equals(StringValues.requestText.DELETE)){
            if(resultDTOFound.getSResult().equals("-1"))
            {
                dialog.outputMessage("Error","The Item could not be deleted");
            }
            else {
                dialog.outputMessage("Success", "The Item was deleted");
                txtMacAddress.setText("");
                txtPortNo.setText("");
                txtSerialNo.setText("");
                txtSwitchNo.setText("");
            }
            editObject = null;
            myObject = null;
        }
    }

    private void sendMail(MyData data) {

        Intent i = new Intent(MainActivity.this,EmailMe.class);
        i.putExtra("emailData",data);
        startActivity(i);
    }
}