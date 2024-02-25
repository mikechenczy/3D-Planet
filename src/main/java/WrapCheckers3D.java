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
	private BranchGroup sceneBg;//sceneBg��һ���µ�ģ�ͣ���BranchGroup��ʵ��
	private SimpleUniverse su;
	/*����๹����һ����С��ʹ���߻����Ա���ٲ��Ҽ򵥵ĵõ�һ��Java 3D Ӧ�ó�������������
	 * ������õ��ഴ�������б�Ҫ�Ķ��������ӽ�Ϊ�����������һ�ߡ������ԣ���Ҳ������һ��locale��һ��������ViewingPlatform����һ��Viewer����(����������Ĳ���ʹ�õĶ���Ĭ��ֵ)��
	 * �Ӻܶ������Java 3D Ӧ������ᷢ��SimpleUniverse�ṩ�����б�Ҫ����Ҫ���ǵĳ���ʵ�ֵķ������Ӹ�����һ���Ӧ�ó���Ҳ���ܷ���������Ҫ����Ŀ���������õ�����ķ�������Ҳ���ʹ������� 
	 * Ҳ����˵������ഴ���˹۲���ģʽ��Ӧ��
	*/
	private BoundingSphere bounds;//��������
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
		sceneBg.addChild(new CheckerFloor().getBG());//���õĶ���������������������
		floatingSphere();
		sceneBg.compile();
	}
	public void lightScene() {
		Color3f white = new Color3f(1.0f,1.0f,1.0f);
		AmbientLight ambientLightNode = new AmbientLight(white);
		ambientLightNode.setInfluencingBounds(bounds);
		sceneBg.addChild(ambientLightNode);
		
		Vector3f lightDirection = new Vector3f(-1.0f,-1.0f,-1.0f);//���ýǶ�
		DirectionalLight light = new DirectionalLight(white,lightDirection);//���ù���
		
		light.setInfluencingBounds(bounds);
		sceneBg.addChild(light);//addChild��һ���ձ�Ĵ�ģ������������
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
		Color3f specular = new Color3f(0.9f,0.9f,0.9f);//������ɫ
		
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