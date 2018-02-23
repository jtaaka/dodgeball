package tamk.fi.polttopallopeli.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import tamk.fi.polttopallopeli.Dodgeball;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1270;
		config.height = 720;
		new LwjglApplication(new Dodgeball(), config);
	}
}
