package com.example.adidas.bluetooth20;

import android.content.Intent;
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

public class FragmentBluetooth extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    private Button btn_search;
    private ListView listView;
    private GetListAdapter mAdapter;

    private float[] data;
    private LinkedList<String> strData;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public FragmentBluetooth() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentBluetooth newInstance(int sectionNumber) {
        FragmentBluetooth fragment = new FragmentBluetooth();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        btn_search= (Button) rootView.findViewById(R.id.fragmain_btn);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getActivity(),DeviceActivity.class);
                getActivity().startActivityForResult(intent1,REQUEST_CONNECT_DEVICE_SECURE);
            }
        });


        return rootView;
    }



}
