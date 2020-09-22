package com.garcia.starshooter.game.objects;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;

    // singleton: prevent instantiation from other classes
    private AudioManager() {
    }


    public void play(Sound sound) {
        play(sound, 1);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1);
    }


    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 0);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
//        if (!GamePreferences.instance.sound) return;
        sound.play(volume, pitch, pan);
    }

    public void play(Music music) {
        stopMusic();
        playingMusic = music;
//        if (GamePreferences.instance.music) { // changed based on save prefs
        music.setLooping(true);
//            music.setVolume(GamePreferences.instance.volMusic);
        music.setVolume(0.3f);
        music.play();
//        }
    }

//    public void onSettingsUpdated() { when settings are changed
//        if (playingMusic == null) return;
//        playingMusic.setVolume(GamePreferences.instance.volMusic);
//        if (GamePreferences.instance.music) {
//            if (!playingMusic.isPlaying()) playingMusic.play();
//        } else {
//            playingMusic.pause();
//        }
//    }

    public void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

}
