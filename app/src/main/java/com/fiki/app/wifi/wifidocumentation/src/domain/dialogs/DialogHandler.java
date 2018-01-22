package com.fiki.app.wifi.wifidocumentation.src.domain.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

/**
 * Created by root on 2017/10/16.
 */

public class DialogHandler {
    private Runnable ans_true = null;
    private Runnable ans_false = null;
    private Context ctx;


    public DialogHandler(Context ctx)
    {
        this.ctx = ctx;
    }
    // Dialog. --------------------------------------------------------------

    public boolean Confirm( String Title, String ConfirmText,
                            String CancelBtn, String OkBtn, Runnable aProcedure, Runnable bProcedure)
    {

        ans_true = aProcedure;
        ans_false= bProcedure;

        AlertDialog dialog = new AlertDialog.Builder(ctx).create();


        dialog.setTitle(Title);
        dialog.setMessage(ConfirmText);
        dialog.setCancelable(false);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, OkBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_true.run();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, CancelBtn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        ans_false.run();
                    }
                });
        dialog.setIcon(android.R.drawable.ic_dialog_info);
        dialog.show();
        return true;
    }

    public void outputMessage(String title,String message)
    {
        Dialog d = new Dialog(ctx);
        d.setTitle(title);
        TextView tv = new TextView(ctx);

        tv.setText(message);
        d.setContentView(tv);
        d.show();
    }
}
