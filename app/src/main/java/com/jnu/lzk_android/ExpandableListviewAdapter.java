package com.jnu.lzk_android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jnu.lzk_android.Dataprocess.GIft;

import java.util.ArrayList;

public class ExpandableListviewAdapter extends BaseExpandableListAdapter {
    //Model：定义的数据
    public static ArrayList<String> giftFather;
    public static ArrayList<ArrayList<GIft>> giftChilds;
//    private String[] giftFather;
//    //注意，字符数组不要写成{{"A1,A2,A3,A4"}, {"B1,B2,B3,B4，B5"}, {"C1,C2,C3,C4"}}
//    private String[][] giftChilds;

    private Context context;

    public ExpandableListviewAdapter(Context context,ArrayList<String> giftFather, ArrayList<ArrayList<GIft>> giftChilds){
        this.context=context;
        this.giftFather = giftFather;
        this.giftChilds = giftChilds;
    }

    @Override
    public int getGroupCount() {
        return giftFather.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return giftChilds.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return giftFather.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return giftChilds.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablelist_father,parent,false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.parent_textview_id = convertView.findViewById(R.id.parent_textview_id);
            groupViewHolder.parent_image = convertView.findViewById(R.id.parent_image);
            convertView.setTag(groupViewHolder);
        }else {
            groupViewHolder = (GroupViewHolder)convertView.getTag();
        }
        groupViewHolder.parent_textview_id.setText(giftFather.get(groupPosition));
        //如果是展开状态，
        if (isExpanded){
            groupViewHolder.parent_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_item_down));
        }else{
            groupViewHolder.parent_image.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_item_right));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablelist_son,parent,false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.chidren_item = (TextView)convertView.findViewById(R.id.chidren_item);
            childViewHolder.chidren_item1 = (TextView)convertView.findViewById(R.id.chidren_item1);
            childViewHolder.chidren_item2 = (TextView)convertView.findViewById(R.id.chidren_item2);
            convertView.setTag(childViewHolder);

        }else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.chidren_item.setText(giftChilds.get(groupPosition).get(childPosition).getTextName());
        childViewHolder.chidren_item1.setText(giftChilds.get(groupPosition).get(childPosition).getTextReason());
        childViewHolder.chidren_item2.setText(giftChilds.get(groupPosition).get(childPosition).getTime());
        return convertView;
    }

    //指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    static class GroupViewHolder {
        TextView parent_textview_id;
        ImageView parent_image;
    }

    static class ChildViewHolder {
        TextView chidren_item;
        TextView chidren_item1;
        TextView chidren_item2;
    }
}
