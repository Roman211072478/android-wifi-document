package com.fiki.app.wifi.wifidocumentation.src.domain.services.mydata;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.MyData;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl.MyDataDAOImpl;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.MyDataDAO;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyDataCudService extends IntentService {
    private ResultDTO resultDTO;
    private Bundle bundle;
    private MyDataDAO dao;

    public MyDataCudService() {

        super("MyDataCrudService");
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
            if(request.equals("create"))
            {
                Long result = dao.create(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("update"))
            {
                int result = dao.update(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("delete"))
            {
                int result = dao.delete(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("deleteTable"))
            {
                int result = dao.deleteTable();
                sResult = String.valueOf(result);
            }

            resultDTO = new ResultDTO.Builder()
                    .request(request)
                    .sResult(sResult)//whether passed or not
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
