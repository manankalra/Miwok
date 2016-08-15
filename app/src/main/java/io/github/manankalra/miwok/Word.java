package io.github.manankalra.miwok;

public class Word {
    private static final int NO_IMAGE_PROVIDED = -1;
    private String defaultTranslation;
    private String miwokTranslation;
    private int imageResourceId = NO_IMAGE_PROVIDED;
    private int audioResourceId;

    public Word(String def, String miwok, int audioResource) {
        defaultTranslation = def;
        miwokTranslation = miwok;
        audioResourceId = audioResource;
    }

    public Word(String def, String miwok, int imageresourse, int audioResource) {
        defaultTranslation = def;
        miwokTranslation = miwok;
        imageResourceId = imageresourse;
        audioResourceId = audioResource;
    }

    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    public String getMiwokTranslation() {
        return miwokTranslation;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getAudioResourceId() {
        return audioResourceId;
    }

    public boolean hasImage() {
        return imageResourceId != NO_IMAGE_PROVIDED;
    }
}
