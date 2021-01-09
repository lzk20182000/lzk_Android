package com.jnu.lzk_android;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.jnu.lzk_android.Dataprocess.DateBank;
import com.jnu.lzk_android.Dataprocess.GIft;

import java.util.ArrayList;


public class PutGiftFragment extends Fragment {
    public static GiftAdapter adapter_Putgift;
    public static DateBank dataBank_Putgift;
    private static ArrayList<GIft> putGift;
    //按钮
    private FloatingActionMenu buttonMenu;
    private FloatingActionButton bottonOff;
    private FloatingActionButton bottonGet;


    public PutGiftFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_put_gift, container, false);
        initData();
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
    private void initData() {
        if(dataBank_Putgift == null){
            dataBank_Putgift =new DateBank(this.getContext());
        }
        dataBank_Putgift.Load();
    }
    private void initView(View view) {
        getPutGift();
        adapter_Putgift = new GiftAdapter(this.getContext(), R.layout.item_gift,  putGift);

        ListView listViewBooks=view.findViewById(R.id.listview_putgift);
        listViewBooks.setAdapter(adapter_Putgift);
    }

    //刷新界面
    @Override
    public void onResume() {
        super.onResume();
        adapter_Putgift.notifyDataSetChanged();
    }

    //获取在databank中存取的数据
    public static void getPutGift(){
//        dataBank_Putgift.Load();
//        int length = dataBank_Putgift.getGifts().size();
//        int Type = -1;
        int Type ;
        dataBank_Putgift.Load();
        if (putGift == null)
            putGift = new ArrayList<GIft>();
        else
            putGift.clear();
        for(int i = 0; i < dataBank_Putgift.getGifts().size(); i++){
            //获取databank中类型
            Type = dataBank_Putgift.getGifts().get(i).getType();
            //类型为0是随礼
            if(Type == 0)
                putGift.add(dataBank_Putgift.getGifts().get(i));
        }
    }
    //数据绑定
    private static class GiftAdapter extends ArrayAdapter<GIft> {
        private int resourceId;

        public GiftAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GIft> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        //获取界面
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            GIft gIft = getItem(position);
            View view;
            if(null == convertView){
                view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);
            }else{
                view=convertView;
            }
            ((TextView)view.findViewById((R.id.gift_people))).setText(gIft.getTextName());
            ((TextView)view.findViewById((R.id.gift_time))).setText(gIft.getTime());
            ((TextView)view.findViewById((R.id.gift_reason))).setText(gIft.getTextReason());
            return view;
        }
    }


}