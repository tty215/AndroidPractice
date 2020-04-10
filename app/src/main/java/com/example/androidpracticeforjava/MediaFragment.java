package com.example.androidpracticeforjava;

import android.app.Activity;
import android.content.Intent;
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

import com.example.androidpracticeforjava.service.SoundManagerService;

import java.io.IOException;


public class MediaFragment extends Fragment {
    private Activity _parentActivity;

    private Button _btPlay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parentActivity = getActivity();
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
                Intent intent = new Intent(_parentActivity, SoundManagerService.class);

                Log.d("sound", "" + _btPlay.getText());

                if (_btPlay.getText().equals("再生")) {
                    _parentActivity.startService(intent);
                    _btPlay.setText(R.string.bt_play_pause);
                }
                else {
                    _parentActivity.stopService(intent);
                    _btPlay.setText(R.string.bt_play_play);
                }
            }
        });

        return view;
    }
}
