package com.github.dmgcodevil.jmspy.ui

import com.github.dmgcodevil.jmspy.graph.Edge
import com.github.dmgcodevil.jmspy.graph.InvocationGraph
import com.github.dmgcodevil.jmspy.proxy.JMethod
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBoxBuilder
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.commons.lang3.StringUtils

/**
 * Created by dmgcodevil on 11/18/2014.
 */
class Controller {

    private Stage stage;
    @FXML
    private TreeView<String> iGraphTree;

    @FXML
    private TextField filterEdit;

    @FXML
    private Button applyFilterBtn;

    @FXML
    private TextField skipMethodEdit;

    @FXML
    private Button addSkipMethodBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ListView skipMethodList;

    @FXML
    private Button clearSkipMethodListBtn;

    private InvocationGraph invocationGraph;

    private String filterValue;

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        TreeItem<String> rootItem = new TreeItem<String>("empty");
        iGraphTree.setRoot(rootItem);
        skipMethodList.getItems().addAll("toString", "equals", "hasNext");
    }

    @FXML
    private void handleMenuItemOpenGraph(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JMSpy invocation graph");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                invocationGraph = InvocationGraph.load(file);
                drawGraph(invocationGraph)
            } catch (e) {
                final Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Button button = new Button("Close");
                button.setOnMouseClicked({
                    dialogStage.close();
                })
                dialogStage.setScene(new Scene(VBoxBuilder.create().
                        children(new Text("Failed to open invocation graph, bad format"), button).
                        alignment(Pos.CENTER).padding(new Insets(5)).build()));
                dialogStage.show();
            }

        }

    }

    @FXML
    private void addSkipMethod(MouseEvent event) {
        skipMethodList.getItems().add(skipMethodEdit.getText());
        skipMethodEdit.clear();
    }

    @FXML
    private void clearSkipMethodList(MouseEvent event) {
        skipMethodList.getItems().clear();
    }

    @FXML
    private void refreshGraph(MouseEvent event) {
        if (invocationGraph != null) {
            drawGraph(invocationGraph)
        }
    }

    @FXML
    private void filterGraph(KeyEvent event) {
        if (invocationGraph != null) {
            filterValue = filterEdit.getText();
            //drawGraph(invocationGraph)
        }
    }


    private void drawGraph(InvocationGraph invocationGraph) {
        clearIGraphTree();
        fillRoot(invocationGraph)
        TreeItem<String> rootItem = iGraphTree.getRoot();
        rootItem.setExpanded(true);

        invocationGraph.root.outgoingEdges.each { it ->
            addItem(rootItem, it)
        }
    }

    private void addItem(TreeItem<String> rootItem, Edge edge) {
        if (!isSkipped(edge.getMethod())) {
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

    private boolean include(Edge edge) {
        if (edge.to != null && edge.to.outgoingEdges != null && edge.to.outgoingEdges.size() > 0) {
            edge.to.outgoingEdges.each {
                if (it.method.getName().startsWith(filterValue)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void fillRoot(InvocationGraph iGraph) {
        iGraphTree.getRoot().setValue(iGraph.root.type.getName());
    }

    private void clearIGraphTree() {
        iGraphTree.getRoot().getChildren().clear();
        iGraphTree.getRoot().setValue("empty")
    }

    private boolean isSkipped(JMethod jMethod) {
        skipMethodList.getItems().contains(jMethod.name);
    }

}
