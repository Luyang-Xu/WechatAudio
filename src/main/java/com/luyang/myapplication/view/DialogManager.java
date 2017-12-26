package com.luyang.myapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.luyang.myapplication.R;

/**
 * Created by luyang on 2017/12/21.
 */

public class DialogManager {

    private Dialog dialog;

    private ImageView icon;

    private ImageView voice;

    private TextView dialog_label;

    private Context context;

    public DialogManager(Context context) {
        this.context = context;
    }

    public void showDialog(){
        dialog = new Dialog(context, R.style.theme_dialog);
        //将dialog的layout样式加载进来
        LayoutInflater inflater =LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.dialog_layout,null);
        dialog.setContentView(view);

        icon = dialog.findViewById(R.id.dialog_icon);
        voice = dialog.findViewById(R.id.dialog_voice);
        dialog_label = dialog.findViewById(R.id.dialog_label);

        dialog.show();
    }

    public void recording(){
        if(dialog != null && dialog.isShowing()){
            icon.setVisibility(View.VISIBLE);
            voice.setVisibility(View.VISIBLE);
            dialog_label.setVisibility(View.VISIBLE);

            dialog_label.setText(R.string.dialog_label);
            icon.setImageResource(R.drawable.recorder);

        }

    }

    public void cancelDialog(){

        if(dialog != null && dialog.isShowing()){
            icon.setVisibility(View.VISIBLE);
            voice.setVisibility(View.GONE);
            dialog_label.setVisibility(View.VISIBLE);


            icon.setImageResource(R.drawable.cancel);
            dialog_label.setText(R.string.dialog_label);
        }

    }

    public void tooShort(){

        if(dialog != null && dialog.isShowing()){
            icon.setVisibility(View.VISIBLE);
            voice.setVisibility(View.GONE);
            dialog_label.setVisibility(View.VISIBLE);

            dialog_label.setText(R.string.too_short);
            icon.setImageResource(R.drawable.voice_to_short);

        }

    }

    public void dismissDialog(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 通过得到资源的id属性来刷新图片
     * @param level
     */
    public void setVolumeLevel(int level){

        if(dialog != null && dialog.isShowing()){
//            icon.setVisibility(View.VISIBLE);
//            voice.setVisibility(View.VISIBLE);
//            dialog_label.setVisibility(View.VISIBLE);

            int resid = context.getResources().getIdentifier("v"+level,"drawable",context.getPackageName());
            voice.setImageResource(resid);

        }
    }
}
