package com.github.dmgcodevil.jmspy.ui

/**
 * Created by dmgcodevil on 11/17/2014.
 */
import com.github.dmgcodevil.jmspy.graph.Edge
import com.github.dmgcodevil.jmspy.graph.InvocationGraph;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by dmgcodevil on 11/16/2014.
 */
public class JMSpy extends Application {


    private static final int HIGH = 500
    public static final int WIGHT = 300

    @Override
    public void start(final Stage primaryStage) {

        VBox root = new VBox();

        Scene scene = new Scene(root, WIGHT, HIGH);
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");
        MenuItem openFileMenuItem = new MenuItem("Load file");
        def openFile = {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open JMSpy File");
            File file = fileChooser.showOpenDialog(primaryStage);
            InvocationGraph invocationGraph = InvocationGraph.load(file);
            drawGraph(root, invocationGraph);

        } as EventHandler<ActionEvent>

        openFileMenuItem.setOnAction(openFile);
        menuFile.getItems().addAll(openFileMenuItem);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        // --- Menu View
        Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);


        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void drawGraph(Pane pane, InvocationGraph invocationGraph) {
        TreeItem<String> rootItem = new TreeItem<String>(invocationGraph.root.type.getName());
        rootItem.setExpanded(true);

        invocationGraph.root.outgoingEdges.each { it ->
            addItem(rootItem, it)
        }


        TreeView<String> tree = new TreeView<String>(rootItem);
        StackPane root = new StackPane();
        pane.getChildren().add(tree);
        //primaryStage.setScene(new Scene(root, 300, 250));
    }

    public static void main(String[] args) {
        launch(JMSpy.class, args);
    }

    private void addItem(TreeItem<String> rootItem, Edge edge) {
        TreeItem<String> child = new TreeItem<String>(edge.getMethod().toString());
        rootItem.getChildren().add(child);
        if (edge.to != null) {
            child.setExpanded(false);
            edge.to.outgoingEdges.each {
                addItem(child, it)
            }
        }

    }
}
