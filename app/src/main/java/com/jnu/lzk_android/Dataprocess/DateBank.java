package com.jnu.lzk_android.Dataprocess;

import android.content.Context;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DateBank {
    private ArrayList<GIft> arrayListGifts =new ArrayList<>();
    private Context context;
    private final String GIFT_FILE_NAME ="gifts.txt";
    public DateBank(Context context)
    {
        this.context=context;
    }
    public ArrayList<GIft> getGifts() {
        return arrayListGifts;
    }


    public void Save()
    {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(context.openFileOutput(GIFT_FILE_NAME,Context.MODE_PRIVATE));
            oos.writeObject(arrayListGifts);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Load()
    {
        ObjectInputStream ois = null;
        arrayListGifts =new ArrayList<>();
        try {
            ois = new ObjectInputStream(context.openFileInput(GIFT_FILE_NAME));
            arrayListGifts = (ArrayList<GIft>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
