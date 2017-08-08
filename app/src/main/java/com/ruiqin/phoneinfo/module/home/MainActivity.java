package com.ruiqin.phoneinfo.module.home;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import com.ruiqin.phoneinfo.R;
import com.ruiqin.phoneinfo.base.BaseActivity;
import com.ruiqin.phoneinfo.commonality.view.PermissionTipDialog;
import com.ruiqin.phoneinfo.module.BlankFragment;
import com.ruiqin.phoneinfo.module.home.adapter.MainRecyclerAdapter;
import com.ruiqin.phoneinfo.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresenter, MainModel> implements MainContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new BlankFragment());
        mPresenter.setAdapter();
    }

    @Override
    public boolean canBack() {
        mToolbarTitle.setText("BaseProject");
        return false;
    }

    private long lastClickTime;

    @Override
    public void onBackPressed() {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime > 2000) {
            ToastUtils.showShort("再按一次退出");
            lastClickTime = currentClickTime;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setRecyclerAdapterSuccess(MainRecyclerAdapter mainRecyclerAdapter) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        mRecyclerView.setAdapter(mainRecyclerAdapter);
    }

    @OnClick({R.id.imei, R.id.btn1, R.id.imsi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imei:
                checkIMEIPermission();
                break;
            case R.id.btn1:
                BluetoothAdapter bAdapt = BluetoothAdapter.getDefaultAdapter();
                if (bAdapt != null) {
                    String address = bAdapt.getAddress();
                    mTvContent.setText(address);
                }
                break;
            case R.id.imsi:
                checkIMSIPermission();
                break;
        }
    }

    private static final int IMEI_PERMISSION = 1;

    /**
     * 获取IMEI的权限
     */
    private void checkIMEIPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, IMEI_PERMISSION);
        } else {
            mTvContent.setText(getIMEI());//获取设备ID
        }
    }

    private static final int IMSI_PERMISSION = 2;

    /**
     * 获取IMSI的权限
     */
    private void checkIMSIPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, IMSI_PERMISSION);
        } else {
            mTvContent.setText(getIMSI());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            showPermission();
        }
        switch (requestCode) {
            case IMEI_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermission();
                    return;
                }
                checkIMEIPermission();
                break;
            case IMSI_PERMISSION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermission();
                    return;
                }
                checkIMSIPermission();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 展示权限
     */
    PermissionTipDialog mPermissionTipDialog;

    private void showPermission() {
        if (mPermissionTipDialog == null) {
            mPermissionTipDialog = new PermissionTipDialog(mContext);
        }
        mPermissionTipDialog.show();
    }

    /**
     * 获取设备Id(IMEI)
     */
    private String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null ? telephonyManager.getDeviceId() : null;
    }

    /**
     * 获取IMSI
     */
    private String getIMSI() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null ? telephonyManager.getSubscriberId() : null;
    }
}
