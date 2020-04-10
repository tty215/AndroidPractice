package com.example.androidpracticeforjava.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.androidpracticeforjava.MediaFragment;
import com.example.androidpracticeforjava.R;

import java.io.IOException;
import java.lang.reflect.Array;

public class SoundManagerService extends Service {

    private MediaPlayer _player;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        _player = new MediaPlayer();

        String id = "soundmanagerservice_nortification_channel";
        String name = getString(R.string.msg_channel_name);

        // 通知の重要度を標準に設定
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        // 通知チャネル作成
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.createNotificationChannel(channel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_player.isPlaying()) {
            _player.stop();
        }

        _player.release();
        _player = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String mediaFileUrlStr =
            "android.resource://" +
            getPackageName() +
            "/" +
            R.raw.great_hope;
        Uri mediaFileUri = Uri.parse(mediaFileUrlStr);

        try {
            // 音声ファイルを指定する
            _player.setDataSource(SoundManagerService.this, mediaFileUri);

            // 再生準備が完了した時のリスナを設定
            _player.setOnPreparedListener(new PlayerPreparedListener());

            // メディア再生が終了した時のリスナを設定
            _player.setOnCompletionListener(new PlayerCompletionListener());

            _player.prepareAsync();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return START_NOT_STICKY;
    }


    // メディアクラスの準備完了で呼ばれる
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    }

    // メディア再生終了時に呼ばれる
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    SoundManagerService.this,
                    "soundmanagerservice_nortification_channel"
            );
            builder.setSmallIcon(android.R.drawable.ic_dialog_info);
            builder.setContentTitle(getString(R.string.msg_title_finish));
            builder.setContentText(getString(R.string.msg_text_finish));

            Notification notification = builder.build();
            NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            manager.notify(0, notification);

            stopSelf();
        }
    }
}
