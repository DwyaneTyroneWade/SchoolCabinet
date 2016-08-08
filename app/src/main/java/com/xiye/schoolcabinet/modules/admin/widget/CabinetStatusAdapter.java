package com.xiye.schoolcabinet.modules.admin.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.beans.Record;
import com.xiye.sclibrary.widget.adapter.CommonBaseAdapter;

import java.util.List;

/**
 * Created by wushuang on 8/8/16.
 */
public class CabinetStatusAdapter extends CommonBaseAdapter<Record> {

    public CabinetStatusAdapter(Context context) {
        super(context);
    }

    public CabinetStatusAdapter(Context context, List _datas) {
        super(context, _datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = null;
        if (v == null) {
            v = inflater.inflate(R.layout.item_status_list, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.fillData(position);
        return v;
    }

    public class ViewHolder {
        public TextView tvCardId;
        public TextView tvBoxId;
        public TextView tvBoxDoorStatus;
        public TextView tvOperationTime;

        public ViewHolder(View v) {
            tvCardId = (TextView) v.findViewById(R.id.tv_card_id);
            tvBoxId = (TextView) v.findViewById(R.id.tv_box_id);
            tvBoxDoorStatus = (TextView) v.findViewById(R.id.tv_box_door_status);
            tvOperationTime = (TextView) v.findViewById(R.id.tv_operation_time);
        }

        public void fillData(int position) {
            tvCardId.setText(getItem(position).cardId);
            tvBoxId.setText(getItem(position).boxId);
            int status = Integer.parseInt(getItem(position).boxDoorStatus);
            switch (status) {
                case 0:
                    tvBoxDoorStatus.setText(context.getString(R.string.box_door_status_close));
                    break;
                case 1:
                    tvBoxDoorStatus.setText(context.getString(R.string.box_door_status_open));
                    break;
                case 2:
                    tvBoxDoorStatus.setText(context.getString(R.string.box_door_status_damage));
                default:
                    break;
            }

            tvOperationTime.setText(getItem(position).operationTime);
        }
    }
}
