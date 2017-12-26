package com.luyang.myapplication.view;

import android.media.MediaRecorder;
import java.io.File;
import java.util.UUID;

/**
 * Created by luyang on 2017/12/22.
 */

public class AudioManager {

    private MediaRecorder mediaRecorder;

    private String dir;

    private String curPath;

    private boolean isPrepared;

    //单例模式
    private static AudioManager singleInstance;

    private AudioManager(String audioDir) {
        dir = audioDir;
    }

    public static AudioManager getSingleInstance(String ldir) {
        if (singleInstance == null) {
            synchronized (AudioManager.class) {
                if (singleInstance == null) {
                    singleInstance = new AudioManager(ldir);
                }
            }
        }
        return singleInstance;
    }

    //回传接口
    public interface AudioStateListener {
        void wellPrepared();
    }

    private AudioStateListener mlistener;

    public void setOnAudioStateListener(AudioStateListener listener) {
        mlistener = listener;
    }


    public void prepareAudio() {
        isPrepared = false;
        //1.创建问价夹
        File mDir = new File(dir);
        if (!mDir.exists()) {
            mDir.mkdir();
        }

        String fileName = generateName();

        try {
            File file = new File(mDir, fileName);
            curPath = file.getAbsolutePath();
            //启动音频前的录音准备
            mediaRecorder = new MediaRecorder();
            //输出文件路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            //输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //音频来源话筒
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //音频编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isPrepared = true;
        if (mlistener != null) {
            //通知已经准备好了，可以开始录制音频
            mlistener.wellPrepared();
        }


    }

    //随机存储语音文件的文件名
    private String generateName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            //需要间隔一段时间调用一次的，也就是说，需要放在线程里面调用的。第一次调用会返回0,获取在前一次调用此方法之后录音中出现的最大振幅
            try {
                return 7 * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                return 1;
            }

        }
        return 1;
    }

    public void release() {
        mediaRecorder.stop();
        mediaRecorder.release();;
        mediaRecorder = null;

    }

    public void cancel() {
        release();
        if(curPath != null){
            File delFile = new File(curPath);
            delFile.delete();
            curPath = null;
        }

    }

    public String getCurFilePath(){
        return curPath;
    }

}
