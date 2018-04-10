package logica;

import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by moise on 12/5/2017.
 */

public class Order implements Serializable{

    private int tableId;
    private ArrayList<String> items;
    private String userIp;

    public Order(){}

    public Order(int tableId, ArrayList<String> items) {
        this.tableId = tableId;
        this.items = items;
        userIp = "";
    }

    public Order(int tableId, ArrayList<String> items, String userIp) {
        this.tableId = tableId;
        this.items = items;
        this.userIp = userIp;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }
}
