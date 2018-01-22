package com.fiki.app.wifi.wifidocumentation.src.domain.services.receipt;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.ReceipentsModel;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl.ReceiptDAOImpl;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.ReceiptDAO;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RecieptCrudService extends IntentService {
    private ResultDTO resultDTO;
    private Bundle bundle;
    private ReceiptDAO receiptDAO;

    public RecieptCrudService() {

        super("RecieptCrudService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        runService(intent);
    }
    private void runService(Intent intent)
    {
        receiptDAO = new ReceiptDAOImpl(getApplicationContext());
        ReceipentsModel object;

        ResultReceiver receiver = intent.getParcelableExtra("receiverTag");//send ur receive through
        object = (ReceipentsModel)intent.getSerializableExtra("dtoTag");
        String request = (String)intent.getStringExtra("requestTag");

        bundle = new Bundle();//what we sending back to main activity

        if(!request.isEmpty())
        {
            String sResult = null;
            if(request.equals("create"))
            {
                receiptDAO.deleteTable();

                Long result = receiptDAO.create(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("update"))
            {
                int result = receiptDAO.update(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("delete"))
            {
                int result = receiptDAO.delete(object);
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
