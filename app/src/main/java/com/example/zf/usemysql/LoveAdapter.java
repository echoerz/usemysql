package com.example.zf.usemysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 18810 on 2018/6/22.
 */

public class LoveAdapter extends ArrayAdapter {
    private final int resourceId;

    public LoveAdapter(Context context, int textViewResourceId, List<Love_item> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Love_item loves = (Love_item) getItem(position); //
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        ImageView Picture = (ImageView) view.findViewById(R.id.love_picture_item);//获取该布局内的图片视图
        TextView words = (TextView) view.findViewById(R.id.love_words_item);//获取该布局内的文本视图
        //Picture.setImageResource(updatas.getImageId());//为图片视图设置图片资源
        words.setText(loves.ShowInLove());//为文本视图设置文本内容
        return view;
    }
}
