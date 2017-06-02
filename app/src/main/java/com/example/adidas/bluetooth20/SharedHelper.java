package com.example.adidas.bluetooth20;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adidas on 2017/6/2.
 */

public class SharedHelper {

    private Context mContext;

    public SharedHelper(){};

    public SharedHelper(Context context){
        this.mContext=context;
    }

    public void save(ArrayList<String> saveData) {

        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("edit1", saveData.get(0));
        editor.putString("edit2", saveData.get(1));
        editor.putString("edit3", saveData.get(2));
        editor.putString("edit4", saveData.get(3));
        editor.putString("edit5", saveData.get(4));
        editor.putString("edit6", saveData.get(5));
        editor.putString("spin1", saveData.get(6));
        editor.putString("spin2", saveData.get(7));
        editor.putString("spin3", saveData.get(8));
        editor.commit();
        Toast.makeText(mContext, "信息已写入SharedPreference中", Toast.LENGTH_SHORT).show();
    }

    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
        data.put("spin1", sp.getString("spin1", "0"));
        data.put("spin2", sp.getString("spin2", "0"));
        data.put("spin3", sp.getString("spin3", "0"));
        data.put("edit1", sp.getString("edit1", "1"));
        data.put("edit2", sp.getString("edit2", "50"));
        data.put("edit3", sp.getString("edit3", "300"));
        data.put("edit4", sp.getString("edit4", "30"));
        data.put("edit5", sp.getString("edit5", "80"));
        data.put("edit6", sp.getString("edit6", "5"));
        return data;
    }


}
