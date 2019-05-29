package com.baidu.buycar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

public class MyCarAdapter extends BaseAdapter {

    private final Context context;
    private final List<HashMap<String, String>> list;
    private HashMap<String, Integer> pitchOnMap;

    public HashMap<String, Integer> getPitchOnMap() {
        return pitchOnMap;
    }

    public void setPitchOnMap(HashMap<String, Integer> pitchOnMap) {
        this.pitchOnMap = pitchOnMap;
    }

    public MyCarAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;

        pitchOnMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            pitchOnMap.put(list.get(i).get("id"), 0);
        }

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_layout, null);
        final CheckBox checkBox;
        ImageView icon;
        final TextView name, price, num, type, down, add;

        checkBox = convertView.findViewById(R.id.check_box);

        icon = convertView.findViewById(R.id.iv_adapter_list_pic);
        RoundedCorners roundedCorners = new RoundedCorners(20);
        RequestOptions requestOptions = RequestOptions
                .bitmapTransform(roundedCorners)
                .circleCrop();
        Glide.with(context).load(R.drawable.timg).apply(requestOptions).into(icon);

        name = convertView.findViewById(R.id.tv_goods_name);
        name.setText(list.get(position).get("name"));

        price = convertView.findViewById(R.id.tv_goods_price);
        price.setText("￥ 10");

        type = convertView.findViewById(R.id.tv_type_size);
        type.setText(list.get(position).get("type"));

        num = convertView.findViewById(R.id.tv_num);
        num.setText(list.get(position).get("content"));

        if (pitchOnMap.get(list.get(position).get("id")) == 0) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }

        // 判断CheckBox选中状态及选中未选中的执行后果
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    pitchOnMap.put(list.get(position).get("id"), 1);
                } else {
                    pitchOnMap.put(list.get(position).get("id"), 0);
                }
                mOnClickListener.onClick(pitchOnMap);
            }
        });

        // 点击增加按钮
        add = convertView.findViewById(R.id.tv_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).put("content", (Integer.valueOf(list.get(position).get("content")) + 1) + "");
                notifyDataSetChanged();
                mOnClickListener.onClick(pitchOnMap);
            }
        });

        // 点击减少按钮
        down = convertView.findViewById(R.id.tv_reduce);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.valueOf(list.get(position).get("content")) <= 0) {
                    Toast.makeText(context, " Don't more lost ", Toast.LENGTH_SHORT).show();
                } else {
                    list.get(position).put("content", (Integer.valueOf(list.get(position).get("content")) - 1) + "");
                    notifyDataSetChanged();
                }
                mOnClickListener.onClick(pitchOnMap);
            }
        });

        return convertView;
    }


    // 创建一个接口
    public interface OnClickListener {
        // 把价格展示到总价上
        void onClick(HashMap<String, Integer> pitchMap);
    }

    // 定义一个接口对象
    private OnClickListener mOnClickListener;

    // 给外部一个方法 展示总价
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
