package com.garcia.starshooter.game.objects.effects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

    // pos
    public Rectangle boundingBox;

    // vars
    public float movementSpeed;
    public float dmg = 0;

    // gfx
    TextureRegion textureRegion;

    public Laser(float xCenter, float yBottom, float width, float height, float movementSpeed, float dmg, TextureRegion textureRegion) {
        this.boundingBox = new Rectangle(xCenter, yBottom, width, height);
        this.movementSpeed = movementSpeed;
        this.dmg += dmg;
        this.textureRegion = textureRegion;
    }

    public void draw (Batch batch) {
        batch.draw(textureRegion, boundingBox.x - boundingBox.width / 2, boundingBox.y, boundingBox.width, boundingBox.height);
    }

}
