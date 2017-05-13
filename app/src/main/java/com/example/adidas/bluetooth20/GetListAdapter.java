package com.example.adidas.bluetooth20;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;
import java.util.zip.Inflater;

/**
 * Created by Adidas on 2017/5/13.
 */

public class GetListAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<GetData> mData;

    private LinkedList<String> strData;

    String[] name={"电机转速（%）","电机测温","换向阀","尿素泵","喷嘴脉宽（%）"};

    public GetListAdapter(Context context,float[] data){
        this.mContext=context;

        mData=new LinkedList<>();
        strData=new LinkedList<>();
        initData(data);



    }

    public void initData(float[] data){
        strData=convertToString(data);
        for (int i=0;i<5;i++){
            GetData getData=new GetData();
            getData.setName(name[i]);
            getData.setContent(strData.get(i));
            mData.add(getData);

        }
        notifyDataSetChanged();
    }

    public void updateData(float[] data){
        strData=convertToString(data);
        for (int i=0;i<5;i++){
            GetData getData=new GetData();
            getData.setName(name[i]);
            getData.setContent(strData.get(i));
            mData.set(i,getData);

        }
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.list_item,viewGroup,false);
            holder=new ViewHolder();
            holder.textName= (TextView) view.findViewById(R.id.fragget_list_text_name);
            holder.textContent= (TextView) view.findViewById(R.id.fragget_list_text_data);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.textContent.setText(mData.get(i).getContent());
        holder.textName.setText(mData.get(i).getName());

        return view;
    }


    private class GetData {
        private String name;
        private String content;
        public GetData(){}

        public GetData(String name,String content){
            this.name=name;
            this.content=content;
        }

        public String getContent() {
            return content;
        }

        public String getName() {
            return name;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class ViewHolder{
        TextView textName;
        TextView textContent;

    }

    public LinkedList<String> convertToString(float[] data){
        if (data.length!=5)
        return null;

        float tmpdata;
        String[] tempStr=new String[5];
        for (int i=0;i<5;i++){
            tmpdata=data[i];
            switch (i){
                case 0:
                    if (tmpdata==0)
                        tempStr[i]="电机关闭";
                    else
                        tempStr[i]=String.valueOf(data[i]);
                    break;
                case 1:
                    if (tmpdata==0)
                        tempStr[i]="关闭";
                    else
                        tempStr[i]="打开";
                    break;
                case 2:
                    if (tmpdata==0)
                        tempStr[i]="反向";
                    else
                        tempStr[i]="正向";
                    break;
                case 3:
                    if (tmpdata==0)
                        tempStr[i]="加热关闭";
                    else
                        tempStr[i]="加热打开";
                    break;
                case 4:
                    if (tmpdata==0)
                        tempStr[i]="喷嘴关闭";
                    else
                        tempStr[i]=String.valueOf(data[i]);
                    break;



            }

        }
        LinkedList<String> list=new LinkedList<>();
        for(String str:tempStr){
            list.add(str);
        }
        return list;

    }

}
