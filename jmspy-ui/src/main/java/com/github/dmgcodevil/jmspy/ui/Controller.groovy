package com.github.dmgcodevil.jmspy.ui

import com.github.dmgcodevil.jmspy.graph.Edge
import com.github.dmgcodevil.jmspy.graph.InvocationGraph
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.stage.FileChooser
import javafx.stage.Stage

/**
 * Created by dmgcodevil on 11/18/2014.
 */
class Controller {

    private Stage stage;
    @FXML
    private TreeView<?> iGraphTree;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleMenuItemOpenGraph(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JMSpy File");
        File file = fileChooser.showOpenDialog(stage);
        InvocationGraph invocationGraph = InvocationGraph.load(file);
        drawGraph(invocationGraph);
    }

    private void drawGraph(InvocationGraph invocationGraph) {
        TreeItem<String> rootItem = new TreeItem<String>(invocationGraph.root.type.getName());
        rootItem.setExpanded(true);

        invocationGraph.root.outgoingEdges.each { it ->
            addItem(rootItem, it)
        }
        iGraphTree.setRoot(rootItem);
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
