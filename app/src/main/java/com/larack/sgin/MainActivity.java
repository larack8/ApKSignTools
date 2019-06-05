package com.larack.sgin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.larack.sgin.app.PackageListActivity;
import com.larack.sgin.tools.ApkSignUtils;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText mPkgNameEt;
    private TextView mResultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPkgNameEt = (EditText) findViewById(R.id.packageField);
        mResultTv = (TextView) findViewById(R.id.resultText);

        findViewById(R.id.selectBtn).setOnClickListener(this);
        findViewById(R.id.genSignatureBtn).setOnClickListener(this);
        findViewById(R.id.copyBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectBtn:
                onSelect();
                break;
            case R.id.genSignatureBtn:
                String sign = ApkSignUtils.getSign(MainActivity.this, mPkgNameEt.getText().toString());
                Log.d(TAG, "应用签名:" + sign);
                mResultTv.setText(sign);
                break;
            case R.id.copyBtn:
                doCopy();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            String packageName = data.getStringExtra("packageName");
            mPkgNameEt.setText(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 显示选择列表
    protected void onSelect() {
        Intent intent = new Intent(MainActivity.this, PackageListActivity.class);
        startActivityForResult(intent, 1);
    }

    // 复制到剪切板
    protected void doCopy() {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(mResultTv.getText().toString());
        Toast.makeText(this, "已复制到剪切板", Toast.LENGTH_LONG).show();
    }
}
