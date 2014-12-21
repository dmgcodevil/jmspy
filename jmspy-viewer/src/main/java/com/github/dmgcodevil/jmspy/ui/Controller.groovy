package com.github.dmgcodevil.jmspy.ui

import com.github.dmgcodevil.jmspy.InvocationRecord
import com.github.dmgcodevil.jmspy.Snapshot
import com.github.dmgcodevil.jmspy.context.InvocationContext
import com.github.dmgcodevil.jmspy.graph.Edge
import com.github.dmgcodevil.jmspy.graph.InvocationGraph
import com.github.dmgcodevil.jmspy.proxy.JMethod
import com.google.common.collect.Lists
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ContextMenu
import javafx.scene.control.ContextMenuBuilder
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.control.MenuItemBuilder
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBoxBuilder
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.apache.commons.lang3.StringUtils

import java.nio.file.Paths
import java.text.SimpleDateFormat

import static com.github.dmgcodevil.jmspy.ui.UIUtils.createSimplePopup
import static com.github.dmgcodevil.jmspy.ui.UIUtils.showPopupMessage
import static org.apache.commons.lang3.StringUtils.isNotEmpty

/**
 * Created by dmgcodevil on 11/18/2014.
 */
class Controller {

    private Stage stage;
    @FXML
    private TreeView<EdgeTreeItem> iGraphTree;

    @FXML
    private TextField filterEdit;

    @FXML
    private Button applyFilterBtn;

    @FXML
    private TextField skipMethodEdit;

    @FXML
    private Button addSkipMethodBtn;

    @FXML
    private Button removeSkipMethodBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private ListView skipMethodList;

    @FXML
    private Button clearSkipMethodListBtn;

    @FXML
    private ListView<InvocationRecordListItem> invocationRecordsListView;

    @FXML
    private Label methodLbl;

    @FXML
    private Label targetClassLbl;

    @FXML
    private Label invocationInfoLbl;

    @FXML
    private Button copyStacktraceBtn;

    @FXML
    private CheckBox collapseChBox;

    private Snapshot snapshot;

    private File currDir;

    private InvocationGraph invocationGraph;

    private InvocationContext context;

    private boolean collapse;

