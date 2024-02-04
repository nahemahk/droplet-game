package hk.nahema.dropletgame;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private Texture dropImage, playerImage, oceanImage, background;
    private Sound dropSound;
    public static Music rainMusic;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    public static Rectangle player, ocean;
    public static Array<Rectangle> raindrops, buckets;

    private long lastDropTime;
    private BitmapFont font;
    private Viewport viewport;
    public static int moveKey, oldMoveKey, rainSpeed, score;
    public static boolean paused;
    private float rainSpeedMult;

    public static final int WIDTH = 640, HEIGHT = 480, SPRITE_SIZE = 64;
    
    @Override
    public void create() {
        buckets = new Array<>();
        paused = false;
        rainSpeedMult = 1;
        rainSpeed = 1000000000;
        score = 0;
        
        dropImage = new Texture(Gdx.files.internal("assets/image/drop.png"));
        playerImage = new Texture(Gdx.files.internal("assets/image/bucket.png"));
        background = new Texture(Gdx.files.internal("assets/image/sky.png"));
        oceanImage = new Texture(Gdx.files.internal("assets/image/ocean.png"));
        dropSound = Gdx.audio.newSound(Gdx.files.internal("assets/audio/drop.ogg"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/rain.ogg"));
        
        rainMusic.setLooping(true);
        rainMusic.play();
        
        font = new BitmapFont();
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        batch = new SpriteBatch();

        player = new Rectangle();
        player.x = WIDTH / 2 - SPRITE_SIZE / 2; // center the player horizontally
        player.width = player.height = SPRITE_SIZE;

        ocean = new Rectangle();
        ocean.width = WIDTH;
        ocean.height = HEIGHT;
        ocean.y = -HEIGHT;

        raindrops = new Array<>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, WIDTH - SPRITE_SIZE);
        raindrop.y = HEIGHT;
        raindrop.width = raindrop.height = SPRITE_SIZE;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        Player.metaActions();
        
        batch.begin();
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.draw(background, 0, 0);

        for (Rectangle bucket : buckets) {
            batch.draw(playerImage, bucket.x, bucket.y);
        }
        
        batch.draw(playerImage, player.x, player.y);

        for (Rectangle raindrop : raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        batch.draw(oceanImage, ocean.x, ocean.y);

        font.draw(batch, "Score: " + score, 10, HEIGHT - 10);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), WIDTH - 60, HEIGHT - 10);

        if (!paused) {
            
            Player.movement(delta);
            Player.limitToBounds();

            if (ocean.y >= 0) {
                font.draw(batch, "GAME OVER", 10, 20);
                if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                    rainSpeed = 1000000000;
                    score = 0;
                    ocean.y = -HEIGHT;
                }
            } else {
                rainSpeedMult = 1 + ((float) score / 10000f);

                // check if we need to create a new raindrop
                if (TimeUtils.nanoTime() - lastDropTime > rainSpeed / rainSpeedMult) {
                    spawnRaindrop();
                }
                for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
                    Rectangle raindrop = iter.next();
                    raindrop.y -= 200 * delta * rainSpeedMult;
                    if (raindrop.y + SPRITE_SIZE < 0 || raindrop.overlaps(ocean)) {
                        iter.remove();
                        ocean.y += 10;
                    } else if (raindrop.overlaps(player)) {
                        dropSound.play();
                        iter.remove();
                        score += 25;
                    } else {
                        for (Rectangle bucket : buckets) {
                            if (raindrop.overlaps(bucket)) {
                                dropSound.play();
                                iter.remove();
                                score += 25;
                                break;
                            }
                        }
                    }
                }
            }

        } else {
            font.draw(batch, "PAUSED", WIDTH / 2 - 20, HEIGHT / 2 + 10);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        background.dispose();
        oceanImage.dispose();
        dropImage.dispose();
        playerImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        batch.dispose();
    }
    
}
