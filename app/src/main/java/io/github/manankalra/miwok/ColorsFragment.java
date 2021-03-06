package io.github.manankalra.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }
        }
    };
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_colors, container, false);
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colorsList = new ArrayList<>();
        colorsList.add(new Word("Red", "Weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        colorsList.add(new Word("Green", "Chokkoki", R.drawable.color_green, R.raw.color_green));
        colorsList.add(new Word("Brown", "Ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        colorsList.add(new Word("Gray", "Ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        colorsList.add(new Word("Black", "Kululli", R.drawable.color_black, R.raw.color_black));
        colorsList.add(new Word("White", "Kelelli", R.drawable.color_white, R.raw.color_white));
        colorsList.add(new Word("Dusty Yellow", "Ṭopiisə", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorsList.add(new Word("Mustard Yellow", "Chiwiiṭə", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter itemsAdapter = new WordAdapter(getActivity(), colorsList, R.color.category_colors);
        ListView rootColors = (ListView) rootView.findViewById(R.id.rootColors);
        rootColors.setAdapter(itemsAdapter);

        rootColors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word clickedWord = colorsList.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), clickedWord.getAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(completionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        //If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            //Regardless of the current state of the media player, release its resources
            //because we no longer need it.
            mediaPlayer.release();
            //Set the media player back to null. For our code, we've decided that
            //setting the media player to null is an easy way to tell that the media player
            //is not configured to play an audio file at the moment.
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
