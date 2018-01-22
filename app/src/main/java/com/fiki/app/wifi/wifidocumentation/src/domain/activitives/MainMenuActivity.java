package com.fiki.app.wifi.wifidocumentation.src.domain.activitives;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fiki.app.wifi.wifidocumentation.MainActivity;
import com.fiki.app.wifi.wifidocumentation.SetEmailAct;
import com.fiki.app.wifi.wifidocumentation.R;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.dialogs.DialogHandler;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.email.EmailFindService;

public class MainMenuActivity extends AppCompatActivity implements ServiceResultReceiver.Receiver{

//    COMPONENTS
    private Button btnSetEmail,btnAddData,btnList,btnList2;
    private DialogHandler dialog;
    private boolean dataFound;
    private EmailDetails emailObject;
    private ServiceResultReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dialog = new DialogHandler(this);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        initComponent();
        setButtons();
    }

    private void setButtons() {
        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent service = new Intent(MainMenuActivity.this, EmailFindService.class);

                service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
                service.putExtra("dtoTag", emailObject);
                service.putExtra("receiverTag", receiver);

                startService(service);
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, PutOutData.class);
                startActivity(intent);
            }
        });

        btnSetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SetEmailAct.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        btnSetEmail = (Button)findViewById(R.id.mainMenuActSavEmail);
        btnAddData = (Button)findViewById(R.id.MainMenuActAddData);
        btnList = (Button)findViewById(R.id.MainMenuActListData);
        btnList2 = (Button)findViewById(R.id.MainMenuActListData2);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ResultDTO resultDTOFound = null;
        resultDTOFound = (ResultDTO) resultData.getSerializable("ServiceTag");

        if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
        {
            if(resultDTOFound.getSResult().equals("-1"))
            {
                dataFound = false;
                dialog.outputMessage("Warning","You cannot send an email without setting your email details!");
            }
            else{
                dataFound = true;
                emailObject = (EmailDetails) resultDTOFound.getResult().get(0);

                Intent i = new Intent(MainMenuActivity.this,MainActivity.class);
                i.putExtra("emailDetails",emailObject);
                startActivity(i);
            }
        }
    }
}
