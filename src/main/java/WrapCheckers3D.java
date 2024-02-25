import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.*;
public class WrapCheckers3D extends JPanel{
	private BranchGroup sceneBg;//sceneBg是一个新的模型，用BranchGroup来实现
	private SimpleUniverse su;
	/*这个类构架起一个最小的使用者环境以便快速并且简单的得到一个Java 3D 应用程序并且运行它。
	 * 这个有用的类创建了所有必要的对象在以视角为主场景表面的一边。很明显，它也创建了一个locale，一个单独的ViewingPlatform，和一个Viewer对象(这两个对象的参数使用的都是默认值)。
	 * 从很多基础的Java 3D 应用软件会发现SimpleUniverse提供了所有必要的需要他们的程序实现的方法。从更多老一点的应用程序也可能发现他们需要更多的控制在命令得到更多的方法里，并且不能使用这个类 
	 * 也就是说，这个类创建了观察者模式的应用
	*/
	private BoundingSphere bounds;//创造球体
	private static final float BOUNDSIZE=10.0f;
	
	public WrapCheckers3D(int width,int height) {
		this.setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		this.add("Center",canvas);
		canvas.setBounds(0,0,width,height);
		
		su = new SimpleUniverse(canvas);
		createSceneGroup();
		initUserPosition();
		orbitControls(canvas);
		su.addBranchGraph(sceneBg);
	}
	
	public void createSceneGroup() {
		sceneBg = new BranchGroup();
		bounds = new BoundingSphere(new Point3d(0,10,0),BOUNDSIZE);
		
		lightScene();
		addBackground();
		sceneBg.addChild(new CheckerFloor().getBG());//运用的多个方法在下面和其他类中
		floatingSphere();
		sceneBg.compile();
	}
	public void lightScene() {
		Color3f white = new Color3f(1.0f,1.0f,1.0f);
		AmbientLight ambientLightNode = new AmbientLight(white);
		ambientLightNode.setInfluencingBounds(bounds);
		sceneBg.addChild(ambientLightNode);
		
		Vector3f lightDirection = new Vector3f(-1.0f,-1.0f,-1.0f);//设置角度
		DirectionalLight light = new DirectionalLight(white,lightDirection);//设置光照
		
		light.setInfluencingBounds(bounds);
		sceneBg.addChild(light);//addChild是一个普遍的从模型中增加例子
	}
	private void addBackground() {
		Background back = new Background();
		back.setApplicationBounds(bounds);
		back.setColor(0.17f,0.62f,0.92f);
		sceneBg.addChild(back);
	}
	private void floatingSphere() {
		Color3f black = new Color3f(0.0f,0.0f,0.0f);
		Color3f blue = new Color3f(0.3f,0.3f,0.8f);
		Color3f specular = new Color3f(0.9f,0.9f,0.9f);//设置颜色
		
		Material blueMat = new Material(blue,black,blue,specular,25.0f);
		blueMat.setLightingEnable(true);
		
		Appearance blueApp = new Appearance();
		blueApp.setMaterial(blueMat);
		
		Transform3D t3d = new Transform3D();
		t3d.set(new Vector3f(0,4,0));
		TransformGroup tg = new TransformGroup(t3d);
		tg.addChild(new Sphere(2.0f,blueApp));
		sceneBg.addChild(tg);
	}
	private void initUserPosition() {
		ViewingPlatform vp =su.getViewingPlatform();
		TransformGroup steerTG = vp.getViewPlatformTransform();
		
		Transform3D t3d = new Transform3D();
		steerTG.getTransform(t3d);
		
		t3d.lookAt(new Point3d(0,5,20),new Point3d(0,0,0),new Vector3d(0,1,0));
		t3d.invert();
		
		steerTG.setTransform(t3d);
	}
	private void orbitControls(Canvas3D canvas) {
		OrbitBehavior orbit = new OrbitBehavior(canvas,OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(bounds);
		ViewingPlatform vp = su.getViewingPlatform();
		vp.setViewPlatformBehavior(orbit);
	}
}