    private static final String BUILD_VERSION = "1.1.2";

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        TreeItem<EdgeTreeItem> rootItem = new TreeItem<>(EdgeTreeItem.EMPTY);
        iGraphTree.setRoot(rootItem);
        skipMethodList.getItems().addAll("toString", "equals", "hasNext");
        addIGraphTreeContextMenu();
    }

    private boolean notEmptyNode() {
        EdgeTreeItem.EMPTY != iGraphTree.getSelectionModel().getSelectedItem().getValue()
    }

    private void addIGraphTreeContextMenu() {
        def handlerWrapper = {
            EventHandler handler ->

                [handle: { event ->
                    if (notEmptyNode()) {
                        handler.handle event
                    } else {
                        showPopupMessage(createSimplePopup("This is empty node. At first, please open a snapshot",
                                UIUtils.POPUP_WARN), stage)
                    }

                }] as EventHandler
        }

        def copyToClipboard = handlerWrapper { event ->
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(iGraphTree.getSelectionModel().getSelectedItem().getValue().label);
            clipboard.setContent(content);
        }

        def copyFullToClipboard = handlerWrapper { event ->
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(nodeToString(iGraphTree.getSelectionModel().getSelectedItem(), new StringBuilder(), 0));
            clipboard.setContent(content);
        }

        def excludeMethod = handlerWrapper { event ->
            String methodName = iGraphTree.getSelectionModel().getSelectedItem().getValue()
            methodName = methodName.substring(0, methodName.indexOf("("));
            skipMethodList.getItems().add(methodName)
            refreshGraph()
        }

        def showInfo = handlerWrapper { event ->
            def contextInfo = iGraphTree.getSelectionModel().getSelectedItem().getValue().edge.contextInfo
            if (contextInfo != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/invocation_context.fxml"));
                    Parent root = (Parent) loader.load();
                    InvocationContextInfoController controller = loader.getController();
                    controller.init(contextInfo)
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (t) {
                    t.printStackTrace()
                }
            } else {
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initStyle(StageStyle.UTILITY);
                Text text = new Text("There is no information for this method");
                Button button = new Button("OK");
                button.setOnMouseClicked({
                    dialogStage.close();
                })
                dialogStage.setScene(new Scene(VBoxBuilder.create().
                        children(text, button).
                        alignment(Pos.CENTER).padding(new Insets(5)).build()));
                dialogStage.show();
            }
        }

        ContextMenu rootContextMenu = ContextMenuBuilder.create().items(
                MenuItemBuilder.create().text("Copy to clipboard").onAction(copyToClipboard).build(),
                MenuItemBuilder.create().text("Copy whole node to clipboard").onAction(copyFullToClipboard).build(),
                MenuItemBuilder.create().text("Exclude/skip").onAction(excludeMethod).build(),
                MenuItemBuilder.create().text("Show info").onAction(showInfo).build())
                .build();

        iGraphTree.setContextMenu(rootContextMenu);
    }

    @FXML
    private void handleMenuItemOpenGraph(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open JMSpy invocation graph");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Jmspy snapshot (*.jmspy)", "*.jmspy");
        fileChooser.getExtensionFilters().add(extFilter);
        if (currDir != null) {
            fileChooser.setInitialDirectory(Paths.get(currDir.getParent()).toFile());
        }

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            currDir = file;
            try {
                snapshot = Snapshot.load(file);
                loadRecords();
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
    private void showAboutInfo(ActionEvent event) {
        showPopupMessage("The viewer to analyze JMSPY snapshots.\n\n" +
                "--------------------------------------------\n" +
                "   build version '$BUILD_VERSION'", stage)
    }

    @FXML
    private void drawInvocationGraph(MouseEvent event) {
        if (invocationRecordsListView.getItems().isEmpty()) {
            return
        }
        InvocationRecordListItem item = invocationRecordsListView.getSelectionModel().getSelectedItem();
        if (item != null) {
            InvocationRecord invocationRecord = getInvocationRecordById(item.id);
            invocationGraph = invocationRecord.invocationGraph
            context = invocationRecord.invocationContext;
            fillInvocationContext()
            drawGraph(invocationGraph)

        }
    }

    private void fillInvocationContext() {
        methodLbl.setText(context?.root?.toString() ?: "unknown");
        targetClassLbl.setText(context?.root?.targetClass ?: "unknown")
        invocationInfoLbl.setText(context?.rootContextInfo?.info ?: "unknown")
    }

    private InvocationRecord getInvocationRecordById(def id) {
        InvocationRecord record = null
        if (snapshot != null) {
            record = snapshot.invocationRecords.find {
                it.id.equals(id)
            }
        }
        return record
    }

    private void loadRecords() {
        def label = {
            InvocationRecord record ->
                String lbl = "method: '" + (record?.invocationContext?.root?.toString() ?: "unknown") + "'";
                if (!lbl.isEmpty()) {
                    lbl += " - "
                }
                def created = record?.created;
                if (created != null) {
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                    lbl += dt.format(created);
                } else {
                    lbl += "unknown date";
                }

                return lbl
        }
        if (snapshot != null) {
            invocationRecordsListView.getItems().clear();
            snapshot.invocationRecords.each { it ->
                InvocationRecordListItem item = new InvocationRecordListItem(label(it), it.id);
                invocationRecordsListView.getItems().add(item)
            }
        }
    }

    @FXML
    private void collapse(ActionEvent event) {
        collapse = collapseChBox.selected
        refreshGraph();
    }

    @FXML
    private void addSkipMethod(MouseEvent event) {
        def methodName = skipMethodEdit.getText();
        if (isNotEmpty(methodName)) {
            skipMethodList.getItems().add(methodName);
            skipMethodEdit.clear();
        }
    }

    @FXML
    private void removeSkipMethod(MouseEvent event) {
        def si = skipMethodList.getSelectionModel().getSelectedItem();
        skipMethodList.getItems().remove(si);
    }

    @FXML
    private void copyStacktraceToClipboard(MouseEvent event) {
        if (context != null) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(StringUtils.join(context.stackTrace, "\n"));
            clipboard.setContent(content);
            showPopupMessage("Successfully copied to clipboard", stage)
        }
    }

    @FXML
    private void clearSkipMethodList(MouseEvent event) {
        skipMethodList.getItems().clear();
    }

    @FXML
    private void refreshGraph(MouseEvent event) {
        refreshGraph()
    }

    private void refreshGraph() {
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

    private String nodeToString(TreeItem<EdgeTreeItem> item, StringBuilder builder, int step) {
        builder.append("| " * step)
        if (item.getChildren().size() > 0) {
            builder.append("+- ")
        } else {
            builder.append("\\- ")
        }
        builder.append(item.getValue())
        if (item.getChildren().size() > 0) {
            step += 3
            for (def child : item.getChildren()) {
                builder.append("\n");
                nodeToString(child, builder, step)
            }
            return builder.toString();
        }
        return builder.toString();
    }

    private void drawGraph(InvocationGraph invocationGraph) {
        drawGraph(invocationGraph, Filter.EMPTY);
    }

    private void drawGraph(InvocationGraph invocationGraph, Filter filter) {
        clearIGraphTree();
        fillRoot(invocationGraph)
        TreeItem<EdgeTreeItem> rootItem = iGraphTree.getRoot();
        rootItem.setExpanded(true);

        invocationGraph.root.outgoingEdges.each { it ->
            addItem(rootItem, it, 0, filter)
        }
    }

    private void addItem(TreeItem<EdgeTreeItem> rootItem, Edge edge, int level, Filter filter) {
        if (!isSkipped(edge.getMethod()) && filter.apply(edge.getMethod().name, level)) {
            TreeItem<EdgeTreeItem> child = findTreeItem(rootItem, edge)
            if (collapse) {
                if (child == null) {
                    child = new TreeItem<>(new EdgeTreeItem(edge));
                    rootItem.getChildren().add(child);
                }
                child.value.incCollapse();
            } else {
                child = new TreeItem<>(new EdgeTreeItem(edge));
                rootItem.getChildren().add(child);
            }

            if (edge.to != null) {
                child.setExpanded(false);
                edge.to.outgoingEdges.each {
                    addItem(child, it, level + 1, filter)
                }
            }
        }
    }

    private TreeItem<EdgeTreeItem> findTreeItem(TreeItem<EdgeTreeItem> rootItem, Edge edge) {
        rootItem.children.find {
            if (it.value.edge != null && it.value.edge.method != null && it.value.edge.method.name.equals(edge.method.name)) {
                return it
            }
        }
    }

    private void fillRoot(InvocationGraph iGraph) {
        iGraphTree.getRoot().setValue(new EdgeTreeItem(iGraph.root.type.getName()));
    }

    private void clearIGraphTree() {
        iGraphTree.getRoot().getChildren().clear();
        iGraphTree.getRoot().setValue(new EdgeTreeItem())
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

    private class InvocationRecordListItem {
        String label;
        def id;

        InvocationRecordListItem(String label, def id) {
            this.label = label
            this.id = id
        }

        @Override
        public String toString() {
            return label
        }
    }

    private static class EdgeTreeItem {
        String label = "empty";
        Edge edge;
        final static EdgeTreeItem EMPTY = new EdgeTreeItem("empty");
        boolean collapse;
        int countCollapsed;

        EdgeTreeItem() {
        }

        EdgeTreeItem(String label) {
            this.label = label
        }

        EdgeTreeItem(Edge edge) {
            this.edge = edge
            label = edge.method.toString();
        }

        void incCollapse() {
            countCollapsed++;
        }

        @Override
        public String toString() {
            String suffix = ''
            if (countCollapsed > 0) {
                suffix = "($countCollapsed)"
            }
            return label + suffix;
        }
    }

}
