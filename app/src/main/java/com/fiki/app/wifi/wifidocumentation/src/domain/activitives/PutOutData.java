package com.fiki.app.wifi.wifidocumentation.src.domain.activitives;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.fiki.app.wifi.wifidocumentation.MainActivity;
import com.fiki.app.wifi.wifidocumentation.R;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.IsAtablet;
import com.fiki.app.wifi.wifidocumentation.src.domain.confi.StringValues;
import com.fiki.app.wifi.wifidocumentation.src.domain.dialogs.DialogHandler;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.receiver.ServiceResultReceiver;
import com.fiki.app.wifi.wifidocumentation.src.domain.services.mydata.MyDataFindService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class PutOutData extends Activity implements ServiceResultReceiver.Receiver{

    private Button nextBtn,prevButton;
    private ServiceResultReceiver receiver;
    private DialogHandler dialog;
    private List<MyData> myDataList;
    private int pagerLimit;
    private LinearLayout mainLayout,rightSideLayout;
    private boolean isTablet;
    private Integer amountOfPages;
    private int currentPage = 1;
    private TextView txtCurrentPage;
    private int starter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(IsAtablet.isItATablet(PutOutData.this))
        {
            isTablet = true;
            pagerLimit = 12;
            setContentView(R.layout.put_outdata_large);
        }
        else{
            pagerLimit = 6;
            isTablet = false;
            setContentView(R.layout.activity_put_out_data);
        }

        dialog = new DialogHandler(PutOutData.this);
        receiver = new ServiceResultReceiver(new Handler());
        receiver.setReceiver(this);
        initComponents();
        loadData();
    }

    private void loadData() {

        Intent service = new Intent(PutOutData.this, MyDataFindService.class);

        //put extra values into the intent, to be send to the called service
        service.putExtra("requestTag", StringValues.requestText.FIND_ALL);
        service.putExtra("dtoTag", new MyData.Builder().build());
        service.putExtra("receiverTag", receiver);

        startService(service);
    }
    private void initComponents() {
        mainLayout = (LinearLayout)findViewById(R.id.data_main_layout);
        rightSideLayout = (LinearLayout)findViewById(R.id.data_main_layout_two);
        nextBtn = (Button)findViewById(R.id.output_data_next);
        prevButton = (Button)findViewById(R.id.output_data_previous);
        txtCurrentPage = (TextView) findViewById(R.id.outdata_current_page);

        prevButton.setEnabled(false);
        setButtons();
    }

    private void setButtons() {
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    }
    //previous page
    private void previous()
    {
        if(isTablet)
        {
            rightSideLayout.removeAllViews();
        }
        mainLayout.removeAllViews();

            currentPage--;
//            starter = currentPage * pagerLimit;
            starter =  starter - pagerLimit;
            showData(starter);

            //check if there are more pages if not, den disable buttons
            if((currentPage-1) < 1)
            {
                prevButton.setEnabled(false);
            }
            if((currentPage) < amountOfPages)
            {
                nextBtn.setEnabled(true);
            }

    }
    //next page
    private void next()
    {
        if(isTablet)
        {
            rightSideLayout.removeAllViews();
        }
        mainLayout.removeAllViews();

        currentPage++;

         //   starter = (currentPage-1) * pagerLimit;

        starter=starter+pagerLimit;
            showData(starter);

            if(!prevButton.isEnabled())
            {
                prevButton.setEnabled(true);
            }

            //check if there are more pages if not, den disable buttons
            if(currentPage == amountOfPages)
            {
                nextBtn.setEnabled(false);
            }


    }
    //create data
    private void showData(int starter) {

        int tester = 0;
        for(int i = starter;i<(starter+pagerLimit);i++)
        {
            if(myDataList.size() == i)
            {
                break;
            }
            TableLayout table = new TableLayout(this);
            table.setPadding(3,0,3,5);

            tester = i+1;
            if(tester % 2 == 0)
            {
                table.setBackgroundColor(Color.parseColor("#FF303F9F"));
            }
            else{
                table.setBackgroundColor(Color.BLACK);
            }

            if(tester % 2 == 0)
            {
                if (isTablet) {
//                    testCreateDataView(mainLayout, table, i, myDataList.get(i)); //index is objectListIndex
                    testCreateDataView(rightSideLayout, table, i, myDataList.get(i)); //index is objectListIndex
                }
                else {
                    testCreateDataView(mainLayout, table, i, myDataList.get(i)); //index is objectListIndex

                }
            }
            else {
                testCreateDataView(mainLayout, table, i, myDataList.get(i)); //index is objectListIndex
            }
        }
        txtCurrentPage.setText(String.valueOf(currentPage)+" of "+String.valueOf(amountOfPages));

    }
    private void testCreateDataView(LinearLayout mainOne,TableLayout layout,int index,MyData dataObject) {
//        tableLayoutList.get(0);

        /***table row*/
        TableRow row1 = firstRow(dataObject);
        /***row 2*/
        //table row
        TableRow row2 = secondRow();
        /***row 3 dynamic*/
        TableRow row3 = thirdRow(dataObject);
        /**Row 4**/
        TableRow row4 = forthRow();
        /**Row 5 dynamic**/
        TableRow row5 = fifthRow(dataObject);

        Button editButton = new Button(this);
        editButton.setText("SELECT");

        layout.addView(row1);
        layout.addView(row2);
        layout.addView(row3);
        layout.addView(row4);
        layout.addView(row5);
        layout.addView(editButton);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        editButton.setLayoutParams(params);

        editButton.setId(index);
        editButton.setTextColor(Color.parseColor("#ff000000"));
        setButton(editButton);

        mainOne.addView(layout);
//        rightSideLayout;

        //using linear maybe coz its parent is linear. i dont knw dude
        LinearLayout.LayoutParams tableParams = (LinearLayout.LayoutParams)layout.getLayoutParams();
        tableParams.setMargins(0,0,0,5);

        layout.setLayoutParams(tableParams);
    }

    //set button
    public void setButton(Button button) {

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

//               Toast.makeText(PutOutData.this, "index "+ v.getId(), Toast.LENGTH_SHORT).show();
               myDataList.get( v.getId());

               Intent intent = new Intent(PutOutData.this, MainActivity.class);
               intent.putExtra("editData", myDataList.get( v.getId()));
               startActivity(intent);
               finish();
           }
       });
    }
    //dynamic data
    private TableRow fifthRow(MyData data) {
        TableRow row5 = new TableRow(this);
        row5.setPadding(3,0,3,0);
        //textView
        TextView switchNo = new TextView(this);
        switchNo.setText(String.valueOf(data.getSwitchNo()));
        switchNo.setTextColor(Color.parseColor("#ffffff"));
        switchNo.setPadding(0,0,10,0);
        //set Text to row
        row5.addView(switchNo);

        TextView location = new TextView(this);
        location.setText(String.valueOf(data.getLocation()));
        location.setTextColor(Color.parseColor("#ffffff"));
        location.setPadding(10,0,0,0);

        //set Text to row
        row5.addView(location);
        TableRow.LayoutParams params = (TableRow.LayoutParams)location.getLayoutParams();
        params.span = 6;

        location.setLayoutParams(params);
        return row5;
    }

    private TableRow forthRow() {
        TableRow row4 = new TableRow(this);
        row4.setPadding(3,0,3,0);
        //textView
        TextView headSwitch = new TextView(this);
        headSwitch.setText("Switch No:");
        headSwitch.setTextColor(Color.parseColor("#FFFF4081"));
        headSwitch.setTypeface(null, Typeface.BOLD);
        headSwitch.setPadding(0,0,10,0);
        //set Text to row
        row4.addView(headSwitch);
        TextView headLocation = new TextView(this);
        headLocation.setText("Location:");
        headLocation.setTextColor(Color.parseColor("#FFFF4081"));
        headLocation.setTypeface(null, Typeface.BOLD);
        headLocation.setPadding(10,0,0,0);
        //set Text to row
        row4.addView(headLocation);
        return row4;
    }
