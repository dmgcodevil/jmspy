package com.github.dmgcodevil.jmspy.ui

import com.github.dmgcodevil.jmspy.graph.Edge
import com.github.dmgcodevil.jmspy.graph.InvocationGraph
import com.github.dmgcodevil.jmspy.proxy.JMethod
import com.google.common.collect.Lists
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
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
                        children(new Text("Failed to open invocation graph. " + e.getMessage()), button).
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
            def filterValue = filterEdit.getText();
            Filter filter;
            if (StringUtils.isNotBlank(filterValue)) {
                filter = Filter.parse(filterValue);
            } else {
                filter = Filter.EMPTY;
            }
            drawGraph(invocationGraph, filter)
        }
    }


    private void drawGraph(InvocationGraph invocationGraph) {
        drawGraph(invocationGraph, Filter.EMPTY);
    }

    private void drawGraph(InvocationGraph invocationGraph, Filter filter) {
        clearIGraphTree();
        fillRoot(invocationGraph)
        TreeItem<String> rootItem = iGraphTree.getRoot();
        rootItem.setExpanded(true);

        invocationGraph.root.outgoingEdges.each { it ->
            addItem(rootItem, it, 0, filter)
        }
    }

    private void addItem(TreeItem<String> rootItem, Edge edge, int level, Filter filter) {
        if (!isSkipped(edge.getMethod()) && filter.apply(edge.getMethod().name, level)) {
            TreeItem<String> child = new TreeItem<String>(edge.getMethod().toString());
            rootItem.getChildren().add(child);
            if (edge.to != null) {
                child.setExpanded(false);
                edge.to.outgoingEdges.each {
                    addItem(child, it, level + 1, filter)
                }
            }
        }
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

    private static class Filter {
        List<String> elements;
        public static Filter EMPTY = new Filter(Collections.emptyList());

        Filter(List<String> elements) {
            this.elements = elements
        }

        static Filter parse(String filter) {
            return new Filter(Lists.newArrayList(StringUtils.split(filter, ".")));
        }

        boolean apply(String name, def index) {

            if (!EMPTY.equals(this) && elements.size() > index) {
                def filter = get(index);
                return "*".equals(filter) || name.startsWith(filter)
            }
            return true;
        }

        String get(int index) {
            return elements.get(index);
        }

        boolean isEmpty() {
            return elements.isEmpty();
        }
    }

}
