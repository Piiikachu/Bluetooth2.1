package com.example.adidas.bluetooth20;

import android.content.Context;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adidas.bluetooth20.Bluetooth.Constants;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Adidas on 2017/5/13.
 */

public class FragmentSend extends Fragment implements Constants{

    private EditText edit1,edit2,edit3,edit4,edit5,edit6,edit7;
    private TextView textView,textOrder;
    private Button btnSingle,btnResultOK,btnResultWrong;
    private Spinner spin1,spin2,spin3;

    private Context mContext;
    private float[] floatData;
    private int[] status;
    private String result;
    private String testing;
    public static int orderPosition=0;

    MainActivity activity;
    private SharedHelper sh;
    private DataCollection collection;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentSend() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentSend newInstance(int sectionNumber) {
        FragmentSend fragment = new FragmentSend();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send, container, false);
        bindView(rootView);
        activity= (MainActivity) getActivity();
        result=" ";

        floatData=getFloatData();

        status=new int[]{0,0,0};
        Spinner.OnItemSelectedListener listener=new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status[(int) adapterView.getTag()-1]=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        spin1.setOnItemSelectedListener(listener);
        spin2.setOnItemSelectedListener(listener);
        spin3.setOnItemSelectedListener(listener);

        activity.setOnReceiveOrder(new MainActivity.OnReceiveOrder() {
            @Override
            public void onOrder(String msg) {

            }
        });


        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                floatData=getFloatData();


                analyseOrder(orderPosition);
                if (collection==null){
                collection=new DataCollection(floatData,status,result);
                }else {
                    collection.updateData(floatData);
                    collection.updateData(status);
                    collection.updateData(result);
                }
                if (activity.state!= STATE_CONNECTED){
                    Toast.makeText(getActivity(),"未连接蓝牙设备",Toast.LENGTH_SHORT).show();
                    return;
                }
                String message=collection.toString();
                activity.sendMessage(message);
            }
        });

        btnResultOK.setOnClickListener(btnListener);
        btnResultOK.setEnabled(false);
        btnResultWrong.setOnClickListener(btnListener);
        btnResultWrong.setEnabled(false);


        activity.fragmentGet.setOnOrderReceived(new FragmentGet.OnOrderReceived() {
            @Override
            public void receiveOrder() {
                if (orderPosition!=0){
                    btnResultWrong.setEnabled(true);
                    btnResultOK.setEnabled(true);
                    if (orderPosition==6){
                        btnResultWrong.setEnabled(false);
                        btnResultOK.setEnabled(false);
                        textOrder.setText("测试被终止！");
                        return;
                    }
                }
                textOrder.setText(analyseOrder(orderPosition)+"进行中，模拟返回测试结果");
                if (Float.valueOf(activity.fragmentGet.collectData.get(3))!=0f)
                edit2.setText(activity.fragmentGet.collectData.get(3));
                if (Float.valueOf(activity.fragmentGet.collectData.get(0))!=0f)
                edit5.setText(activity.fragmentGet.collectData.get(0));
            }
        });

        mContext=getActivity().getApplicationContext();
        sh=new SharedHelper(mContext);


        return rootView;
    }
    Button.OnClickListener btnListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (orderPosition==0){
                textOrder.setText(analyseOrder(orderPosition));
                return;
            }
            switch (view.getId()){
                case R.id.fragsend_btnOK:
                    result=analyseOrder(orderPosition)+"完毕，测试结果正常";

                    btnResultOK.setEnabled(false);
                    btnResultWrong.setEnabled(false);
                    break;
                case R.id.fragsend_btnWrong:
                    result=analyseOrder(orderPosition)+"完毕，测试结果异常";
                    btnResultOK.setEnabled(false);
                    btnResultWrong.setEnabled(false);
                    break;
            }
            if (collection==null){
                floatData=getFloatData();
                collection=new DataCollection(floatData,status,result);
            }else {
                collection.updateData(result);
            }
            String message=collection.toString();
            activity.sendMessage(message);
            orderPosition=0;
            result=analyseOrder(orderPosition);
            textOrder.setText("未接收到测试指令");
        }

    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onStart() {
        super.onStart();
        Map<String,String> data = sh.read();
        edit1.setText(data.get("edit1"));
        edit2.setText(data.get("edit2"));
        edit3.setText(data.get("edit3"));
        edit4.setText(data.get("edit4"));
        edit5.setText(data.get("edit5"));
        edit6.setText(data.get("edit6"));
        status[0]=Integer.valueOf(data.get("spin1"));
        status[1]=Integer.valueOf(data.get("spin2"));
        status[2]=Integer.valueOf(data.get("spin3"));
        spin1.setSelection(status[0]);
        spin2.setSelection(status[1]);
        spin3.setSelection(status[2]);

    }

    public void bindView(View rootView){

        textOrder= (TextView) rootView.findViewById(R.id.fragsend_text_order);
        edit1= (EditText) rootView.findViewById(R.id.fragsend_edit1);
        edit2= (EditText) rootView.findViewById(R.id.fragsend_edit2);
        edit3= (EditText) rootView.findViewById(R.id.fragsend_edit3);
        edit4= (EditText) rootView.findViewById(R.id.fragsend_edit4);
        edit5= (EditText) rootView.findViewById(R.id.fragsend_edit5);
        edit6= (EditText) rootView.findViewById(R.id.fragsend_edit6);
        btnSingle= (Button) rootView.findViewById(R.id.fragsend_btn_single_send);
        btnResultOK= (Button) rootView.findViewById(R.id.fragsend_btnOK);
        btnResultWrong= (Button) rootView.findViewById(R.id.fragsend_btnWrong);
        spin1= (Spinner) rootView.findViewById(R.id.fragsend_spin1);
        spin1.setTag(1);
        spin2= (Spinner) rootView.findViewById(R.id.fragsend_spin2);
        spin2.setTag(2);
        spin3= (Spinner) rootView.findViewById(R.id.fragsend_spin3);
        spin3.setTag(3);



    }

    public float[] getFloatData(){
        float[] floatData=new float[6];
        floatData[0]=Float.valueOf(edit1.getText().toString());
        floatData[1]=Float.valueOf(edit2.getText().toString());
        floatData[2]=Float.valueOf(edit3.getText().toString());
        floatData[3]=Float.valueOf(edit4.getText().toString());
        floatData[4]=Float.valueOf(edit5.getText().toString());
        floatData[5]=Float.valueOf(edit6.getText().toString());

        return floatData;
    }

    public String analyseOrder(int orderPostion){
        switch (orderPostion){
            case 0:
                testing=" ";
                break;
            case 1:
                testing="密封测试";
                break;
            case 2:
                testing="小流量测试";
                break;
            case 3:
                testing="大流量测试";
                break;
            case 4:
                testing="动态测试";
                break;
            case 5:
                testing="排空";
                break;
            case 6:
                testing="停止";
                break;

        }
        return testing;
    }


    public void saveSH() {
        floatData=getFloatData();
        if (collection==null)
        {
            collection=new DataCollection(floatData,status,result);
        }
        collection.updateData(floatData);
        collection.updateData(status);
        sh.save(collection.collect);

    }


}
