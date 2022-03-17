package com.example.tododata;

import java.util.Date;

public class ToDoItem {

    private final long itemID;
    private String itemName;
    private String itemDescription; //optional
    private Date itemDeadlineDate;

    public ToDoItem(long itemID, String itemName, String itemDescription, Date itemDeadlineDate) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemDeadlineDate = itemDeadlineDate;
    }

    public long getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Date getItemDeadlineDate() {
        return itemDeadlineDate;
    }

    public void setItemDeadlineDate(Date itemDeadlineDate) {
        this.itemDeadlineDate = itemDeadlineDate;
    }

    @Override
    public String toString() {
        return this.itemName;
    }
}
