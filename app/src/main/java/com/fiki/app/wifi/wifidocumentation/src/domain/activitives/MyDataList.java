package com.fiki.app.wifi.wifidocumentation.src.domain.activitives;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fiki.app.wifi.wifidocumentation.MainActivity;
import com.fiki.app.wifi.wifidocumentation.R;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.dialogs.DialogHandler;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.mydata.MyDataFindService;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDataList extends AppCompatActivity implements ServiceResultReceiver.Receiver{

    ListView lvResults;
    private HashMap map;
    private ServiceResultReceiver receiver;
    private DialogHandler dialog;
    private List<MyData> myDataList;
    private MyData object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_list);

        dialog = new DialogHandler(MyDataList.this);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        initComponents();
        loadData();
        setOnItemClick();
    }

    private void setOnItemClick()
    {
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String data = (String) parent.getItemAtPosition(position);


                if (!(data.trim()).equals("")) {

                    object = myDataList.get(position);

                    dialog.Confirm("View/edit", "Would you live to view and edit? ",
                            "No", "Yes",
                            new Runnable() { //coolest thing ever
                                @Override
                                public void run() {

                                    try
                                    {
                                        if(object != null) {
                                            Intent intent = new Intent(MyDataList.this, MainActivity.class);
                                            intent.putExtra("editData",object);
                                            startActivity(intent);
                                            object = null;
                                        }
                                    }
                                    catch (NullPointerException ex)
                                    {

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
            }
        });
    }
    private void loadData() {

        Intent service = new Intent(MyDataList.this, MyDataFindService.class);

        //put extra values into the intent, to be send to the called service
        service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
        service.putExtra("dtoTag", new MyData.Builder().build());
        service.putExtra("receiverTag", receiver);

        startService(service);
    }

    private void initComponents() {
        lvResults = (ListView)findViewById(R.id.lvResults);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        ResultDTO resultDTOFound = null;
        resultDTOFound = (ResultDTO) resultData.getSerializable("ServiceTag");

        if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_ALL))
        {
            if(resultDTOFound.getSResult().equals("-1" ))
            {
                //no data found
            }
            else{
                myDataList = new ArrayList<>();
                for(int i = 0;i<resultDTOFound.getResult().size();i++)
                {
                    myDataList.add((MyData)resultDTOFound.getResult().get(i));
                }
                setListView(resultDTOFound);
            }
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.FIND_BY_ID)){
//            object = (MyData)resultDTOFound.getResult().get(0);
        }
        else if(resultDTOFound.getRequest().equals(StringValues.requestText.CREATE)){
            dialog.outputMessage("Success","EmailDetails details was successfully set");

        }else if(resultDTOFound.getRequest().equals(StringValues.requestText.DELETE)){

        }

    }
    private void setListView(ResultDTO resultDTO)
    {

        if(resultDTO.getSResult().equals("-1"))
        {

        }
        else {
            List<String> list = new ArrayList<String>();

            if (!myDataList.isEmpty()) {
                // Get a set of the entries

                for(int i = 0; i < myDataList.size();i++)
                {

                    list.add(myDataList.get(i).getId() + " | " + myDataList.get(i).getMacAddress() + " | " +
                            myDataList.get(i).getSerialNo() + " | " +
                            myDataList.get(i).getPortNo()+ " | " +
                            myDataList.get(i).getSwitchNo()+" | " +
                            myDataList.get(i).getLocation());//Add to string list
                }

                lvResults.setAdapter(new ArrayAdapter<String>(MyDataList.this, android.R.layout.simple_list_item_1, list));
            }
        }
    }
}
