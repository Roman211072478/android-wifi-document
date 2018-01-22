package com.fiki.app.wifi.wifidocumentation.src.domain.services.email;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;
import com.fiki.app.wifi.wifidocumentation.src.domain.model.ResultDTO;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.EmailDAO;
import com.fiki.app.wifi.wifidocumentation.src.domain.repos.Impl.EmailDAOImpl;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class EmailCrudService extends IntentService {

    private ResultDTO resultDTO;
    private Bundle bundle;
    private EmailDAO emailDAO;

    public EmailCrudService() {

        super("EmailCrudService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        runService(intent);
    }
    private void runService(Intent intent)
    {
        emailDAO = new EmailDAOImpl(getApplicationContext());
        EmailDetails object;

        ResultReceiver receiver = intent.getParcelableExtra("receiverTag");//send ur receive through
        object = (EmailDetails)intent.getSerializableExtra("dtoTag");
        String request = (String)intent.getStringExtra("requestTag");

        bundle = new Bundle();//what we sending back to main activity

        if(!request.isEmpty())
        {
            String sResult = null;
            if(request.equals("create"))
            {
                emailDAO.deleteTable();

                Long result = emailDAO.create(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("update"))
            {
                int result = emailDAO.update(object);
                sResult = String.valueOf(result);
            }
            else if(request.equals("delete"))
            {
                int result = emailDAO.delete(object);
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
