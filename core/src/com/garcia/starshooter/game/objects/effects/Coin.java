package com.garcia.starshooter.game.objects.effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Coin {

    Texture texture;

    private Animation<TextureRegion> coinAnimation;
    private float coinTimer;

    TextureRegion[][] textureRegion2D;
    TextureRegion[] textureRegion1D;

    public float x, y;
    float totalAnimationTime;

    public Coin(float x, float y) {
        this.x = x;
        this.y = y;

        totalAnimationTime = 0.8f;
        texture = new Texture("sprites/effects/coin_01.png");


        // split texture
        textureRegion2D = TextureRegion.split(texture, 120, 120);

        // convert to 1D array
        textureRegion1D = new TextureRegion[8];

        int index = 0;

        for (int j = 0; j < 8; j++) { //column
            textureRegion1D[index] = textureRegion2D[0][j];
            index++;
        }

        coinAnimation = new Animation<TextureRegion>(totalAnimationTime / 8, textureRegion1D);
        coinAnimation.setPlayMode(Animation.PlayMode.LOOP);
        coinTimer = 0;
    }

    public void update(float deltaTime) {
        coinTimer += deltaTime;
    }

    public void draw(Batch batch) {
        batch.draw(coinAnimation.getKeyFrame(coinTimer),
                x, y, 5, 6);
    }

}