//dynamic data
    private TableRow thirdRow(MyData data) {
        TableRow row3 = new TableRow(this);
        row3.setPadding(3,0,3,0);
        //textView
        TextView mac = new TextView(this);
        mac.setText(String.valueOf(data.getMacAddress()));
        mac.setTextColor(Color.parseColor("#ffffff"));
        mac.setPadding(0,0,10,0);
        //set Text to row
        row3.addView(mac);
        TextView serial = new TextView(this);
        serial.setText(String.valueOf(data.getSerialNo()));
        serial.setTextColor(Color.parseColor("#ffffff"));
        serial.setPadding(10,0,0,0);
        //set Text to row
        row3.addView(serial);
        TextView port = new TextView(this);
        port.setText(String.valueOf(data.getPortNo()));
        port.setTextColor(Color.parseColor("#ffffff"));
        port.setPadding(10,0,0,0);
        //set Text to row
        row3.addView(port);
        return row3;
    }

    private TableRow secondRow() {
        //table row
        TableRow row2 = new TableRow(this);
        row2.setPadding(3,0,3,0);
        //textView
        TextView headMac = new TextView(this);
        headMac.setText("Mac Address:");
        headMac.setTextColor(Color.parseColor("#FFFF4081"));
        headMac.setTypeface(null, Typeface.BOLD);
        headMac.setPadding(0,0,10,0);
        //set Text to row
        row2.addView(headMac);
        TextView headSerial = new TextView(this);
        headSerial.setText("Serial No:");
        headSerial.setTextColor(Color.parseColor("#FFFF4081"));
        headSerial.setTypeface(null, Typeface.BOLD);
        headSerial.setPadding(10,0,0,0);
        //set Text to row
        row2.addView(headSerial);
        TextView headPort = new TextView(this);
        headPort.setText("Port:");
        headPort.setTextColor(Color.parseColor("#FFFF4081"));
        headPort.setTypeface(null, Typeface.BOLD);
        headPort.setPadding(10,0,0,0);
        //set Text to row
        row2.addView(headPort);
        return row2;
    }

    private TableRow firstRow(MyData data) {
        /***table row*/
        TableRow row1 = new TableRow(this);
        row1.setPadding(3,0,3,0);
        //textView
        TextView idString = new TextView(this);
        idString.setText("Id");
        idString.setTextColor(Color.parseColor("#ffff8800"));
        //set Text to row
        row1.addView(idString);
        //textView
        TextView id = new TextView(this);
        id.setText(String.valueOf(data.getId()));
        id.setTypeface(null, Typeface.BOLD);
        id.setTextColor(Color.parseColor("#ffff8800"));
        //set Text to row
        row1.addView(id);
        //row1 done
        return row1;
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
                /***now process data**/
                showData(0);
                Integer value = myDataList.size() / pagerLimit;

                if(myDataList.size() % pagerLimit != 0)
                {
                    value++;
                }
                amountOfPages = value;
                if(currentPage < amountOfPages)
                {
                    nextBtn.setEnabled(true);
                    //disable next button. dont allow next
                }
                else
                {
                    nextBtn.setEnabled(false);
                }
                txtCurrentPage.setText(String.valueOf(currentPage)+" of "+String.valueOf(amountOfPages));

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
}
