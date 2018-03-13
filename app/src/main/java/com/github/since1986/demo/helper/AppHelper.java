package com.github.since1986.demo.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.github.since1986.demo.MainActivity;
import com.github.since1986.demo.R;

public class AppHelper {

    private Context context;

    public AppHelper(Context context) {
        this.context = context;
    }

    public void showExitDialog() {

        new AlertDialog.Builder(context)
                .setTitle(R.string.text_exit_app)
                .setMessage(R.string.text_confirm_exit_app)
                .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //完全退出
                        context.startActivity(
                                new Intent(context, MainActivity.class)
                                        .putExtra(MainActivity.KEY_EXIT_APP, true)
                        );
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }
}
