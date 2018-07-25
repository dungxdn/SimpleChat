package jp.bap.traning.simplechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

@EActivity(R.layout.activity_network)
public class NetworkActivity extends BaseActivity {

    @ViewById
    AppCompatButton btnTryAgain;

    @Override
    public void afterView() {
        overridePendingTransition(R.anim.anim_shake,0);
    }

    @Click
    void btnTryAgain() {
        if (isConnectedNetwork() == true) {
            finish();
        } else {
            Toast.makeText(NetworkActivity.this, "No Internet. Please Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}