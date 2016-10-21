package com.mkch.youshi.view;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mkch.youshi.R;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ZJ on 2016/10/20.
 */
public class RecordButton extends Button {
    public static final File AUDIO_DIR = new File(Environment.getExternalStorageDirectory(), "audio");
    private Dialog _dialog;
    MediaRecorder recorder;
    File audioFile;
    ImageView imageView;
    MicVolumnPicker micVolumnPicker;

    private long startTime;

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://开始录音
                startRecord();
                break;
            case MotionEvent.ACTION_UP://停止录音
                stopRecord();
                break;
            case MotionEvent.ACTION_CANCEL://取消，手指松开
                break;
            default:
                break;
        }
        return true;
    }

    private void stopRecord() {
        //关闭对话框
        if (_dialog != null) {
            _dialog.dismiss();
        }
        //停止拾取音量
        if (micVolumnPicker != null) {
            micVolumnPicker.setRunning(false);
        }
        //停止录音
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            //判断录音时长是否太短
            Log.d("jlj", "duration is -------------------------" + (System.currentTimeMillis()) + "," + startTime);
            if ((System.currentTimeMillis() - startTime) < 1000) {
                Toast.makeText(getContext(), "录音的时间太短", Toast.LENGTH_SHORT).show();
                //删除录音文件
                if (audioFile.exists()) {
                    audioFile.delete();
                }
                return;
            }
        }
        Log.d("jlj", "----------" + audioFile.getAbsolutePath());
        //录音完成
        if (onRecordFinishedListener != null) {
            int _duration = (int) (System.currentTimeMillis() - startTime);
            onRecordFinishedListener.onFinished(audioFile, _duration);
        }
    }

    private void startRecord() {
        startTime = System.currentTimeMillis();//开始录音时间
        _dialog = new Dialog(getContext(), R.style.like_toast_dialog_style);
        imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.sound_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        _dialog.addContentView(imageView, params);
        //开始使用MediaRecorder录音
        recorder = new MediaRecorder();
        //音源
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //文件格式
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        //编码格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //输出文件的路径
        if (!AUDIO_DIR.exists()) {
            AUDIO_DIR.mkdir();
        }
        audioFile = new File(AUDIO_DIR, generate() + ".amr");
        recorder.setOutputFile(audioFile.getAbsolutePath());
        //缓冲
        try {
            recorder.prepare();
            recorder.start();
            //开始显示对话框
            _dialog.show();
            //启动线程
            micVolumnPicker = new MicVolumnPicker();
            micVolumnPicker.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generate() {
        UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }


    /**
     * 开始录音之后，一个不断拾取音量的线程
     */
    class MicVolumnPicker extends Thread {
        private boolean isRunning = true;

        @Override
        public void run() {
            while (isRunning) {
                SystemClock.sleep(200);
                if (recorder == null) {
                    return;
                }
                //根据分贝大小计算一个音量的相对值
                //分贝，根据这个相对值不断替换显示的图片
                int x = recorder.getMaxAmplitude();
                if (x != 0) {
                    int f = (int) (10 * Math.log(x) / Math.log(10));
                    if (f < 26) {
                        handler.sendEmptyMessage(0);//2格子图片
                    } else if (f < 32) {
                        handler.sendEmptyMessage(1);//3格子图片
                    } else if (f < 38) {
                        handler.sendEmptyMessage(2);//4格子图片
                    } else {
                        handler.sendEmptyMessage(3);//5格子图片,满格
                    }
                }
            }
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
    }

    private int[] volumeImgs = {
            R.drawable.sound_1,
            R.drawable.sound_2,
            R.drawable.sound_3,
            R.drawable.sound_4,
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //更新音量图片
            imageView.setImageResource(volumeImgs[msg.what]);
        }
    };

    /**
     * 监听录音完成
     */
    public interface OnRecordFinishedListener {
        /**
         * 录制完成后回调方法
         *
         * @param audioFile 录制的文件
         * @param duration  时长
         */
        void onFinished(File audioFile, int duration);
    }

    private OnRecordFinishedListener onRecordFinishedListener;

    public void setOnRecordFinishedListener(OnRecordFinishedListener onRecordFinishedListener) {
        this.onRecordFinishedListener = onRecordFinishedListener;
    }
}
