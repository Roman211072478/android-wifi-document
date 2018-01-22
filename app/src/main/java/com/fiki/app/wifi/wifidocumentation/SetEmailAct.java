package com.fiki.app.wifi.wifidocumentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.dialogs.DialogHandler;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.email.EmailCrudService;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.email.EmailFindService;

import java.util.HashMap;

public class SetEmailAct extends Activity implements ServiceResultReceiver.Receiver {

    Button btnSave,setBtn,cancBtn;
    LinearLayout editView,currentView;
    TextView tvCurrentEmail;
    EditText txtEmail,txtPassword;
    EmailDetails object;
    private HashMap map;
    private ServiceResultReceiver receiver;
    private DialogHandler dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        dialog = new DialogHandler(this);

        initComponents();
        setButtons();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.email_set_menu, menu);
        return true;
    }

    //check if if email address set
    private void loadData() {

        //Request data
        EmailDetails tmObj = new EmailDetails.Builder().build();

        Intent service = new Intent(SetEmailAct.this, EmailFindService.class);

        //put extra values into the intent, to be send to the called service
        service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
        service.putExtra("dtoTag", tmObj);
        service.putExtra("receiverTag", receiver);

        startService(service);
    }

    private void setButtons() {
    //save buttons
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    EmailDetails myEmail =  new EmailDetails.Builder()
                            .email(txtEmail.getText().toString())
                            .password(txtPassword.getText().toString())
                            .build();

                    Intent service = new Intent(SetEmailAct.this, EmailCrudService.class);

                    //put extra values into the intent, to be send to the called service
                    service.putExtra("requestTag", StringValues.requestText.CREATE);
                    service.putExtra("dtoTag", myEmail);
                    service.putExtra("receiverTag", receiver);

                    startService(service);
                }

            }
        });

    //set button
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditing();
            }
        });

    //cancel button
        cancBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrent();
                txtEmail.setText("");
                txtPassword.setText("");
            }
        });
    }

    //validation
    private boolean validate() {

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(!email.equals("")){
            if(!password.equals(""))
            {

                return true;
            }else{
                Toast.makeText(SetEmailAct.this, "Please enter a password", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SetEmailAct.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //show editing form and hide current email
    private void showEditing()
    {
        editView.setVisibility(View.VISIBLE);
        currentView.setVisibility(View.GONE);
    }
    //show current email, and hide edit view
    private void showCurrent()
    {
        editView.setVisibility(View.GONE);
        currentView.setVisibility(View.VISIBLE);
    }
    //initiate components
    private void initComponents()
    {
        btnSave = (Button)findViewById(R.id.btnSaveEmail);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        tvCurrentEmail = (TextView)findViewById(R.id.set_email_curent_email);
        setBtn = (Button)findViewById(R.id.set_email_btn_set_email);
        cancBtn = (Button)findViewById(R.id.set_email_cancl_btn);
        editView = (LinearLayout)findViewById(R.id.set_email_edit_email);
        currentView = (LinearLayout)findViewById(R.id.set_email_current_layout);

        showCurrent();
    }
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ResultDTO resultDTOFound = null;
        resultDTOFound = (ResultDTO) resultData.getSerializable("ServiceTag");

        if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
        {
            if(resultDTOFound.getSResult().equals("-1") )
            {
                //no data found
                tvCurrentEmail.setText("Please set a email address");
            }
            else{
                object = (EmailDetails)resultDTOFound.getResult().get(0);
                tvCurrentEmail.setText(object.getEmail());
                object = null;
            }
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_BY_ID)){
            object =(EmailDetails)resultDTOFound.getResult().get(0);
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.CREATE)){
            dialog.outputMessage("Success","EmailDetails details was successfully set");
            txtEmail.setText("");
            txtPassword.setText("");
            showCurrent();
            loadData();

        }else if(resultDTOFound.getRequest().equals(StringValues.requestText.DELETE)){
        }
    }
}
