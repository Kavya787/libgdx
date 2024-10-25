
package com.test_game.main.birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bird extends Actor {
    private Texture texture;
    private float textureScaleX = 1.0f;  // Horizontal scaling factor
    private float textureScaleY = 1.0f;  // Vertical scaling factor
    private float textureX = 0.0f;        // X position of the texture
    private float textureY = 0.0f;
    public Bird(Texture texture) {
        this.texture = texture;
        setSize(texture.getWidth(), texture.getHeight());
        resizeTexture(0.2f, 0.2f);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Resizes the texture by scaling it.
     * @param scaleX The horizontal scale factor.
     * @param scaleY The vertical scale factor.
     */
    public void resizeTexture(float scaleX, float scaleY) {
        this.textureScaleX = scaleX;
        this.textureScaleY = scaleY;

        // Update the Actor's size based on the scaled texture size
        setSize(texture.getWidth() * textureScaleX, texture.getHeight() * textureScaleY);
    }
}
