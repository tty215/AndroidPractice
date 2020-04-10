package com.example.androidpracticeforjava;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;


public class MediaFragment extends Fragment {
    private Activity _parentActivity;

    private MediaPlayer _player;
    private Button _btPlay;
    private Button _btBack;
    private Button _btForward;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parentActivity = getActivity();

        _player = new MediaPlayer();

        String mediaFileUrlStr =
                "android.resource://" +
                _parentActivity.getPackageName() +
                "/" +
                R.raw.flexxx;
        Uri mediaFileUri = Uri.parse(mediaFileUrlStr);

        try {
            // 音声ファイルを指定する
            _player.setDataSource(_parentActivity, mediaFileUri);

            // 再生準備が完了した時のリスナを設定
            _player.setOnPreparedListener(new PlayerPreparedListener());

            // メディア再生が終了した時のリスナを設定
            _player.setOnCompletionListener(new PlayerCompletionListener());

            _player.prepareAsync();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media, container, false);

        _btPlay = view.findViewById(R.id.btPlay);
        _btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_player.isPlaying()) {
                    _player.pause();
                    _btPlay.setText(R.string.bt_play_play);
                }
                else {
                    _player.start();
                    _btPlay.setText(R.string.bt_play_pause);
                }
            }
        });

        _btBack = view.findViewById(R.id.btBack);
        _btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _player.seekTo(0);
            }
        });

        _btForward = view.findViewById(R.id.btForward);
        _btForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int alltime = _player.getDuration();
                int current = _player.getCurrentPosition();

                Log.d("media", "alltime: "+ alltime);
                Log.d("media", "current: "+ current);

                if (current + 15000 >= alltime ) {
                    _player.seekTo(0);
                    _player.pause();
                    _btPlay.setText(R.string.bt_play_play);
                }
                else {
                    _player.seekTo(current + 15000);
                }
            }
        });

        return view;
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

    // メディアクラスの準備完了で呼ばれる
    private class PlayerPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            // タップ可能にする
            _btPlay.setEnabled(true);
            _btBack.setEnabled(true);
            _btForward.setEnabled(true);
        }
    }

    // メディア再生終了時に呼ばれる
    private class PlayerCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // ボタンの表示変更
            _btPlay.setText(R.string.bt_play_play);
        }
    }
}
