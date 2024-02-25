import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class box_roatation extends Application {
    int t=0;
    private double anchorX=0;
    private  double anchorY=0;
    private double anchrangleX=0;
    private double anchrangleY=0;
    private final DoubleProperty angleX=new SimpleDoubleProperty(0);
    private final DoubleProperty angley=new SimpleDoubleProperty(0);
    private final Box box=prepareBox();

    private Box prepareBox() {
        Box box=new Box(50,50,50);
        PhongMaterial material=new PhongMaterial();
        material.setDiffuseMap(new Image("file:///G:/untitled3/.jpg"));
        material.setSpecularColor(Color.WHITE);
        box.setMaterial(material);
        return  box;
    }

    @Override
    public void start(Stage stage) throws Exception {

        //camera
        Camera camera=new PerspectiveCamera();

        //box


        my_group mg=new my_group();

        mg.getChildren().addAll(prepareligh());
        mg.getChildren().add(box);
        mg.getChildren().add(new AmbientLight());
        //translate
        mg.translateXProperty().set(250);
        mg.translateYProperty().set(250);
        mg.translateZProperty().set(-500);
        Sphere s=new Sphere(50);
        s.translateXProperty().set(120);
        s.translateYProperty().set(200);
        //mg.getChildren().add(s);


        //control
        stage.addEventHandler(KeyEvent.KEY_PRESSED,keyEvent -> {
            switch (keyEvent.getCode()){
                case A:

                    mg.rotateByX(10);
                    break;
                case B:
                    mg.rotateByX(-10);
                    break;
                case C:

                    mg.rotateByY(10);
                    break;
                case D:
                    mg.rotateByY(-10);
                    break;
            }
        });
        //duration
        Duration strt=Duration.ZERO;
        Duration end=Duration.seconds(10);
        //keyframe
        //transition
        Circle c=new Circle(150,150,100);
        /*PathTransition transition=new PathTransition();
        transition.setNode(box);
        transition.setDuration(Duration.seconds(5));
        transition.setPath(c);
        transition.setCycleCount(PathTransition.INDEFINITE);
        transition.play();*/


        //scene
        Group root=new Group();
        root.getChildren().add(0,mg);
        // root.getChildren().add(prepareligh());
        Scene scene=new Scene(root,500,500);
        scene.setFill(Color.AQUAMARINE);
        scene.setCamera(camera);
        //mouse control
        initMouseControl(mg,scene);
        //animation
        animation();


        //stage
        stage.setScene(scene);
        stage.show();
    }
    private final PointLight pointLight=new PointLight();

    private Node[] prepareligh() {

        pointLight.getTransforms().add(new Translate(0,-50,100));

        pointLight.setColor(Color.RED);
        pointLight.setRotationAxis(Rotate.X_AXIS);
        Sphere sphere=new Sphere(10);
        sphere.rotateProperty().bind(pointLight.rotateProperty());
        sphere.rotationAxisProperty().bind(pointLight.rotationAxisProperty());
        sphere.getTransforms().setAll(pointLight.getTransforms());
        return new Node[]{pointLight,sphere};

    }

    private void animation() {
        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                //box.setRotationAxis(new Point3D(0,1,0));
                //box.rotateProperty().set(box.getRotate()+1);
                pointLight.setRotate(pointLight.getRotate()+1);

            }
        };
        timer.start();
    }

    private void initMouseControl(my_group mg, Scene scene) {
        Rotate xrotate;
        Rotate yrotate;
        mg.getTransforms().addAll(xrotate=new Rotate(0,Rotate.X_AXIS),
                yrotate=new Rotate(0,Rotate.Y_AXIS));
        xrotate.angleProperty().bind(angleX);
        yrotate.angleProperty().bind(angley);

        scene.setOnMousePressed(mouseEvent -> {
            anchorX=mouseEvent.getSceneX();
            anchorY=mouseEvent.getSceneY();
            anchrangleX=angleX.get();
            anchrangleY=angley.get();
            System.out.println(anchrangleX+" "+anchrangleY);
        });
        scene.setOnMouseDragged(mouseEvent -> {
            angleX.set(anchrangleX-(anchorY-mouseEvent.getSceneY()));
            angley.set(anchrangleY+(anchorX-mouseEvent.getSceneX()));
        });
    }

    public static void main(String[] args){
        launch(args);
    }
    class my_group extends Group{
        Rotate r;
        Transform t=new Rotate();
        public void rotateByX(int deg){
            r=new Rotate(deg,Rotate.X_AXIS);
            t=t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
        public void rotateByY(int deg){
            r=new Rotate(deg,Rotate.Y_AXIS);
            t=t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
