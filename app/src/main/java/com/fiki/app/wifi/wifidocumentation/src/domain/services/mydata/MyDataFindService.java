package com.fiki.app.wifi.wifidocumentation.src.domain.services.mydata;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;


import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl.MyDataDAOImpl;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.MyDataDAO;


import java.util.HashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyDataFindService extends IntentService {
    private ResultDTO resultDTO;
    private Bundle bundle;
    private MyDataDAO dao;

    public MyDataFindService() {

        super("EmailFindService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        runService(intent);
    }
    private void runService(Intent intent)
    {
        dao = new MyDataDAOImpl(getApplicationContext());
        MyData object;

        ResultReceiver receiver = intent.getParcelableExtra("receiverTag");//send ur receive through
        object = (MyData)intent.getSerializableExtra("dtoTag");
        String request = (String)intent.getStringExtra("requestTag");

        bundle = new Bundle();//what we sending back to main activity

        if(!request.isEmpty())
        {
            String sResult = null;
            HashMap resultmap = new HashMap();

            if(request.equals("findById"))
            {
                MyData tmpObject = dao.findById(object.getId());
                resultmap.put(0,tmpObject);

                sResult = String.valueOf(tmpObject.getId());
            }
            else if(request.equals("findAll"))
            {
                resultmap = dao.getList();
                sResult = String.valueOf(resultmap.size()-1);
                if(resultmap.containsKey("error"))
                {sResult = String.valueOf(-1);}
            }

            resultDTO = new ResultDTO.Builder()
                    .request(request)
                    .sResult(sResult)//whether passed or not
                    .result(resultmap)
                    .build();
            bundle.putSerializable("ServiceTag", resultDTO);
        }
        else
        {
            resultDTO = new ResultDTO.Builder()
                    .request(request)
                    .sResult("failed")//whether passed or not
                    .build();
            bundle.putSerializable("ServiceTag", resultDTO);
        }
        receiver.send(0,bundle);
    }

    public Bundle runForTesting(Intent intent)
    {
        runService(intent);
        return bundle;
    }
}
