package bluelight;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Bluelight extends Application {
    
    private List<Button> bluetoothButtons = new ArrayList<>();
    private BluetoothConnection connection;
    private LightController controller;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(new Scene(buildWindow(), 350, 400));
        primaryStage.show();
        connection = new BluetoothConnection(this);
        controller = new LightController(connection);
    }
    
    public Parent buildWindow() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setHgap(10);
        grid.setVgap(10);

        final CheckBox useStrobe = new CheckBox("randomize strobing");
        GridPane.setHalignment(useStrobe, HPos.CENTER);

        Button randomizeButton = new Button("Randomize lights");
        
        final Bluelight bluelight = this;
        
        randomizeButton.setOnAction(e -> {
        	controller.randomize(useStrobe.isSelected());
        });
        randomizeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        randomizeButton.setPrefHeight(1000);
        //randomizeButton.setDisable(true);
        bluetoothButtons.add(randomizeButton);

        Separator separator = new Separator();

        Label inputLabel = new Label("Set specific rgbps:");
        GridPane.setHalignment(inputLabel, HPos.CENTER);

        NumberTextField red = new NumberTextField();
        red.setPromptText("red");
        NumberTextField green = new NumberTextField();
        green.setPromptText("green");
        NumberTextField blue = new NumberTextField();
        blue.setPromptText("blue");
        NumberTextField position = new NumberTextField();
        position.setPromptText("position");
        NumberTextField strobe = new NumberTextField();
        strobe.setPromptText("strobe");

        Button setRgbps = new Button("set");
        setRgbps.setMaxWidth(Double.MAX_VALUE);
        setRgbps.setOnAction(e -> {
            int r = getValAndLimitToBounds(red);
            int g = getValAndLimitToBounds(green);
            int b = getValAndLimitToBounds(blue);
            int p = getValAndLimitToBounds(position);
            int s = getValAndLimitToBounds(strobe);
        	controller.setLights(r, g, b, p, s);
        });
        //setRgbps.setDisable(true);
        bluetoothButtons.add(setRgbps);
        
        Separator separator2 = new Separator();
        
        Button thingButton = new Button("Screen");
        thingButton.setOnAction(e -> {
	        JFrame frame = new JFrame();
	        JPanel panel = new JPanel();
	        frame.setContentPane(panel);
	        frame.setUndecorated(true);
	        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        
	        frame.setVisible(true);
        });
        
        Button rainbowButton = new Button("Rainbow");
        rainbowButton.setOnAction(e -> {
	        controller.rainbow();
        });
        
        Button fadeButton = new Button("Fade");
        fadeButton.setOnAction(e -> {
	        controller.fade();
        });
        
        Button randFast = new Button("Random Fast");
        randFast.setOnAction(e -> {
	        controller.randomizeRepeatFast();
        });
        
        Button randSlow = new Button("Random Slow");
        randSlow.setOnAction(e -> {
	        controller.randomizeRepeatSlow();
        });
        
        Button internet = new Button("Internet");
        internet.setOnAction(e -> {
	        controller.internet();
        });

        GridPane innerGrid = new GridPane();
        innerGrid.setHgap(10);
        innerGrid.setVgap(10);
        innerGrid.add(red, 0, 0);
        innerGrid.add(green, 1, 0);
        innerGrid.add(blue, 2, 0);
        innerGrid.add(position, 0, 1);
        innerGrid.add(strobe, 1, 1);
        innerGrid.add(setRgbps, 2, 1);

        grid.add(randomizeButton, 0, 0);
        grid.add(useStrobe, 0, 1);
        grid.add(separator, 0, 2);
        grid.add(inputLabel, 0, 3);
        grid.add(innerGrid, 0, 4);
        grid.add(separator2, 0, 5);
        //grid.add(thingButton, 0, 6);
        grid.add(rainbowButton, 0, 6);
        grid.add(fadeButton, 0, 7);
        grid.add(randFast, 0, 8);
        grid.add(randSlow, 0, 9);
        grid.add(internet, 0, 10);

        return grid;
    }
    
    public void enableButtons() {
        //for (Button b : bluetoothButtons) {
        //	System.out.println("Enabling: " + b.getText());
        //    b.setDisable(false);
        //}
    }

    public int getValAndLimitToBounds(NumberTextField ntf) {
        Integer val = ntf.getValue();
        if (val == null) {
            ntf.setValue(0);
            return 0;
        } else if (val > 255) {
            ntf.setValue(255);
            return 255;
        }
        return val;
    }
}
