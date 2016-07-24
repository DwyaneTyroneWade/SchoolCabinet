package com.xiye.schoolcabinet.modules.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.manager.ConfigManager;

/**
 * Created by wushuang on 7/24/16.
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_help);
        initView();
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        setClassInfo(ConfigManager.getClassName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
}
