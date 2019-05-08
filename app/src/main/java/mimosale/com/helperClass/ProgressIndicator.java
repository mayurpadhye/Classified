package mimosale.com.helperClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mimosale.com.R;

public class ProgressIndicator {

    private static ProgressDialog progressDialog;
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(context.getResources().getString(R.string.loading));
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null ) {
            progressDialog.dismiss();
        }


}
}
