package com.example.adidas.bluetooth20;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Adidas on 2017/5/13.
 */

public class FragmentGet extends Fragment {

    private ListView listView;
    private GetListAdapter mAdapter;
    private TextView textView;


    private float[] data;
    private int orderPosition;
    private LinkedList<String> strData;
    public ArrayList<String> collectData;
    private MainActivity activity;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentGet() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentGet newInstance(int sectionNumber) {
        FragmentGet fragment = new FragmentGet();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_get, container, false);

        activity= (MainActivity) getActivity();




        listView= (ListView) rootView.findViewById(R.id.fragget_listview);
        data=new float[5];
        data[0]=0.0f;
        data[1]=0.0f;
        data[2]=0.0f;
        data[3]=0.0f;
        data[4]=0.0f;


        mAdapter=new GetListAdapter(getActivity(),data);



        listView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.setOnReceiveMessage(new MainActivity.OnReceiveMessage() {
            @Override
            public void readMsg(String msg) {
                analysMessage(msg);
                mAdapter.updateData(data);

            }
        });
    }

    public void analysMessage(String message){
        String[] strs=new String[]{};
        strs=message.split(",");
        if (collectData==null){
        this.collectData=new ArrayList<>();
            for (int i=0;i<6;i++)
            collectData.add(strs[i]);
        }else {
            for (int i=0;i<6;i++)
                collectData.set(i,strs[i]);
        }

        data[0]=Float.valueOf(collectData.get(0));
        data[2]=Float.valueOf(collectData.get(1));
        data[3]=Float.valueOf(collectData.get(2));
        data[4]=Float.valueOf(collectData.get(3));
        data[1]=Float.valueOf(collectData.get(4));

        FragmentSend.orderPosition=Integer.valueOf(collectData.get(5));


    }

    public interface OnOrderReceived{
        void receiveOrder();
    }

    public OnOrderReceived onOrderReceived;

    public void setOnOrderReceived(OnOrderReceived onOrderReceived){
        this.onOrderReceived=onOrderReceived;
    }



}
