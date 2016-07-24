package io.github.manankalra.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };
    AudioManager audioManager;
    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> familyList = new ArrayList<>();
        familyList.add(new Word("Father", "əpə", R.drawable.family_father, R.raw.family_father));familyList.add(new Word("Mother", "əṭa", R.drawable.family_mother, R.raw.family_mother));
        familyList.add(new Word("Son", "Angsi", R.drawable.family_son, R.raw.family_son));familyList.add(new Word("Daughter", "Tune", R.drawable.family_daughter, R.raw.family_daughter));
        familyList.add(new Word("Older Brother", "Taachi", R.drawable.family_older_brother, R.raw.family_older_brother));familyList.add(new Word("Younger Brother", "Chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyList.add(new Word("Older Sister", "Teṭe",R.drawable.family_older_sister, R.raw.family_older_sister));familyList.add(new Word("Younger Sister", "Kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyList.add(new Word("Grandfather", "Paapa", R.drawable.family_grandfather, R.raw.family_grandfather));familyList.add(new Word("Grandmother", "Ama", R.drawable.family_grandmother, R.raw.family_grandmother));

        WordAdapter itemsAdapter = new WordAdapter(this, familyList, R.color.category_family);
        ListView rootFamily = (ListView) findViewById(R.id.rootFamily);
        rootFamily.setAdapter(itemsAdapter);

        rootFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word clickedWord = familyList.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(FamilyActivity.this, clickedWord.getAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(completionListener);
                }

            }
        });
    }
    @Override
    public void onStop(){
        super.onStop();
        releaseMediaPlayer();
    }
    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        }
    }
}
