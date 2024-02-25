import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class sphere extends Application {
    Sphere sp1=prepareSphere();

    private Sphere prepareSphere() {
        Sphere sp=new Sphere(50);
        PhongMaterial material=new PhongMaterial();
        material.setDiffuseMap(new Image("file:///G:/untitled3/world.jpg"));
        sp.setMaterial(material);
        return  sp;
    }
    private double anchorX=0;
    private  double anchorY=0;
    private double anchrangleX=0;
    private double anchrangleY=0;
    private final DoubleProperty angleX=new SimpleDoubleProperty(0);
    private final DoubleProperty angley=new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) throws Exception {

        //camera
        Camera camera=new PerspectiveCamera();

        //button
        Text t=new Text();
        Button stp_orbit=new Button("STP_planet");
        Button strt_orbit=new Button("STRT_planet");
        Button stp_spin=new Button("STP_Spin");
        Button strt_spin=new Button("STRT_Spin");
        Button stp_sat=new Button("Stp_SAT");
        Button strt_sat=new Button("Strt_SAT");
        //sliders
        Slider sliderZ=new Slider();
        sliderZ.setPrefSize(500,20);
        sliderZ.setMax(200);
        sliderZ.setMin(-200);
        sliderZ.setValue(0);

        Slider sliderX=new Slider();
        sliderX.setPrefSize(500,80);
        sliderX.setMax(150);
        sliderX.setMin(-150);
        sliderX.setValue(0);

        Slider sliderY=new Slider();
        sliderY.setPrefSize(500,140);
        sliderY.setMin(-150);
        sliderY.setMax(150);
        sliderY.setValue(0);



        //background
        Image back=new Image("file:///G:/untitled3/background.jpg");
        ImageView view=new ImageView(back);
        view.translateXProperty().set(-2500);
        view.translateYProperty().set(-1200);
        view.translateZProperty().set(5000);

        //planet creation
        planet merqury=new planet(5);
        merqury.setposition(660,400);
        merqury.setTexture(new Image("file:///G:/untitled3/merqury.jpg"));

        planet venus=new planet(7);
        venus.setposition(680,400);
        venus.setTexture(new Image("file:///G:/untitled3/venus.jpg"));

        planet earth=new planet(10);
        earth.setposition(700,400);
        earth.setTexture(new Image("file:///G:/untitled3/world.jpg"));
        //moon
        satalite moon=new satalite(earth,5);
        moon.setTex(new Image("file:///G:/untitled3/moon.jpg"));


        planet mars=new planet(8);
        mars.setposition(800,400);
        mars.setTexture(new Image("file:///G:/untitled3/mars.jpg"));
        //deimos
        satalite deimos=new satalite(mars,5);
        deimos.setTex(new Image("file:///G:/untitled3/deimos.jpg"));
        //phobos
        satalite phobos=new satalite(mars,3);
        phobos.setTex(new Image("file:///G:/untitled3/phobos.jpg"));

        planet jupiter=new planet(20);
        jupiter.setposition(900,400);
        jupiter.setTexture(new Image("file:///G:/untitled3/jupiter.jpg"));
        //europa
        satalite europa=new satalite(jupiter,5);
        europa.setTex(new Image("file:///G:/untitled3/europa.jpg"));
        //calisto
        satalite calisto=new satalite(jupiter,6);
        calisto.setTex(new Image("file:///G:/untitled3/callisto.jpg"));
        //ganymade
        satalite ganymade=new satalite(jupiter,8);
        ganymade.setTex(new Image("file:///G:/untitled3/ganymade.jpg"));
        //lo
        satalite lo=new satalite(jupiter,6);
        lo.setTex(new Image("file:///G:/untitled3/lo.jpg"));

        planet saturn=new planet(15);
        saturn.setposition(1000,400);
        saturn.setTexture(new Image("file:///G:/untitled3/saturn.jpg"));
        saturn.ellipse.translateYProperty().set(400);
        //titan
        satalite titan=new satalite(saturn,7);
        titan.setTex(new Image("file:///G:/untitled3/titan.jpg"));



        //sun creation
        star sun=new star(50);
        sun.position(650,400,0);
        sun.setTex(new Image("file:///G:/untitled3/sun.jpg"));

        //earth.translateXProperty().set(250);
        //earth.translateYProperty().set(250);
        //earth.translateZProperty().set(-500);



        stp_orbit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                earth.radtimer.stop();
                merqury.radtimer.stop();
                venus.radtimer.stop();
                mars.radtimer.stop();
                jupiter.radtimer.stop();
                saturn.radtimer.stop();
            }
        });
        strt_orbit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                merqury.radtimer.start();
                earth.radtimer.start();
                venus.radtimer.start();
                mars.radtimer.start();
                jupiter.radtimer.start();
                saturn.radtimer.start();
            }
        });

        //transition

        Circle c=new Circle(250,250,150);
        Sphere s2=new Sphere(100);

        /*PathTransition transition=new PathTransition();
        transition.setNode(earth.pl);
        transition.setDuration(Duration.seconds(5));
        transition.setPath(c);
        transition.setCycleCount(PathTransition.INDEFINITE);
        //transition.play();*/
        //stage and scene
        GridPane button=new GridPane();
        button.addRow(0,sliderZ);
        button.addRow(1,sliderX,sliderY);
        button.addRow(0,t,stp_orbit,strt_orbit,stp_sat,strt_sat);
        button.setStyle("-fx-spacing: 10;"+"-fx-padding: 10;");
        Group solar=new Group();
        solar.getChildren().addAll(sun.body,sun.pointLight,earth.pl,moon.body,mars.pl,deimos.body,phobos.body,merqury.pl,venus.pl,
                jupiter.pl,europa.body,calisto.body,ganymade.body,lo.body,saturn.pl,titan.body);
        stp_sat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               moon.orbiting_timer.stop();
               deimos.orbiting_timer.stop();
               phobos.orbiting_timer.stop();
               europa.orbiting_timer.stop();
               calisto.orbiting_timer.stop();
               ganymade.orbiting_timer.stop();
               lo.orbiting_timer.stop();
               titan.orbiting_timer.stop();
            }
        });
        strt_sat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                moon.orbiting_timer.start();
                deimos.orbiting_timer.start();
                phobos.orbiting_timer.start();
                europa.orbiting_timer.start();
                calisto.orbiting_timer.start();
                ganymade.orbiting_timer.start();
                lo.orbiting_timer.start();
                titan.orbiting_timer.start();
            }
        });

        /*zoom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                solar.translateZProperty().set(solar.getTranslateZ()-50);
            }
        });
        mini.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                solar.translateZProperty().set(solar.getTranslateZ()+50);
            }
        });*/
        sliderZ.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                solar.translateZProperty().set(sliderZ.getValue()*10);
            }
        });
        sliderX.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                solar.translateXProperty().set(sliderX.getValue()*10);
            }
        });
        sliderY.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                solar.translateYProperty().set(sliderY.getValue()*10);
            }
        });



        Group root=new Group();
        //root.getChildren().add(0,view);

        root.getChildren().add(0,button);
        root.getChildren().add(1,solar);
        //root.getChildren().add(2,earth.pl);

        root.setStyle("-fx-padding: 20;"+"-fx-spacing: 20;");
        Scene scene=new Scene(root,1500,800,true);
        //scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        scene.setFill(Color.BLACK);

        initMouseControl(solar,scene);
        //animation
        //orbit and self spin
        merqury.spin(5);
        merqury.circular_move(2,150,130,0);
        venus.spin(-5);
        venus.circular_move(-1.7,200,160,50);
        earth.spin(-4);
        earth.circular_move(1.3,380,360,100);
        moon.orbit(.05,0,40,40,40);
        moon.spin(2);
        //earth.move(250,200);
        mars.spin(4);
        mars.circular_move(1.2,550,510,60);
        deimos.orbit(.05,20,40,20,30);
        deimos.spin(3);
        phobos.orbit(-.04,10,20,40,30);
        phobos.spin(2);

        jupiter.spin(3);
        jupiter.circular_move(1.1,900,850,20);
        europa.orbit(.05,0,40,50,45);
        europa.spin(2);
        calisto.orbit(-.06,10,30,45,30);
        calisto.spin(3);
        ganymade.orbit(.03,60,50,55,40);
        ganymade.spin(4);
        lo.orbit(.04,30,60,60,50);
        lo.spin(3);

        saturn.spin(6);
        saturn.circular_move(.9,1050,1020,30);
        titan.orbit(.05,0,60,40,50);
        titan.spin(3);


        sun.spin(1);
        //animation();

        //stage
        stage.setScene(scene);
        stage.show();

    }


    private void animation() {
        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long l) {
                sp1.setRotationAxis(new Point3D(0,1,0));
               sp1.rotateProperty().set(sp1.getRotate()+2);
            }
        };
        timer.start();

    }
    class planet {
        double sweeprad;
        private double change=0;

        double pi=3.1416;
        public Sphere pl;
        Ellipse ellipse=new Ellipse(50,30);


        Box st_ring=new Box(50,1,10);
        AnimationTimer spintimer;
        //AnimationTimer movetimer;
        AnimationTimer radtimer;
        boolean left=true;
        boolean right=false;

        planet(int r){
            pl=new Sphere(r);


            //pl.setTranslateX();
            //pl.setTranslateY(150);
        }
        void setposition(double x,double y){
            pl.translateXProperty().set(x);
            pl.translateYProperty().set(y);

        }
        void setTexture(Image image){
            PhongMaterial material=new PhongMaterial();
            material.setDiffuseMap(image);
            pl.setMaterial(material);
        }
        void spin(int deg){
            spintimer=new AnimationTimer() {
                @Override
                public void handle(long l) {

                    pl.rotateProperty().set(pl.getRotate()+deg);
                    pl.setRotationAxis(new Point3D(0,1,0));
                    /*if(deg==6){

                        ellipse.translateXProperty().set(pl.getTranslateX());
                        ellipse.translateZProperty().set(pl.getTranslateZ());

                    }*/
                }
            };spintimer.start();
        }
        /*void move(int a,int b){
            /*movetimer=new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if(left) {
                        pl.translateXProperty().set(pl.getTranslateX() - 1);
                        double y=1-Math.pow((pl.getTranslateX()-600)/a,2);
                        pl.translateZProperty().set(20-b*Math.sqrt(y));
                        if(pl.getTranslateX()<(600-a+1)){
                            left=false;
                            right=true;
                        }
                    }
                    else if(right){
                        pl.translateXProperty().set(pl.getTranslateX()+1);
                        double y=1-Math.pow((pl.getTranslateX()-600)/a,2);
                        pl.translateZProperty().set(20+b*Math.sqrt(y));
                        if(pl.getTranslateX()>(600+a-2)){
                            right=false;
                            left=true;
                        }
                    }


                }
            };movetimer.start();
        }*/
        void circular_move(double deg,double a,double b,double initial){
            sweeprad=deg*pi/180;
            change=initial*pi/180;

            radtimer=new AnimationTimer() {

                @Override
                public void handle(long l) {
                    if(pl.getTranslateX()<600+a &&pl.getTranslateX()>600)
                        change +=sweeprad;

                    else
                        change +=(sweeprad+.01);

                    pl.translateXProperty().set(600+a* Math.cos(change));
                    pl.translateZProperty().set(20+b*Math.sin(change));

                }
            };radtimer.start();

        }



    }
    class satalite{
        Sphere body;
        planet host;
        private double change=0;
        AnimationTimer spintimer;
        AnimationTimer orbiting_timer;
        satalite(planet host,double r){
            this.host=host;
            body=new Sphere(r);
            body.translateYProperty().set(host.pl.getTranslateY()-20);

        }
        void  setTex(Image image){
            PhongMaterial material=new PhongMaterial();
            material.setDiffuseMap(image);
            body.setMaterial(material);
        }
        void orbit(double sweeprad,double init,double a,double b,double c){
            change=init;
            orbiting_timer=new AnimationTimer() {


                public void handle(long l) {
                change+=sweeprad;
                    body.translateXProperty().set(host.pl.getTranslateX()+a* Math.cos(change));

                    body.translateYProperty().set(400+b*Math.sin(change));
                    body.translateZProperty().set(host.pl.getTranslateZ()+c*Math.sin(change));
                }
            };
            orbiting_timer.start();
        }
        void spin(double deg){
            spintimer=new AnimationTimer() {
                @Override
                public void handle(long l) {

                    body.rotateProperty().set(body.getRotate()+deg);
                    body.setRotationAxis(Rotate.Y_AXIS);

                }
            };
            spintimer.start();
        }
    }
    class star{
        Sphere body;
        AnimationTimer spintimer;
        PointLight pointLight=new PointLight();
        star(int r){
            body=new Sphere(50);
        }
        void position( double x,double y,double z){
            body.translateXProperty().set(x);
            body.translateYProperty().set(y);
            body.translateZProperty().set(z);
            pointLight.getTransforms().add(new Translate(x,y,z));
        }


        void setTex(Image image){
            PhongMaterial material=new PhongMaterial();
            material.setDiffuseMap(image);
            material.setSelfIlluminationMap(new Image("file:///G:/untitled3/sun.jpg"));
            body.setMaterial(material);
        }
        void spin(int deg){
            spintimer=new AnimationTimer() {

                @Override
                public void handle(long l) {

                    body.setRotationAxis(new Point3D(0,1,0));
                    body.rotateProperty().set(body.getRotate()+deg);
                }
            };spintimer.start();
        }

    }
    private void initMouseControl(Group mg, Scene scene) {
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

    public static void main(String []args){
        launch(args);
    }
}

///this is my first attempt to make a 3d solar system
