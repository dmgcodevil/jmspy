package com.github.dmgcodevil.jmspy.ui

import com.github.dmgcodevil.jmspy.context.InvocationContextInfo
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.PropertyValueFactory

/**
 * Created by dmgcodevil on 12/7/2014.
 */
class InvocationContextInfoController {

    @FXML
    TextArea invocationContextInfo;

    @FXML
    TableView<TableItem> detailsTableView;

    public void init(InvocationContextInfo contextInfo) {
        invocationContextInfo.setText(contextInfo.info);
        javafx.collections.ObservableList<TableItem> data =
                FXCollections.observableArrayList();

        detailsTableView.setEditable(true);

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(60);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("name"));

        TableColumn valueCol = new TableColumn("Value");
        valueCol.setMinWidth(100);
        valueCol.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("val"));


        contextInfo?.details.each {
            data.add(new TableItem(it.key, it.value));
        }
        detailsTableView.setItems(data);
        detailsTableView.getColumns().addAll(nameCol, valueCol);
    }

    public static class TableItem {
        private SimpleStringProperty name;
        private SimpleStringProperty val;

        public TableItem(String name, String val) {
            this.name = new SimpleStringProperty(name)
            this.val = new SimpleStringProperty(val)
        }

        public String getName() {
            return name.get()
        }

        public void setName(String name) {
            this.name.set(name)
        }

        public String getVal() {
            return val.get()
        }

        public void setVal(String val) {
            this.val.set(val)
        }
    }

}
