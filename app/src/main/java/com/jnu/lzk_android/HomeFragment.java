package com.jnu.lzk_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import com.jnu.lzk_android.Dataprocess.DateBank;
import com.jnu.lzk_android.Dataprocess.GIft;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
    public static  GiftAdapter adapter;
    public static DateBank dateBank;
    public static ArrayList<GIft> giftListHome;
    //获取日历当天的日期
    public static int year1,month1,dayOfMonth1;
    //按钮
    private FloatingActionMenu buttonMenu;
    private FloatingActionButton bottonOff;
    private FloatingActionButton bottonGet;
    private Button buttonUpdate;
    private String Name;
    private String Money;
    private String Reason;
    //菜单的操作
    private static final int CONTEXT_MENU_ITEM_UPDATE = 1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE+1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home, container, false);
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        Calendar cal = Calendar.getInstance();
        year1 = cal.get(Calendar.YEAR);
        month1 = (cal.get(Calendar.MONTH))+1;
        dayOfMonth1 =  cal.get(Calendar.DAY_OF_MONTH);

        //获取日历的年月日
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                year1 = year;month1 = month + 1;dayOfMonth1 = dayOfMonth;
                String time_Now = year1 + "年" + month1 + "月" + dayOfMonth1 + "日" ;
                //initView(view);   //在这个位置出现问题，因为在这里调用的话点击其他日期view会发生冲突，直接程序崩溃
                changeTime(time_Now);//加一个判断listview的函数
                adapter.notifyDataSetChanged();
            }
        });
        initData();
        initView(view);
        ListView listViewGift=view.findViewById(R.id.listviewhome);
        //对listview显示上下文菜单
        this.registerForContextMenu(listViewGift);

        buttonMenu=getActivity().findViewById(R.id.button_menu);
        bottonOff=getActivity().findViewById(R.id.gift_off);
        bottonGet=getActivity().findViewById(R.id.gift_get);

        bottonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_put();//弹出随礼的添加数据的对话框
            }
        });
        bottonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                New_get();//弹出收礼的添加数据的对话框
            }
        });
        return view;
    }
    private void initData() {
        if(dateBank == null){
            dateBank=new DateBank(this.getContext());
        }
        dateBank.Load();
    }
    private void initView(View view) {
        String time_Now = year1 + "年" + month1 + "月" + dayOfMonth1 + "日" ;
        //在切换日期的时候，下面的listview需要更新，判断是否为空以及刷新
        changeTime(time_Now);
        //在listview列表中添加数据
        adapter = new GiftAdapter(this.getContext(), R.layout.item_gift, giftListHome);

        ListView listViewGifts=view.findViewById(R.id.listviewhome);
        //利用适配器添加
        listViewGifts.setAdapter(adapter);
    }
    //该函数判断切换日期时是否为空以及刷新
    public void changeTime(String time_Now)
    {
        if(giftListHome == null)
            giftListHome = new ArrayList<>();
        else
            giftListHome.clear();

        for(int i = 0; i < dateBank.getGifts().size(); i++){
            String time;
            time = dateBank.getGifts().get(i).getTime();
            //判断时间与现在相等
            if(time.equals(time_Now)) {
                giftListHome.add(dateBank.getGifts().get(i));
            }
        }
    }
    private static EditText et_name;
    private static EditText et_money;
    private static EditText et_reason;
    //随礼按钮
    public void New_put()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("添加一条随礼记录");
        //通过布局填充器获自定义的布局文件input_update
        View view = getLayoutInflater().inflate(R.layout.input_update,null);
        //获取两个文本编辑框
        et_name = (EditText) view.findViewById(R.id.et_name);//送礼人
        et_money = (EditText) view.findViewById(R.id.et_money);//金额
        et_reason = (EditText) view.findViewById(R.id.et_reason);//送礼原因
        et_reason.setText("新婚大吉");
        builder.setView(view);//设置input_update为对话提示框
        builder.setCancelable(true);//设置为可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = et_name.getText().toString().trim();
                String money = et_money.getText().toString().trim();
                String reason = et_reason.getText().toString().trim();
                newPut(name,money,reason);//输入完数据之后显示出随礼记录
                Toast.makeText(getActivity(),"送礼人"+name + "正在添加....",Toast.LENGTH_SHORT).show();
            }
        });
        //设置反面按钮，并做事件处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"取消添加",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();//显示Dialog对话框
    }

    //收礼按钮
    public void New_get()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("添加一条收礼记录");
        //通过布局填充器获自定义的布局文件input_update
        View view = getLayoutInflater().inflate(R.layout.input_update,null);
        //获取两个文本编辑框
        et_name = (EditText) view.findViewById(R.id.et_name);//送礼人
        et_money = (EditText) view.findViewById(R.id.et_money);//金额
        et_reason = (EditText) view.findViewById(R.id.et_reason);//送礼原因
        et_reason.setText("新婚大吉");
        builder.setView(view);//设置input_update为对话提示框
        builder.setCancelable(true);//设置为可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = et_name.getText().toString().trim();
                String money = et_money.getText().toString().trim();
                String reason = et_reason.getText().toString().trim();
                newGet(name,money,reason);//输入完数据之后显示收礼记录
                Toast.makeText(getActivity(),"送礼人"+name + "正在添加....",Toast.LENGTH_SHORT).show();
            }
        });
        //设置反面按钮，并做事件处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"取消添加",Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();//显示Dialog对话框
    }
    //点击随礼按钮之后
    public void newPut(String name,String money,String reason)
    {
        //将数据保存在文件中
        putGift_File(name,money,reason);
        //向下方listview列表添加，类型0为随礼
        giftListHome.add(new GIft(Name + "  随礼 $: " + Money + " 元 ", year1 + "年" + month1 + "月" + dayOfMonth1 + "日", Reason,0));
        adapter.notifyDataSetChanged();
    }
    //点击收礼按钮之后
    public void newGet(String name,String money,String reason)
    {
        //将数据保存在文件中
        getGift_File(name,money,reason);
        //向下方listview列表添加，类型1为随礼
        giftListHome.add(new GIft(Name + "  收礼 $: " + Money + " 元 ", year1 + "年" + month1 + "月" + dayOfMonth1 + "日", Reason,1));
        adapter.notifyDataSetChanged();
    }
    //使用databank将输入的随礼数据保存在文件中
    public void putGift_File(String name, String money,String reason)
    {
        //将输入的名字和钱保存
        this.Name=name;
        this.Money=money;
        this.Reason=reason;
        dateBank.Load();
        dateBank.getGifts().add(new GIft(Name + "  随礼 $: " + Money + " 元 ", year1 + "年" + month1 + "月" + dayOfMonth1 + "日", Reason,0));
        dateBank.Save();
        GetGiftFragment.notifiy();
    }
    //使用databank将输入的收礼数据保存在文件中
    public void getGift_File(String name, String money,String reason)
    {
        //将输入的名字和钱保存
        this.Name=name;
        this.Money=money;
        this.Reason=reason;
        dateBank.Load();
        dateBank.getGifts().add(new GIft(Name + "  收礼 $: " + Money + " 元 ", year1 + "年" + month1 + "月" + dayOfMonth1 + "日", Reason,1));
        dateBank.Save();
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
            if(null == convertView)
                view = LayoutInflater.from(getContext()).inflate(this.resourceId, parent, false);
            else
                view=convertView;
            ((TextView)view.findViewById((R.id.gift_people))).setText(gIft.getTextName());
            ((TextView)view.findViewById((R.id.gift_time))).setText(gIft.getTime());
            ((TextView)view.findViewById((R.id.gift_reason))).setText(gIft.getTextReason());
            return view;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v==getActivity().findViewById(R.id.listviewhome)) {
            menu.setHeaderTitle("操作");
            //只保留修改和删除操作
            menu.add(1, CONTEXT_MENU_ITEM_UPDATE, 1, "修改");
            menu.add(1, CONTEXT_MENU_ITEM_DELETE, 1, "删除");
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int position=menuInfo.position;
        switch(item.getItemId())
        {
            case CONTEXT_MENU_ITEM_UPDATE:

                AlertDialog.Builder builderUpdate = new AlertDialog.Builder(this.getContext());
                String stringName;
                String stringMoney;
                builderUpdate.setTitle("修改记录");
                //通过布局填充器获自定义的布局文件input_update
                View view = getLayoutInflater().inflate(R.layout.input_update,null);
                builderUpdate.setView(view);//设置input_update为对话提示框
                builderUpdate.setCancelable(true);//设置为可取消
                String stringText=giftListHome.get(position).getTextName();
                int type=giftListHome.get(position).getType();
                if(type == 1)
                    stringName=stringText.substring(0,stringText.indexOf("  收礼 $: "));
                else
                    stringName=stringText.substring(0,stringText.indexOf("  随礼 $: "));
                stringMoney=stringText.substring(stringName.length()+8,stringText.length()-3);
                et_name = (EditText) view.findViewById(R.id.et_name);//送礼人
                et_money = (EditText) view.findViewById(R.id.et_money);//金额
                et_reason = (EditText) view.findViewById(R.id.et_reason);//送礼原因
                if(stringName!=null)
                    et_name.setText(stringName);
                if(stringMoney!=null)
                    et_money.setText(stringMoney);
                et_reason.setText(giftListHome.get(position).getTextReason());

                builderUpdate.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        String name = et_name.getText().toString();
                        String money = et_money.getText().toString();
                        String reason = et_reason.getText().toString();
                        dateBank.Load();//读取数据
                        for(int i = 0; i < dateBank.getGifts().size(); i++){
                            if(dateBank.getGifts().get(i).EqualsGift(giftListHome.get(position)))
                            {
                                if(dateBank.getGifts().get(i).getType() == 1)
                                {
                                    dateBank.getGifts().get(i).setTextName(name + "  收礼 $: " + money + " 元 ");
                                    dateBank.getGifts().get(i).setTextReason(reason);
                                    giftListHome.get(position).setTextName(name + "  收礼 $: " + money + " 元 ");
                                    giftListHome.get(position).setTextReason(reason);
                                }
                                else
                                {
                                    dateBank.getGifts().get(i).setTextName(name + "  随礼 $: " + money + " 元 ");
                                    dateBank.getGifts().get(i).setTextReason(reason);
                                    giftListHome.get(position).setTextName(name + "  随礼 $: " + money + " 元 ");
                                    giftListHome.get(position).setTextReason(reason);
                                }
                                dateBank.Save();
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"送礼人"+name + "正在修改....",Toast.LENGTH_SHORT).show();
                    }
                });
                //设置反面按钮，并做事件处理
                builderUpdate.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(),"取消修改",Toast.LENGTH_SHORT).show();
                    }
                });
                builderUpdate.create().show();
                break;
            case CONTEXT_MENU_ITEM_DELETE:

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getContext());
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\""+giftListHome.get(position).getTextName() + "\"？");//position在giftListHome中用，dateBank使用会出问题
                builder.setCancelable(true);
                //正面的按钮（肯定）
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dateBank.Load();//读取数据
                        //对于在多个日期的切换中添加的数据，加一个循环，否则删除会出现问题
                        for(int i = 0; i < dateBank.getGifts().size(); i++){
                            if(dateBank.getGifts().get(i).EqualsGift(giftListHome.get(position)))
                            {
                                dateBank.getGifts().remove(i);
                                dateBank.Save();
                                break;
                            }
                        }
                        //会有问题,删除的时候会先删除上一天,而且删除最后一个直接崩溃
                        //adapterGet.notifyDataSetChanged();
                        giftListHome.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }); //反面的按钮（否定)
                builder.create().show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


}