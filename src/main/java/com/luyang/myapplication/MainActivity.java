package com.luyang.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.luyang.myapplication.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Recorder> adapter;
    private List<Recorder> datas = new ArrayList<>();

    private AudioRecorderButton audioRecorderButton;


    private View animView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.id_listview);
        audioRecorderButton = findViewById(R.id.id_recorderButton);
        audioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishListener() {
            @Override
            public void onFinish(float sec, String filePath) {
                Recorder r =new Recorder(sec, filePath);
                datas.add(r);
                adapter.notifyDataSetChanged();
                listView.setSelection(datas.size() -1 );
            }
        });

        adapter = new RecorderAdapter(MainActivity.this,datas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(animView!=null){
                    animView.setBackgroundResource(R.drawable.adj);
                }
                //1.播放动画，
                animView = view.findViewById(R.id.id_recorder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable) animView.getBackground();
                anim.start();
                //2.播放音频
                MediaManager.playSound(datas.get(position).path ,new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    class Recorder{

        float time;
        String path;

        public Recorder() {
        }

        public Recorder(float time, String path) {
            this.time = time;
            this.path = path;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public float getTime() {
            return time;
        }

        public String getPath() {
            return path;
        }
    }






}
