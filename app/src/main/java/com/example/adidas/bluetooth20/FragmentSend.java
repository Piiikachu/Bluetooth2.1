package com.example.adidas.bluetooth20;

import android.os.Bundle;
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

import java.util.ArrayList;

/**
 * Created by Adidas on 2017/5/13.
 */

public class FragmentSend extends Fragment {

    private EditText edit1,edit2,edit3,edit4,edit5,edit6,edit7;
    private TextView textView,textOrder;
    private Button btnSingle,btnConsist,btnResultOK,btnResultWrong;
    private Spinner spin1,spin2,spin3;

    private float[] floatData;
    private int[] status;
    private String result;
    private String testing;
    public static int orderPosition=0;

    MainActivity activity;
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
        result="未接收测试";
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

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
                }
                textOrder.setText(analyseOrder(orderPosition)+"进行中，模拟返回测试结果");
            }
        });


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
                    result=analyseOrder(orderPosition)+"完毕，测试成功";

                    btnResultOK.setEnabled(false);
                    btnResultWrong.setEnabled(false);
                    break;
                case R.id.fragsend_btnWrong:
                    result=analyseOrder(orderPosition)+"完毕，测试失败";
                    btnResultOK.setEnabled(false);
                    btnResultWrong.setEnabled(false);
                    break;
            }
            if (collection==null){
                collection=new DataCollection(floatData,status,result);
            }else {
                collection.updateData(result);
            }
            String message=collection.toString();
            activity.sendMessage(message);
            orderPosition=0;
            result=analyseOrder(orderPosition);
            textOrder.setText(result);
        }

    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void bindView(View rootView){
        textView = (TextView) rootView.findViewById(R.id.fragsend_text);
        textOrder= (TextView) rootView.findViewById(R.id.fragsend_text_order);
        edit1= (EditText) rootView.findViewById(R.id.fragsend_edit1);
        edit2= (EditText) rootView.findViewById(R.id.fragsend_edit2);
        edit3= (EditText) rootView.findViewById(R.id.fragsend_edit3);
        edit4= (EditText) rootView.findViewById(R.id.fragsend_edit4);
        edit5= (EditText) rootView.findViewById(R.id.fragsend_edit5);
        edit6= (EditText) rootView.findViewById(R.id.fragsend_edit6);
        edit7= (EditText) rootView.findViewById(R.id.fragsend_edit7);
        btnSingle= (Button) rootView.findViewById(R.id.fragsend_btn_single_send);
        btnConsist= (Button) rootView.findViewById(R.id.fragsend_btn_consist_send);
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
        float[] floatData=new float[7];
        floatData[0]=Float.valueOf(edit1.getText().toString());
        floatData[1]=Float.valueOf(edit2.getText().toString());
        floatData[2]=Float.valueOf(edit3.getText().toString());
        floatData[3]=Float.valueOf(edit4.getText().toString());
        floatData[4]=Float.valueOf(edit5.getText().toString());
        floatData[5]=Float.valueOf(edit6.getText().toString());
        floatData[6]=Float.valueOf(edit7.getText().toString());

        return floatData;
    }

    public String analyseOrder(int orderPostion){
        switch (orderPostion){
            case 0:
                testing="未接收到测试指令";
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





}
