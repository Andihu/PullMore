package com.example.hujian.pullmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * <p>文件描述：<p>
 * <p>作者：hujian<p>
 * <p>创建时间：2018/10/18/018<p>
 * <p>更改时间：2018/10/18/018<p>
 * <p>版本号：1<p>
 */
public class GridviewAdapter extends BaseAdapter {
    private List<QuickAppBean> beans;
    private Context context;
    private int curposition;
    private int leftcount;

    public GridviewAdapter(List<QuickAppBean> beans, Context context, int curposition) {
        this.beans = beans;
        this.context = context;
        this.curposition =curposition;
        leftcount=beans.size()-curposition;
    }

    @Override
    public int getCount() {
        if (leftcount<4){
            return leftcount;
        }else{
            return 4;
        }
    }

    @Override
    public Object getItem(int position) {

            return beans.get(position+ curposition);

    }

    @Override
    public long getItemId(int position) {

            return position+curposition;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder viewHoder;

        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_headerview_icon,parent,false);
            viewHoder=new ViewHoder();
            viewHoder.appicon=convertView.findViewById(R.id.appicon);
            viewHoder.appname=convertView.findViewById(R.id.appname);
            convertView.setTag(viewHoder);
        }
        else {
            viewHoder= (ViewHoder) convertView.getTag();
        }

            viewHoder.appname.setText(beans.get(position+curposition).getAppname());
        return convertView;
    }
    class ViewHoder {
        TextView appname;
        ImageView appicon;
    }
}
