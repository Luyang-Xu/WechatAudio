package com.luyang.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by luyang on 2017/12/26.
 */

public class RecorderAdapter extends ArrayAdapter<MainActivity.Recorder> {

    private int minWidth;
    private int maxWidth;

    private LayoutInflater inflater;


    public RecorderAdapter(@NonNull Context context, List<MainActivity.Recorder> datas) {
        super(context, -1, datas);

        inflater = LayoutInflater.from(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        minWidth = (int) (dm.widthPixels * 0.15f);
        maxWidth = (int) (dm.widthPixels * 0.7f);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recorder, parent, false);
            holder = new ViewHolder();
            holder.seconds = (TextView) convertView.findViewById(R.id.id_recorder_time);
            holder.length = convertView.findViewById(R.id.id_recorer_length);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seconds.setText(Math.round(getItem(position).time) + "\"");
        ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
        lp.width = (int) (minWidth + (maxWidth / 60f * getItem(position).time));
        return convertView;
    }

    class ViewHolder {

        TextView seconds;
        View length;


    }
}
