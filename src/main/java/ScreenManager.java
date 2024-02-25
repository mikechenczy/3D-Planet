import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ScreenManager {
	private GraphicsDevice device;
	private JFrame frame;
	private String title;
	private boolean isResizable;
	private boolean isWindowMode;
	private int srcWidth;
	private int srcHeight;
	private int srcBitdepth;
	
	public ScreenManager(int srcWidth,int srcHeight,int srcBitdepth,String title) {
		this.srcWidth = srcWidth;
		this.srcHeight = srcHeight;
		this.srcBitdepth = srcBitdepth;
		this.title = title;
	}
	
	public void setWindowMode() {
		KeyPressListener keyPressListener = new KeyPressListener();
		frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
		frame.addKeyListener(keyPressListener);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				//WJK();
			}
		});
		this.setFrametoCenter();
	}
	public void setFullWindowMode() {
		if(frame != null) {
			device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			DisplayMode displayMode = device.getDisplayMode();
			frame.setPreferredSize(new Dimension(displayMode.getWidth(),displayMode.getHeight()));
		}
	}
	
	public int getWidth() {
		return srcWidth;
	}
	public int getHeight() {
		return srcHeight;
	}
	public JFrame getFrame() {
		return frame;
	}
	
	public void setFrametoCenter() {
		if(device!=null)
			return;
		Insets inset = frame.getInsets();
		int srcx=0;
		int srcy=0;
		Dimension srcSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		if(srcSize.width > srcWidth)
			srcx = (srcSize.width=srcWidth/2);
		if(srcSize.height > srcHeight)
			srcy = (srcSize.height=srcHeight/2);
		frame.setBounds(srcx-inset.left,srcy-inset.right,srcWidth+inset.right+inset.left,srcHeight+inset.bottom+inset.top);
	}
	private class KeyPressListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            	WJK();
            }
        }
    }
	private void WJK(){
		int res = JOptionPane.showConfirmDialog(null,"ÊÇ·ñÍË³ö£¡","ÍË³ö",JOptionPane.YES_NO_OPTION);
		if(res == JOptionPane.YES_OPTION) {
			closeFrame();
		}
	}
	public void closeFrame() {
		frame.dispose();
		System.exit(0);
	}
}
