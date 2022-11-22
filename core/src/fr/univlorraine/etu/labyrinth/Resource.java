package fr.univlorraine.etu.labyrinth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public final class Resource {

    public static final String MAP_LEVEL_1 = "levels/map_test2.tmx";

    public static final Texture COIN_TEXTURE = new Texture(Gdx.files.internal("textures/animation_coin.png"));

    public static final Sound COIN_SOUND = Gdx.audio.newSound(Gdx.files.internal("sound/coin.wav"));

    public static final Texture HERO_TEXTURE = new Texture(Gdx.files.internal("textures/animation_hero_knight.png"));

    public static final Texture BOW_TEXTURE =new Texture(Gdx.files.internal("textures/bow.png"));

    public static final String ARROW_TEXTURE = "textures/arrow.png";

    public static final Sound HERO_WALK_SOUND = Gdx.audio.newSound(Gdx.files.internal("sound/sfx_step_grass_l.mp3"));
    public static final int HERO_WALK_DELTASOUND = 25;

    public static final Music MUSIC_LEVEL = Gdx.audio.newMusic(Gdx.files.internal("sound/backgroundMusic.mp3"));

    public static final String FULL_HEART = "textures/ui_heart_full.png";

    public static final String HALF_HEART = "textures/ui_heart_half.png";

    public static final String EMPTY_HEART = "textures/ui_heart_empty.png";

    public static final String FONT_HUD = "fonts/alagard.ttf";

    public static final String IMAGE_COIN_HUD = "textures/coin_anim_f0.png";
    public static final Texture MASKULL_TEXTURE = new Texture(Gdx.files.internal("textures/animation_maskull.png"));

    private Resource() {

    }

    public static void dispose() {
        MUSIC_LEVEL.dispose();
        COIN_SOUND.dispose();
        HERO_WALK_SOUND.dispose();

        HERO_TEXTURE.dispose();
        COIN_TEXTURE.dispose();
        MASKULL_TEXTURE.dispose();
    }


}
