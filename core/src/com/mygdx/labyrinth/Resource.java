package com.mygdx.labyrinth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public final class Resource {

    private Resource instance = new Resource();

    public static final String MAP_LEVEL_1 = "levels/map_test2.tmx";

    public static final Texture COIN_TEXTURE = new Texture(Gdx.files.internal("textures/animation_coin.png"));

    public static final Sound COIN_SOUND = Gdx.audio.newSound(Gdx.files.internal("sound/coin.wav"));

    public static final Texture HERO_TEXTURE = new Texture(Gdx.files.internal("textures/animation_hero_knight.png"));

    public static final Texture BOW_TEXTURE =new Texture(Gdx.files.internal("textures/bow.png"));

    public static final Texture ARROW_TEXTURE = new Texture(Gdx.files.internal("textures/arrow.png"));

    public static final Sound HERO_WALK_SOUND = Gdx.audio.newSound(Gdx.files.internal("sound/sfx_step_grass_l.mp3"));

    public static final Music MUSIC_LEVEL = Gdx.audio.newMusic(Gdx.files.internal("sound/backgroundMusic.mp3"));

    public static final Texture FULL_HEART = new Texture(Gdx.files.internal("textures/ui_heart_full.png"));

    public static final Texture HALF_HEART = new Texture(Gdx.files.internal("textures/ui_heart_half.png"));

    public static final Texture EMPTY_HEART = new Texture(Gdx.files.internal("textures/ui_heart_empty.png"));

    public static final String FONT_HUD = "fonts/alagard.ttf";

    public static final Texture IMAGE_COIN_HUD = new Texture(Gdx.files.internal("textures/coin_anim_f0.png"));
    public static final Texture MASKULL_TEXTURE = new Texture(Gdx.files.internal("textures/animation_maskull.png"));

    public static final Texture POPO_HEAL = new Texture(Gdx.files.internal("textures/popo_heal.png"));

    public static void dispose() {
        MUSIC_LEVEL.dispose();
        COIN_SOUND.dispose();
        HERO_WALK_SOUND.dispose();

        HERO_TEXTURE.dispose();
        COIN_TEXTURE.dispose();
        MASKULL_TEXTURE.dispose();
        POPO_HEAL.dispose();
        FULL_HEART.dispose();
        HALF_HEART.dispose();
        EMPTY_HEART.dispose();
        IMAGE_COIN_HUD.dispose();
        ARROW_TEXTURE.dispose();
    }


}
