package com.jnu.lzk_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jnu.lzk_android.Dataprocess.DateBank;
import com.jnu.lzk_android.Dataprocess.GIft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class GetGiftFragment extends Fragment {

    public static DateBank dataBank_Getgift;
    private ExpandableListView expand_list_id;
    private static ArrayList<GIft> getGift=new ArrayList<>();
    private static ArrayList<String> giftFather = new ArrayList<>();
    private static ArrayList<ArrayList<GIft>> giftSon=new ArrayList<>();
    public static ExpandableListviewAdapter adapterGet;
    //按钮
    private FloatingActionMenu buttonMenu;
    private FloatingActionButton bottonOff;
    private FloatingActionButton bottonGet;

    public GetGiftFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_get_gift, container, false);
        initView(view);
        buttonMenu=getActivity().findViewById(R.id.button_menu);
        bottonOff=getActivity().findViewById(R.id.gift_off);
        bottonGet=getActivity().findViewById(R.id.gift_get);

        bottonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"请在主页添加随礼记录",Toast.LENGTH_SHORT).show();
            }
        });
        bottonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"请在主页添加收礼记录",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    //根据gift的收礼原因进行分类
    private ArrayList<String> giftSort(ArrayList<GIft> gIfts){
        dataBank_Getgift.Save();
        ArrayList<String> str = new ArrayList<>();
        HashMap<String,Integer> map = new HashMap<>();
        for(int i=0;i<dataBank_Getgift.getGifts().size();i++){
            int type = dataBank_Getgift.getGifts().get(i).getType();
            if(type == 1) {
                if (!map.containsKey(gIfts.get(i).getTextReason())) {
                    str.add(gIfts.get(i).getTextReason());
                    map.put((gIfts.get(i).getTextReason()), 0);
                }
            }
        }
        return str;
    }

    //初始化子层数据
    private void addGift(){
        if(dataBank_Getgift == null){
            dataBank_Getgift =new DateBank(this.getContext());
        }
        dataBank_Getgift.Load();

        getGift = dataBank_Getgift.getGifts();
        if(getGift.size() == 0) {
            giftFather.clear();
            giftSon.clear();
            giftFather.add("暂无收礼记录");
        }
        else
            giftFather=giftSort(getGift);

        giftSon.clear();
        for(int i=0;i<giftFather.size();i++){
            ArrayList<GIft> string = new ArrayList<>();
            for(int j=0;j<getGift.size();j++){
                if(giftFather.get(i).equals(getGift.get(j).getTextReason())){
                    int type = dataBank_Getgift.getGifts().get(j).getType();
                    if(type == 1)
                        string.add(getGift.get(j));
                }
            }
            giftSon.add(string);
        }
    }
    private void initView(View view) {
        addGift();
        expand_list_id=view.findViewById(R.id.expand_list_id);
        adapterGet=new ExpandableListviewAdapter(this.getContext(),giftFather,giftSon);
        expand_list_id.setAdapter(adapterGet);
//        //默认展开第一个数组
//        expand_list_id.expandGroup(0);
        //关闭数组某个数组，可以通过该属性来实现全部展开和只展开一个列表功能
        //expand_list_id.collapseGroup(0);
        expand_list_id.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {

                return false;
            }
        });
        //子视图的点击事件
        expand_list_id.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                return true;
            }
        });
        //用于当组项折叠时的通知。
        expand_list_id.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        //
        //用于当组项折叠时的通知。
        expand_list_id.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
    }

    public static  void notifiy(){
        if(adapterGet != null)
            adapterGet.notifyDataSetChanged();
    }
}