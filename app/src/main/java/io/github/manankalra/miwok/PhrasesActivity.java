package io.github.manankalra.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
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
    MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        final ArrayList<Word> phrasesList = new ArrayList<>();
        phrasesList.add(new Word("Where are you going?", "Minto wuksus", R.raw.phrase_where_are_you_going));
        phrasesList.add(new Word("What is your name?", "Tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        phrasesList.add(new Word("My name is...", "Oyaaset...", R.raw.phrase_my_name_is));
        phrasesList.add(new Word("How are you feeling?", "Michәksәs?", R.raw.phrase_how_are_you_feeling));
        phrasesList.add(new Word("I’m feeling good.", "Kuchi achit", R.raw.phrase_im_feeling_good));
        phrasesList.add(new Word("Are you coming?", "Əәnәs'aa?", R.raw.phrase_are_you_coming));
        phrasesList.add(new Word("Yes, I’m coming.", "Hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        phrasesList.add(new Word("I’m coming.", "Əәnәm", R.raw.phrase_im_coming));
        phrasesList.add(new Word("Let’s go.", "Yoowutis", R.raw.phrase_lets_go));
        phrasesList.add(new Word("Come here.", "Ənni'nem", R.raw.phrase_come_here));

        WordAdapter itemsAdapter = new WordAdapter(this, phrasesList, R.color.category_phrases);
        ListView rootPhrases = (ListView) findViewById(R.id.rootPhrases);
        rootPhrases.setAdapter(itemsAdapter);

        rootPhrases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word clickedWord = phrasesList.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, clickedWord.getAudioResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(completionListener);
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    //Clean up the media player by releasing its resources
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
