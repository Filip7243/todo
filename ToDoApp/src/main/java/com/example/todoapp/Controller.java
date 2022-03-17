package com.example.todoapp;

import com.example.tododata.ToDoData;
import com.example.tododata.ToDoItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import java.time.LocalDate;

public class Controller {

    @FXML
    private ListView<ToDoItem> toDoItems;
    @FXML
    private TextField newTaskNameField;
    @FXML
    private TextArea newTaskDescriptionField;
    @FXML
    private DatePicker newTaskDeadlineDateField;
    @FXML
    private ToggleGroup buttonGroup;
    @FXML
    private ToggleButton today;
    @FXML
    private ToggleButton upcoming;
    @FXML
    public ToggleButton overdue;
    @FXML
    public ToggleButton inbox;

    private final ToDoData db = ToDoData.getInstance();
    private FilteredList<ToDoItem> filteredList;
    private final ObservableList<ToDoItem> filter = FXCollections.observableArrayList(db.getData());

    //TODO: zrobic kategorie tabele i filtrowac po ID
    public void initialize() {
        this.filteredList = new FilteredList<>(filter);
        this.toDoItems.setItems(filter);

        toDoItems.setCellFactory(CheckBoxListCell.forListView(toDoItem -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((observableValue, wasSelected, isSelected) -> {
                ToDoData.getInstance().deleteTask(toDoItem);
                filter.remove(toDoItem);
            });
            return observable;
        }));

        buttonGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (t1 == today) {
                filteredList.setPredicate(item -> item.getItemDeadlineDate().compareTo(java.sql.Date.valueOf(LocalDate.now())) == 0);
            } else if (t1 == overdue) {
                filteredList.setPredicate(item -> item.getItemDeadlineDate().compareTo(java.sql.Date.valueOf(LocalDate.now())) < 0);
            } else if (t1 == upcoming) {
                filteredList.setPredicate(item -> item.getItemDeadlineDate().compareTo(java.sql.Date.valueOf(LocalDate.now())) > 0);
            } else if (t1 == inbox) {
                filteredList.setPredicate(item -> true);
            }
            toDoItems.setItems(filteredList);
        });
    }

    public void addNewTask() {
        String newTaskName = this.newTaskNameField.getText();
        String newTaskDescription = this.newTaskDescriptionField.getText();
        LocalDate newTaskDeadlineDate = this.newTaskDeadlineDateField.getValue();
        if (!newTaskName.isEmpty() && newTaskDeadlineDate != null) {
            db.addNewTask(newTaskName, newTaskDescription, newTaskDeadlineDate);
            this.newTaskNameField.clear();
            this.newTaskDescriptionField.clear();
            this.newTaskDeadlineDateField.getEditor().clear();
        } else {
            System.out.println("empty");
        }
        filter.setAll(db.getData());
    }
}