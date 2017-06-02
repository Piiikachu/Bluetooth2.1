package com.example.adidas.bluetooth20;

import java.util.ArrayList;

/**
 * Created by Adidas on 2017/5/15.
 */


public class DataCollection {
    private float[] floatData;
    private int[] status;
    private String result;
    ArrayList<String> collect;

    public DataCollection(float[] floatData,int[] status,String result){
        this.floatData=new float[6];
        this.status=new int[3];

        if (floatData.length>6)
            return;
        if (status.length>3)
            return;

        this.floatData=floatData;
        this.status=status;
        this.result=result;


        this.collect=new ArrayList<>();
        collect=collectData(floatData,status,result);
    }

    public ArrayList<String> collectData(float[] floatData,int[] status,String result){
        if (floatData.length>6)
            return null;
        if (status.length>3)
            return null;

        for (int i=0;i<6;i++){
            collect.add(String.valueOf(floatData[i]));
        }

        for (int i=0;i<3;i++){
            collect.add(String.valueOf(status[i]));
        }

        collect.add(result);

        return collect;
    }

    public ArrayList<String> updateData(float[] floatData){
        if (floatData.length>6)
            return null;
        for (int i=0;i<6;i++){
            collect.set(i,String.valueOf(floatData[i]));
        }
        return collect;
    }
    public ArrayList<String> updateData(int[] status){
        if (status.length>3)
            return null;
        for (int i=0;i<3;i++){
            collect.set(i+6,String.valueOf(status[i]));
        }
        return collect;
    }

    public ArrayList<String> updateData(String string){
        collect.set(9,string);

        return collect;
    }

    public String toString(){
        String str=new String();
        str="";
        for (int i=0;i<collect.size();i++){
            str=str+collect.get(i)+",";
        }
        return str;
    }

}

