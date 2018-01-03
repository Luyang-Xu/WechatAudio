package com.luyang.myapplication.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.luyang.myapplication.R;

/**
 * Created by luyang on 2017/12/20.
 */

public class AudioRecorderButton extends android.support.v7.widget.AppCompatButton implements AudioManager.AudioStateListener {

    private static final int STATE_NORMAL = 1;
    private static final int STATE_REORDING = 2;
    private static final int STATE_CANCEL = 3;

    private int curState = STATE_NORMAL;

    private boolean isRecording = false;

    //Y方向上距离，滑动取消
    private static final int DISTANCE_Y = 50;

    DialogManager dm;

    AudioManager am;

    //定义handler消息常量
    private static final int PREPARED = 1;
    private static final int CHANGED = 2;
    private static final int DIMISS = 3;

    private float time;
    //判断action_up时候的状态
    private boolean ready;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PREPARED:
                    dm.showDialog();
                    isRecording = true;
                    Thread voiceLevelThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isRecording) {
                                try {
                                    Thread.sleep(1000);
                                    time += 1f;
                                    handler.sendEmptyMessage(CHANGED);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    voiceLevelThread.start();
                    break;
                case CHANGED:
                    dm.setVolumeLevel(am.getVoiceLevel(7));
                    break;
                case DIMISS:
                    dm.dismissDialog();
                    break;
                default:

            }
        }
    };


//    第一个构造函数：     当不需要使用xml声明或者不需要使用inflate动态加载时候，实现此构造函数即可
//
//    第二个构造函数:     当需要在xml中声明此控件，则需要实现此构造函数。并且在构造函数中把自定义的属性与控件的数据成员连接起来。
//
//    第三个构造函数：     接受一个style资源


    public AudioRecorderButton(Context context) {
        //调用两个参数的构造函数
        this(context, null);

    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dm = new DialogManager(context);
        //单例模式
        am = AudioManager.getSingleInstance(Environment.getExternalStorageDirectory() + "/imooc_recorder_audios");
        am.setOnAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                am.prepareAudio();
                ready = true;
                return false;
            }
        });
    }

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(PREPARED);

    }

    //录音完成后的回调接口
    public interface AudioFinishListener {
        void onFinish(float sec, String filePath);
    }


    private AudioFinishListener finishListener;

    public void setAudioFinishRecorderListener(AudioFinishListener listener) {
        finishListener = listener;
    }


    //录音按钮状态的逻辑处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //TODO
                isRecording = true;
                changeState(STATE_REORDING);
                break;
            case MotionEvent.ACTION_UP:
                //longclick都没有触发
                if (!ready) {
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording || time < 0.6f) {
                    //AudioManager的prepare没有完成
                    dm.tooShort();
                    am.cancel();
                    handler.sendEmptyMessageDelayed(DIMISS, 1300);
                } else if (curState == STATE_REORDING) {
                    //正常录制状态
                    //录制结束，释放资源
                    am.release();
                    if (finishListener != null) {
                        finishListener.onFinish(time, am.getCurFilePath());
                    }
                    dm.dismissDialog();
                } else if (curState == STATE_CANCEL) {
                    //取消状态
                    //TODO cancel
                    am.cancel();
                    dm.dismissDialog();
                }

                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    if (wantToCancel(x, y)) {
                        changeState(STATE_CANCEL);
                    } else {
                        changeState(STATE_REORDING);
                    }
                }
                break;
            default:
                break;
        }


        return super.onTouchEvent(event);
    }

    private boolean wantToCancel(int x, int y) {

        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y || y > (getHeight() + DISTANCE_Y)) {
            return true;
        }

        return false;
    }

    //按钮弹起时回复状态
    private void reset() {
        time = 0;
        isRecording = false;
        ready = false;
        changeState(STATE_NORMAL);
    }


    //管理button的样式
    private void changeState(int state) {
        if (curState != state) {
            curState = state;
        }
        switch (curState) {
            case AudioRecorderButton.STATE_NORMAL:
                setBackgroundResource(R.drawable.btn_recorder_normal);
                setText(R.string.str_recorder_normal);
                break;
            case STATE_REORDING:
                setBackgroundResource(R.drawable.btn_recorder_recorder);
                setText(R.string.str_recorder_recording);
                if (isRecording) {
                    dm.recording();
                }
                break;
            case STATE_CANCEL:
                setBackgroundResource(R.drawable.btn_recorder_recorder);
                setText(R.string.str_recorder_cancel);
                //TODO want to cancel
                dm.cancelDialog();
                break;

        }
    }


}
