/* 
 * Copyright 2019 Kamil Łobiński <kamilobinski@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package planetviewer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlanetViewer extends Application {

    private static final float WIDTH = 640;
    private static final float HEIGHT = 360;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Sphere sun = new Sphere(150);
    private final Sphere mercury = new Sphere(15);
    private final Sphere venus = new Sphere(19.5);
    private final Sphere earth = new Sphere(19.9);
    private final Sphere mars = new Sphere(16.2);
    private final Sphere jupiter = new Sphere(60);
    private final Sphere saturn = new Sphere(48);
    private final Circle saturnRing = new Circle(98.0);
    private final Sphere uranus = new Sphere(34);
    private final Sphere neptune = new Sphere(32);

    //private final Circle neptuneLine = new Circle(2075.66);
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    double screenX = primaryScreenBounds.getWidth();
    double screenY = primaryScreenBounds.getHeight();

    private PointLight light = new PointLight();

    private Image image = new Image(getClass().getResourceAsStream("/planet_textures/8k_stars_milky_way.jpg"));
    private ImageView imageView = new ImageView(image);

    final Label detailsLabel_1 = new Label("Star profile: ");
    final Label detailsLabel_2 = new Label("Age: ");
    final Label detailsLabel_3 = new Label("Type: ");
    final Label detailsLabel_4 = new Label("Diameter: ");
    final Label detailsLabel_5 = new Label("Circumference at Equator: ");
    final Label detailsLabel_6 = new Label("Mass: ");
    final Label detailsLabel_7 = new Label("Surface temperature: ");

    Label detailsText_1 = new Label();
    Label detailsText_2 = new Label();
    Label detailsText_3 = new Label();
    Label detailsText_4 = new Label();
    Label detailsText_5 = new Label();
    Label detailsText_6 = new Label();
    Label detailsText_7 = new Label();
    
    public VBox planetDetails = new VBox(7);
    public Scene detailsScene = new Scene(planetDetails);
    public Stage detailsStage = new Stage(StageStyle.TRANSPARENT);

    @Override
    public void start(Stage primaryStage) throws Exception {

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

        light.setColor(Color.rgb(196, 196, 196, 1));
        light.setTranslateZ(-1000);
        light.setTranslateX(+100);
        light.setTranslateY(+10);

        Group lightGroup = new Group();
        lightGroup.getChildren().add(light);

        SmartGroup planets = new SmartGroup();
        planets.translateZProperty().set(4000);
        planets.getChildren().addAll(prepareSun(), prepareMercury(), prepareEarth(), prepareVenus(), prepareMars(), prepareJupiter(), prepareSaturn(), prepareSaturnRing(), prepareUranus(), prepareNeptune());

        Group root = new Group();
        root.getChildren().add(planets);
        root.getChildren().add(prepareGalaxy());
        root.getChildren().add(lightGroup);

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setCursor(Cursor.DEFAULT);
        scene.setFill(Color.rgb(10, 10, 10));
        scene.setCamera(camera);
        
        initMouseControl(planets, scene, primaryStage);
        initKeyControl(planets, scene, primaryStage);

        primaryStage.setTitle("PlanetViewer");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);

        prepareAnimation();

        detailsLabel_1.setId("detailsLabel_1");
        detailsLabel_2.setId("detailsLabel_2");
        detailsLabel_3.setId("detailsLabel_3");
        detailsLabel_4.setId("detailsLabel_4");
        detailsLabel_5.setId("detailsLabel_5");
        detailsLabel_6.setId("detailsLabel_6");
        detailsLabel_7.setId("detailsLabel_7");

        detailsText_1.setId("detailsText_1");
        detailsText_2.setId("detailsText_2");
        detailsText_3.setId("detailsText_3");
        detailsText_4.setId("detailsText_4");
        detailsText_5.setId("detailsText_5");
        detailsText_6.setId("detailsText_6");
        detailsText_7.setId("detailsText_7");

        final HBox hbox1 = new HBox(detailsLabel_1, detailsText_1);
        final HBox hbox2 = new HBox(detailsLabel_2, detailsText_2);
        final HBox hbox3 = new HBox(detailsLabel_3, detailsText_3);
        final HBox hbox4 = new HBox(detailsLabel_4, detailsText_4);
        final HBox hbox5 = new HBox(detailsLabel_5, detailsText_5);
        final HBox hbox6 = new HBox(detailsLabel_6, detailsText_6);
        final HBox hbox7 = new HBox(detailsLabel_7, detailsText_7);

        planetDetails.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7);
        planetDetails.setBackground(Background.EMPTY);
        //planetDetails.setPadding(new Insets(20));

        detailsStage.initOwner(primaryStage);
        detailsStage.initModality(Modality.NONE);
        detailsStage.setScene(detailsScene);
        detailsStage.setX(100);
        detailsStage.setY(screenY - 275);
        detailsScene.setFill(Color.TRANSPARENT);
        detailsScene.getStylesheets().add(PlanetViewer.class.getResource("/stylesheet.css").toExternalForm());
    }

    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sun.rotateProperty().set(sun.getRotate() - 0.1);
                earth.rotateProperty().set(earth.getRotate() - 0.2);
                mercury.rotateProperty().set(mercury.getRotate() - 0.6);
                venus.rotateProperty().set(venus.getRotate() - 0.1);
                mars.rotateProperty().set(mars.getRotate() - 0.3);
                jupiter.rotateProperty().set(jupiter.getRotate() - 0.2);
                saturn.rotateProperty().set(saturn.getRotate() - 0.28);
                uranus.rotateProperty().set(uranus.getRotate() - 0.005);
                neptune.rotateProperty().set(neptune.getRotate() - 0.05);
                saturnRing.rotateProperty().set(saturnRing.getRotate() - 0.28);
            }
        };
        timer.start();
    }

    private ImageView prepareGalaxy() {
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 0));
        imageView.setTranslateZ(+6000);

        return imageView;
    }

    private Node prepareSun() {
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_sun.jpg")));
        sun.setRotationAxis(Rotate.Y_AXIS);
        sun.setMaterial(sunMaterial);
        return sun;
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/earthmap.jpg")));
        earth.setRotationAxis(Rotate.Y_AXIS);
        earth.setMaterial(earthMaterial);
        earth.getTransforms().add(new Translate(750.0, 0, 0));
        earth.rotateProperty().set(earth.getRotate() - 100.0);
        return earth;
    }

    private Node prepareMercury() {
        PhongMaterial mercuryMaterial = new PhongMaterial();
        mercuryMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_mercury.jpg")));
        mercury.setRotationAxis(Rotate.Y_AXIS);
        mercury.setMaterial(mercuryMaterial);
        mercury.getTransforms().add(new Translate(300.0, 0, 0));
        mercury.rotateProperty().set(mercury.getRotate() - 4000.0);
        return mercury;
    }

    private Node prepareVenus() {
        PhongMaterial venusMaterial = new PhongMaterial();
        venusMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_venus_atmosphere.jpg")));
        venusMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_venus_surface.jpg")));
        venus.setRotationAxis(Rotate.Y_AXIS);
        venus.setMaterial(venusMaterial);
        venus.getTransforms().add(new Translate(450.0, 0, 0));
        venus.rotateProperty().set(venus.getRotate() - 6500.0);
        return venus;
    }

    private Node prepareMars() {
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_mars.jpg")));
        mars.setRotationAxis(Rotate.Y_AXIS);
        mars.setMaterial(marsMaterial);
        mars.getTransforms().add(new Translate(600.0, 0, 0));
        mars.rotateProperty().set(mars.getRotate() - 1250.0);
        return mars;
    }

    private Node prepareJupiter() {
        PhongMaterial marsMaterial = new PhongMaterial();
        marsMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_jupiter.jpg")));
        jupiter.setRotationAxis(Rotate.Y_AXIS);
        jupiter.setMaterial(marsMaterial);
        jupiter.getTransforms().add(new Translate(1050.0, 0, 0));
        jupiter.rotateProperty().set(jupiter.getRotate() - 400.0);
        return jupiter;
    }

    private Node prepareSaturn() {
        PhongMaterial saturnMaterial = new PhongMaterial();
        saturnMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_saturn.jpg")));
        saturn.setRotationAxis(Rotate.Y_AXIS);
        saturn.setMaterial(saturnMaterial);
        saturn.getTransforms().add(new Translate(1350.0, 0, 0));
        saturn.rotateProperty().set(saturn.getRotate() - 1000.0);
        return saturn;
    }

    private Node prepareSaturnRing() {
        saturnRing.setRotationAxis(Rotate.Y_AXIS);
        saturnRing.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/planet_textures/2k_saturn_ring_alpha2.png"))));

        Translate translate = new Translate();
        translate.setX(1350.0);
        translate.setY(0);
        translate.setZ(0);

        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(90);
        ryBox.setAngle(20);
        rzBox.setAngle(0);

        saturnRing.getTransforms().addAll(translate, rxBox, ryBox, rzBox);
        saturnRing.rotateProperty().set(saturnRing.getRotate() - 1000.0);
        return saturnRing;
    }

    private Node prepareUranus() {
        PhongMaterial uranusMaterial = new PhongMaterial();
        uranusMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_uranus.jpg")));
        uranus.setRotationAxis(Rotate.Y_AXIS);
        uranus.setMaterial(uranusMaterial);
        uranus.getTransforms().add(new Translate(1550.0, 0, 0));
        uranus.rotateProperty().set(venus.getRotate() - 2600.0);
        return uranus;
    }

    private Node prepareNeptune() {
        PhongMaterial neptuneMaterial = new PhongMaterial();
        neptuneMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/planet_textures/2k_neptune.jpg")));
        neptune.setRotationAxis(Rotate.Y_AXIS);
        neptune.setMaterial(neptuneMaterial);
        neptune.getTransforms().add(new Translate(1700.0, 0, 0));
        return neptune;
    }

    /* lines   
    private Node prepareNeptuneLine() {
        new Thread(() -> {
        neptuneLine.setFill(Color.TRANSPARENT);
        neptuneLine.setStroke(Color.WHITE);
        neptuneLine.setStrokeWidth(1);
        Translate translate = new Translate();       
        translate.setX(0); 
        translate.setY(0); 
        translate.setZ(0);
        
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS); 
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS); 
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS); 
        rxBox.setAngle(90); 
        ryBox.setAngle(0); 
        rzBox.setAngle(0); 
        neptuneLine.getTransforms().addAll(translate,rxBox, ryBox, rzBox);
        }).start();
        return neptuneLine;
    }*/
    
    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();

        });
        scene.setOnMouseDragged(event -> {
            scene.setCursor(Cursor.MOVE);
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - (anchorX - event.getSceneX()));

        });
        
        scene.setOnMouseReleased(event -> {
            scene.setCursor(Cursor.DEFAULT);
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            if (group.getTranslateZ() + delta < 4000) {
                group.translateZProperty().set(group.getTranslateZ() + delta);
            } else {
            }
        });
        
        sun.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Sun");
            detailsText_2.setText("4.6 Billion Years");
            detailsText_3.setText("Yelow Dwarf");
            detailsText_4.setText("1,392,684 km");
            detailsText_5.setText("4,366,813 km");
            detailsText_6.setText("1,989E30 kg");
            detailsText_7.setText("5500°C");
            detailsStage.show();
        });
        
        mercury.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Mercury");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Terrestial");
            detailsText_4.setText("4,879,4 km");
            detailsText_5.setText("15,329 km");
            detailsText_6.setText("3,285E23 kg");
            detailsText_7.setText("167°C");
            detailsStage.show();
        });
        
        venus.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Venus");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Terrestial");
            detailsText_4.setText("12,104 km");
            detailsText_5.setText("40,075 km");
            detailsText_6.setText("4,867E24 kg");
            detailsText_7.setText("462°C");
            detailsStage.show();
        });
        
        earth.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Earth");
            detailsText_2.setText("4.5 Billion Years"); //Age
            detailsText_3.setText("Terrestrial"); //Type
            detailsText_4.setText("12,742 km"); //Diameter
            detailsText_5.setText("40,075 km"); //Circumference at Equator
            detailsText_6.setText("5,972E24 kg"); //Mass
            detailsText_7.setText("average 14°C"); //Temperature
            detailsStage.show();
        });
        
        mars.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Mars");
            detailsText_2.setText("4.6 Billion Years");
            detailsText_3.setText("Terrestial");
            detailsText_4.setText("6,779 km");
            detailsText_5.setText("21,343 km");
            detailsText_6.setText("6,39E23 kg");
            detailsText_7.setText("-60°C");
            detailsStage.show();
        }); 
        
        jupiter.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Jupiter");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Gas giant");
            detailsText_4.setText("139,820 km");
            detailsText_5.setText("439,264 km");
            detailsText_6.setText("1,898E27 kg");
            detailsText_7.setText("-145°C");
            detailsStage.show();
        }); 
        
        saturn.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Saturn");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Gas giant");
            detailsText_4.setText("116,460 km");
            detailsText_5.setText("378,675 km");
            detailsText_6.setText("5,683E26 kg");
            detailsText_7.setText("-178°C");
            detailsStage.show();
        }); 
        
        uranus.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Uranus");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Blue planet");
            detailsText_4.setText("50,724 km");
            detailsText_5.setText("160,590 km");
            detailsText_6.setText("8,681E25 kg");
            detailsText_7.setText("-197°C");
            detailsStage.show();
        }); 
        
        neptune.setOnMouseClicked((event) -> {
            detailsStage.close();
            detailsText_1.setText("Neptune");
            detailsText_2.setText("4.5 Billion Years");
            detailsText_3.setText("Gas giant");
            detailsText_4.setText("49,244 km");
            detailsText_5.setText("155,600 km");
            detailsText_6.setText("1,024E26 kg");
            detailsText_7.setText("-214°C");
            detailsStage.show();
        }); 
        
        imageView.setOnMouseClicked((event) -> {
            //detailsStage.hide();
            detailsStage.close();
        });

        imageView.setOnMouseDragEntered((event) -> {
            detailsStage.close();
        });
    }
    
    private void initKeyControl(SmartGroup group, Scene scene, Stage stage) {
        scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

