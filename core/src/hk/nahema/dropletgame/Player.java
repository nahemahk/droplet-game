package hk.nahema.dropletgame;

import static com.badlogic.gdx.Gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Rectangle;
import static hk.nahema.dropletgame.Main.*;
import java.util.Iterator;

/**
 *
 * @author nahemahk
 */
public class Player {

    public static int speed = 400;
    
    public static void movement(float delta) {
        float speedMultiplier = 1 * delta;
        
        if (input.isKeyPressed(Keys.SHIFT_LEFT)) {
            speedMultiplier = 2 * delta;
        }
        
        if(input.isKeyPressed(Keys.UP)) player.y += speed * speedMultiplier;
        if(input.isKeyPressed(Keys.DOWN)) player.y -= speed * speedMultiplier;
        if(input.isKeyPressed(Keys.LEFT)) player.x -= speed * speedMultiplier;
        if(input.isKeyPressed(Keys.RIGHT)) player.x += speed * speedMultiplier;
    }
    
    
    public static void limitToBounds() {
        if (player.x < 0) {
            player.x = 0;
        }
        if (player.x > WIDTH - SPRITE_SIZE) {
            player.x = WIDTH - SPRITE_SIZE;
        }
        if (player.y < 0) {
            player.y = 0;
        }
        if (player.y > HEIGHT - SPRITE_SIZE) {
            player.y = HEIGHT - SPRITE_SIZE;
        }
    }

    public static void actions() {
        if (input.isTouched()) {
            var bucket = new Rectangle();
            bucket.width = bucket.height = SPRITE_SIZE;
            bucket.x = player.x - SPRITE_SIZE / 2;
            bucket.y = -player.y + HEIGHT - SPRITE_SIZE / 2;
            buckets.add(bucket);
        }

        if (input.isButtonPressed(Buttons.RIGHT)) {
            for (Iterator<Rectangle> iter = buckets.iterator(); iter.hasNext();) {
                Rectangle bucket = iter.next();
                if (bucket.overlaps(player)) {
                    iter.remove();
                }
            }
        }
    }
    
    public static void metaActions() {
        // Switch fullscreen or windowed with F
        if (input.isKeyJustPressed(Keys.F)) {
            if (graphics.isFullscreen()) {
                graphics.setWindowedMode(WIDTH, HEIGHT);
            } else {
                graphics.setFullscreenMode(graphics.getDisplayMode());
            }
        }

        if (!paused && input.isKeyJustPressed(Keys.SPACE)) {
            paused = true;
            rainMusic.pause();
        } else if (paused && input.isKeyJustPressed(Keys.SPACE)) {
            paused = false;
            rainMusic.play();
        }

        if (input.isKeyPressed(Keys.ESCAPE)) {
            app.exit();
        }
    }

    /*public static void movementLimited(float delta) {
        int speedMultiplier = 1; if (input.isKeyPressed(Keys.SHIFT_LEFT)) {
            speedMultiplier = 2;
        } input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.UP || keycode == Keys.DOWN || keycode == Keys.LEFT || keycode == Keys.RIGHT) {
                    if (keycode != moveKey) {
                        oldMoveKey = moveKey;
                        moveKey = keycode;
                    }
                }
                return true;
            } @Override
            public boolean keyUp(int keycode) {
                if (keycode == Keys.UP || keycode == Keys.DOWN || keycode == Keys.LEFT || keycode == Keys.RIGHT) {
                    if (moveKey == keycode) moveKey = oldMoveKey;
                }
                return true;
            }
        }); boolean isMoving = input.isKeyPressed(Keys.UP)
                || input.isKeyPressed(Keys.DOWN)
                || input.isKeyPressed(Keys.LEFT)
                || input.isKeyPressed(Keys.RIGHT);

        if (isMoving) { switch (moveKey) {
                case Keys.UP ->
                    player.y += speed * speedMultiplier * delta;
                case Keys.DOWN ->
                    player.y -= speed * speedMultiplier * delta;
                case Keys.LEFT ->
                    player.x -= speed * speedMultiplier * delta;
                case Keys.RIGHT ->
                    player.x += speed * speedMultiplier * delta;
            }
        }
    }*/
    
}
