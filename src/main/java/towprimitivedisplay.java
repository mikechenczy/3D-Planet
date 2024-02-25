import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.applet.Applet;
import java.awt.*;

public class towprimitivedisplay extends Applet {
    public BranchGroup createBranchGroup()
    {
        //����BranchGroup
        BranchGroup BranchGroupRoot = new BranchGroup ();

        //��������������ϵԭ�����η�Χ
        BoundingSphere bounds = new BoundingSphere(new Point3d( 0.0,0.0,0.0),1.0);

        //���屳����ɫ
        Color3f bgColor=new Color3f(.0f,.0f,.0f);
        Background bg=new Background(bgColor);
        bg.setApplicationBounds(bounds);
        BranchGroupRoot.addChild(bg);

        //����ƽ�й⡢��ɫ�����䷽�������÷�Χ
        Color3f directionColor=new Color3f(1.f,1.f,1.f);
        Vector3f vec=new Vector3f(1.0f,-1.0f,-1.0f);
        DirectionalLight directionalLight= new DirectionalLight(directionColor,vec);
        directionalLight.setInfluencingBounds(bounds);
        BranchGroupRoot.addChild(directionalLight);

        //�����ܵ�TransformGroup:transformgroup
        TransformGroup transformgroup=new TransformGroup();

        //���öԸ�TransformGroup�Ķ�д����
        transformgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        //����TransformGroup���뵽BranchGroupRoot��
        BranchGroupRoot.addChild(transformgroup);

        //�������Գ�������ת��ƽ����Ŵ���
        MouseRotate mouserotate=new MouseRotate();
        mouserotate.setTransformGroup(transformgroup);
        BranchGroupRoot.addChild(mouserotate);
        mouserotate.setSchedulingBounds(bounds);

        MouseZoom mousezoom=new MouseZoom();
        mousezoom.setSchedulingBounds(bounds);
        BranchGroupRoot.addChild(mousezoom);
        mousezoom.setSchedulingBounds(bounds);

        MouseTranslate mousetranslate=new MouseTranslate();
        mousetranslate.setTransformGroup(transformgroup);
        BranchGroupRoot.addChild(mousetranslate);
        mousetranslate.setSchedulingBounds(bounds);

        //����������ά��������
        Appearance app1=new Appearance();
        Material material1=new Material();
        material1.setDiffuseColor(new Color3f(1.0f,.0f,0.0f));
        app1.setMaterial(material1);

        Appearance app2=new Appearance();
        Material material2=new Material();
        material2.setDiffuseColor(new Color3f(0.0f,1.0f,0.0f));
        app2.setMaterial(material2);

        //����һ��������һ��������Ĵ�С���������������任����������Ӧ��TransformGroup��ta1��ta2
        TransformGroup tg1=new TransformGroup();
        tg1.addChild(new Sphere(0.3f,app1));
        Transform3D t=new Transform3D();
        t.setTranslation(new Vector3f(0.f,-0.425f,0.f));
        TransformGroup tg2=new TransformGroup(t);
        tg2.addChild(new Box(0.5f,0.05f,0.5f,app2));

        //������õ�����TransformGroup(tg1��tg2)���뵽�ܵ�transformgroup
        transformgroup.addChild(tg1);
        transformgroup.addChild(tg2);

        //��BranchGroupRootԤ����
        BranchGroupRoot.compile();

        //ͨ������������BranchGroupRoot
        return BranchGroupRoot;
    }
    public towprimitivedisplay()
    {
        //������ʾ�������ز���
        setLayout(new BorderLayout());
        //����ͶӰƽ��Canvas3D
        GraphicsConfiguration gc=SimpleUniverse.getPreferredConfiguration();
        Canvas3D c=new Canvas3D(gc);
        //��ͶӰƽ���ϵ�ͼ����ʾ����ʾƽ����м�
        add("Center",c);
        //����SimpleUniverse����ϵͳѡ���ӵ���z������򣬹۲췽����z�ᷴ��
        BranchGroup BranchGroupScene=createBranchGroup();
        SimpleUniverse u=new SimpleUniverse(c);
        u.getViewingPlatform().setNominalViewingTransform();
        //��BranchGroup:BranchGroupScene���뵽SimpleUniverse:u��
        u.addBranchGraph(BranchGroupScene);
    }

    public static void main(String[] args)
    {//ͨ��MainFrame��ʾͼ��

        new MainFrame(new towprimitivedisplay(),300,300);

    }
}
