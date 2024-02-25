import java.util.ArrayList;

import javax.media.j3d.*;
import javax.vecmath.*;
public class ColouredTile extends Shape3D{
	
	private ArrayList<Point3d>coord;
	private Color3f color;
	private QuadArray plane;
	
	public ColouredTile(ArrayList<Point3d>coord,Color3f color) {
		this.coord = coord;
		this.color = color;
		plane = new QuadArray(coord.size(),GeometryArray.COORDINATES|GeometryArray.COLOR_3);
		createGeometry();
		createAppearance();
	}
	
	public void createGeometry() {
		int numPoints = coord.size();
		Point3d[] points = new Point3d[numPoints];
		coord.toArray(points);
		plane.setCoordinates(0,points);
		
		Color3f[] colors = new Color3f[numPoints];
		for(int i=0;i<numPoints;i++)
			colors[i] = this.color;
		plane.setColors(0,colors);
		this.setGeometry(plane);
	}
	
	public void createAppearance() {
		Appearance app = new Appearance();
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		app.setPolygonAttributes(pa);
		this.setAppearance(app);
	}
}