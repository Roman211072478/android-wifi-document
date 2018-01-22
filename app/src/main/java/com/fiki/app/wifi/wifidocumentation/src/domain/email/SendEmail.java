package com.fiki.app.wifi.wifidocumentation.src.domain.email;

import android.content.Context;
import android.content.Intent;

import com.fiki.app.wifi.wifidocumentation.src.domain.model.EmailDetails;

/**
 * Created by root on 2017/10/17.
 */

public class SendEmail  {

    Context ctx;
    EmailDetails emailDetails;

    public SendEmail(Context ctx, EmailDetails emailDetails,String emailAddress)
    {
        this.emailDetails = emailDetails;
        this.ctx = ctx;
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(i.EXTRA_EMAIL,emailAddress);

    }

}
