package hk.nahema.dropletgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import hk.nahema.dropletgame.Main;

public class DesktopLauncher {

    public static void main(String[] arg) {
        if (System.getProperty("os.name").startsWith("Windows")) {
            System.setProperty("java.io.tmpdir", System.getenv("ProgramData") + "/nahemi-games");
        }
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.useVsync(true);
        config.setTitle("Droplet Game");
        config.setWindowIcon(
	        "assets/meta/lwjgl3-512.png",
	        "assets/meta/lwjgl3-256.png",
	        "assets/meta/lwjgl3-128.png",
	        "assets/meta/lwjgl3-64.png",
	        "assets/meta/lwjgl3-32.png",
	        "assets/meta/lwjgl3-16.png"
	    );
        new Lwjgl3Application(new Main(), config);
    }

}