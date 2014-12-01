package com.github.dmgcodevil.jmspy.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by dmgcodevil on 11/18/2014.
 */
public class JMSpyApp extends Application {

    private static final String JMSPY_TITLE = "JMSpy";

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(JMSpyApp.class.getResource("/ui_template.fxml"));
        Parent root = (Parent) loader.load();

        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle(JMSPY_TITLE);
        primaryStage.setScene(new Scene(root));
        controller.init();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(JMSpyApp.class, args);
    }
}

