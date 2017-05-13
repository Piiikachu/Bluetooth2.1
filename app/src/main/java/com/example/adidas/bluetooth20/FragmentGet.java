package com.example.adidas.bluetooth20;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Adidas on 2017/5/13.
 */

public class FragmentGet extends Fragment {

    private ListView listView;
    private GetListAdapter mAdapter;
    private TextView textView;

    private Button btnTest;
    private float[] data;
    private LinkedList<String> strData;


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

        MainActivity activity= (MainActivity) getActivity();


        textView = (TextView) rootView.findViewById(R.id.fragget_text);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        activity.setOnReceiveMessage(new MainActivity.OnReceiveMessage() {
            @Override
            public void readMsg(String msg) {
                textView.setText(msg);
            }
        });
        btnTest= (Button) rootView.findViewById(R.id.fragget_btn);
        listView= (ListView) rootView.findViewById(R.id.fragget_listview);
        data=new float[5];
        data[0]=0.0f;
        data[1]=0.0f;
        data[2]=0.0f;
        data[3]=0.0f;
        data[4]=0.0f;


        mAdapter=new GetListAdapter(getActivity(),data);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data[0]=1.0f;
                data[1]=2.0f;
                data[2]=3.0f;
                data[3]=4.0f;
                data[4]=0.5f;
                mAdapter.updateData(data);
            }
        });


        listView.setAdapter(mAdapter);


        return rootView;
    }



}
