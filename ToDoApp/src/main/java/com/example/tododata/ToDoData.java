package com.example.tododata;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ToDoData {

    private final String url = "jdbc:mysql://localhost:3306/tododata";
    private final String login = "root";
    private final String password = "";
    private Connection connection;
    private Statement statement;
    private ObservableList<ToDoItem> data;


    private ToDoData() {
        this.data = FXCollections.observableArrayList();
        connectDB();
    }


    public ObservableList<ToDoItem> getData() {
        return data;
    }

    public static ToDoData getInstance() {
        return new ToDoData();
    }

    private void connectDB() {
        try {
            this.connection = DriverManager.getConnection(url, login, password);
            this.statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadFromDB();
    }

    public void addNewTask(String iName, String iDesc, LocalDate iDeadline) {
        Date deadline = localDateToDate(iDeadline);
        String update = "INSERT INTO todoitem (itemName, itemDescription, itemDeadline)" +
                " VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = connection.prepareStatement(update);
            pst.setString(1, iName);
            pst.setString(2, iDesc);
            pst.setDate(3, java.sql.Date.valueOf(iDeadline));
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long lastItemID = getItemID();
        this.data.add(new ToDoItem(lastItemID, iName, iDesc, deadline));
    }

    public void deleteTask(ToDoItem item) {
        String delete = "DELETE from todoitem WHERE itemID = ?";
        try {
            PreparedStatement pst = connection.prepareStatement(delete);
            pst.setLong(1, item.getItemID());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromDB() {
        try {
            ResultSet query = statement.executeQuery("SELECT * from todoitem");

            while (query.next()) {
                data.addAll(new ToDoItem(query.getInt("itemID"), query.getString("itemName"), query.getString("itemDescription"), query.getDate("itemDeadline")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Date localDateToDate(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(zoneId).toInstant());
    }

    private long getItemID() {
        long id = 0;
        String query = "SELECT itemID FROM todoitem ORDER BY itemID DESC LIMIT 1";
        try {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                id = rs.getInt("itemID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
