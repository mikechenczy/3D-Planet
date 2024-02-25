import java.awt.Font;
import java.util.ArrayList;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.Text2D;
public class CheckerFloor {
	private BranchGroup floor;
	
	public CheckerFloor() {
		floor = new BranchGroup();
	}
	public BranchGroup getBG() {
		Color3f blue = new Color3f(0,0,1);
		Color3f green = new Color3f(0,1,0);
		
		boolean isBlue = true;
		for(int j=-10;j<9;j++) {
			for(int i = -10;i<9;i++) {
				Point3d t1 = new Point3d(i,0,j);
				Point3d t2 = new Point3d(i + 1,0,j);
				Point3d t3 = new Point3d(i + 1,0,j + 1);
				Point3d t4 = new Point3d(i,0,j + 1);
				ArrayList<Point3d> tileCoord = new ArrayList<Point3d>();
				
				tileCoord.add(t1);
				tileCoord.add(t2);
				tileCoord.add(t3);
				tileCoord.add(t4);
				if(isBlue)
					floor.addChild(new ColouredTile(tileCoord, blue));
				else
					floor.addChild(new ColouredTile(tileCoord, green));
				isBlue =! isBlue;
				floor.addChild(makeText(new Vector3f(i,0,j),"("+i+","+j+")"));
		}
	}
		return floor;
	}
	
	public TransformGroup makeText(Vector3f pt,String text) {
		Color3f white = new Color3f(1,0,0);
		Text2D message = new Text2D(text,white,"SansSerif", 36, Font.BOLD);
		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setTranslation(pt);
		tg.setTransform(t3d);
		tg.addChild(message);
		return tg;
	}
	
}
