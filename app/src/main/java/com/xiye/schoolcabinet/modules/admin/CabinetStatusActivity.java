package com.xiye.schoolcabinet.modules.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.Record;
import com.xiye.schoolcabinet.database.manager.RecordTableManager;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.NeedleManager;
import com.xiye.schoolcabinet.modules.admin.widget.CabinetStatusAdapter;
import com.xiye.sclibrary.net.needle.UiRelatedTask;

import java.util.List;

/**
 * Created by wushuang on 8/8/16.
 */
public class CabinetStatusActivity extends BaseActivity implements View.OnClickListener {
    private Button btnBack;
    private ListView listViewStatus;
    private CabinetStatusAdapter adapter;
    private TextView tvNoData;
    private LinearLayout llTitle;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activiyt_cabinet_status);
        initView();
        getData();
    }

    private void getData() {
        NeedleManager.getBackgroundThreadExecutorForRecord().execute(new UiRelatedTask<List<Record>>() {
            @Override
            protected List<Record> doWork() {
                return RecordTableManager.get();
            }

            @Override
            protected void thenDoUiRelatedWork(List<Record> records) {
                if (records != null && records.size() > 0) {
                    tvNoData.setVisibility(View.GONE);
                    llTitle.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        adapter.setDatas(records);
                    }
                } else {
                    llTitle.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }


        });
    }

    private void initView() {
        setClassInfo(ConfigManager.getClassName());
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);

        listViewStatus = (ListView) findViewById(R.id.lv_status);

        adapter = new CabinetStatusAdapter(this);
        listViewStatus.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                this.finish();
                break;
            default:
                break;
        }
    }
}
