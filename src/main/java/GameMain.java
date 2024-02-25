import javax.swing.*;
public class GameMain {
	private static int srcWidth = 800;
	private static int srcHeight = 600;
	private static int srcBitdepth = 32;
	private JFrame gameFrame;
	private WrapCheckers3D wrap;
	
	public static void main(String []args) {
		GameMain game = new GameMain();
	}
	public GameMain() {
		ScreenManager screen = new ScreenManager(srcWidth,srcHeight,srcBitdepth,"Java 3D Test");
		screen.setWindowMode();
		gameFrame = screen.getFrame();
		wrap = new WrapCheckers3D(srcWidth,srcHeight);
		gameFrame.add(wrap);
	}
}
