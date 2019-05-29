package com.baidu.buycar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MyCarAdapter.OnClickListener {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.all_chekbox)
    CheckBox allChekbox;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_delete)
    TextView delete;
    @BindView(R.id.tv_go_to_pay)
    TextView toPay;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.top_bar)
    Toolbar topBar;

    private List<User> usersList;
        private List<HashMap<String, String>> list = new ArrayList<>();
    private MyCarAdapter myCarAdapter;

    private double totalPrice = 0.00;
    private int totalCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initView();

    }

    private void initView() {

        topBar.setTitle("");
        setSupportActionBar(topBar);

        myCarAdapter = new MyCarAdapter(MainActivity.this, list);
        listview.setAdapter(myCarAdapter);
        myCarAdapter.setOnClickListener(this);

    }


    private void initData() {

        // 创建一个数据库对象    private List<User> usersList;
        usersList = new ArrayList<>();
        DaoUtils.getDbUtils().deleteAll();
        // 自定义数据o'i
        for (int i = 0; i < 15; i++) {
            User user = new User((long) i, "The Buy Car's " + (i + 1) + " Crab", (i + 20) + "  Content", "10", "10");
            DaoUtils.getDbUtils().insert(user);
        }
        // 加入list集合中
        usersList = DaoUtils.getDbUtils().query();
        for (int i = 0; i < usersList.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", usersList.get(i).getId() + "");
            map.put("name", usersList.get(i).getName());
            map.put("price", usersList.get(i).getPrice());
            map.put("type", usersList.get(i).getType());
            map.put("content", usersList.get(i).getContent());
            // 把这些数据存放到集合里 private List<HashMap<String, String>> list = new ArrayList<>();
            list.add(map);
        }

    }

    // 多选框 删除 以及支付按钮后的操作
    @OnClick({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all_chekbox:
                AllSelected();
                break;
            case R.id.tv_delete:
                DeleteChecked(myCarAdapter.getPitchOnMap());
                break;
            case R.id.tv_go_to_pay:
                if (totalCount >= 0) {
                    Toast.makeText(this, "Please Check your want to pay the sub", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "payFor Success", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    // 全部选中 / 取消全选
    private void AllSelected() {

        HashMap<String, Integer> map = myCarAdapter.getPitchOnMap();

        boolean isCheck = false;
        boolean isUnCheck = false;

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (Integer.valueOf(entry.getValue().toString()) == 1) {
                isCheck = true;
            } else {
                isUnCheck = true;
            }
        }

        if (isCheck == true && isUnCheck == false) {//已经全选,做反选
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).get("id"), 0);
            }
            allChekbox.setChecked(false);
        }
        else if (isCheck == true && isUnCheck == true) {//部分选择,做全选
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).get("id"), 1);
            }
            allChekbox.setChecked(true);
        }
        else if (isCheck == false && isUnCheck == true) {//一个没选,做全选
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).get("id"), 1);
            }
            allChekbox.setChecked(true);
        }
        priceControl(map);
        myCarAdapter.setPitchOnMap(map);
        myCarAdapter.notifyDataSetChanged();
    }

    private void DeleteChecked(HashMap<String, Integer> map) {
        List<HashMap<String, String>> waitDeleteList = new ArrayList<>();
        Map<String, Integer> waitDeleteMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (map.get(list.get(i).get("id")) == 1) {
                waitDeleteList.add(list.get(i));
                waitDeleteMap.put(list.get(i).get("id"), map.get(list.get(i).get("id")));
            }
        }
        list.removeAll(waitDeleteList);
        map.remove(waitDeleteMap);
        priceControl(map);
        myCarAdapter.notifyDataSetChanged();
    }

    // 适配器点击事件
    @Override
    public void onClick(HashMap<String, Integer> pitchMap) {
        priceControl(pitchMap);
    }

    private void priceControl(HashMap<String, Integer> pitchOnMap) {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < list.size(); i++) {
            if (pitchOnMap.get(list.get(i).get("id")) == 1) {
                totalCount = totalCount + Integer.valueOf(list.get(i).get("content"));
                double goodsPrice = Integer.valueOf(list.get(i).get("content")) * Double.valueOf(list.get(i).get("price"));
                totalPrice = totalPrice + goodsPrice;
            }
        }
        tvTotalPrice.setText("￥" + totalPrice);
        toPay.setText("payFor(" + totalCount + ")");
    }

}
