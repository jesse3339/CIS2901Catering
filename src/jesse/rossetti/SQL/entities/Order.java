package jesse.rossetti.SQL.entities;

import javafx.beans.property.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jesse.rossetti.CateringApp;
import jesse.rossetti.SQL.interfaces.IEntity;
import jesse.rossetti.SQL.util.TableUtil;
import jesse.rossetti.enums.OrderSize;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;


public class Order implements IEntity<Order> {

    public SimpleIntegerProperty orderID;
    public SimpleIntegerProperty customerID;
    public ObjectProperty<LocalDate> sDate;
    public ObjectProperty<LocalDate> dDate;
    public SimpleFloatProperty cost;
    public SimpleIntegerProperty status;
    public SimpleIntegerProperty orderSize;
    public SimpleIntegerProperty storeID;
    public SimpleStringProperty orderNotes;

    public SimpleStringProperty statusString;
    public SimpleStringProperty orderSizeString;


    public Order() {
        orderID = new SimpleIntegerProperty();
        customerID = new SimpleIntegerProperty();
        sDate = new SimpleObjectProperty<LocalDate>(LocalDate.now()) {};
        dDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 1, 1));
        cost = new SimpleFloatProperty();
        status = new SimpleIntegerProperty();
        orderSize = new SimpleIntegerProperty();
        storeID = new SimpleIntegerProperty();
        storeID = new SimpleIntegerProperty();
        orderNotes = new SimpleStringProperty();
        orderSizeString = new SimpleStringProperty();
        statusString = new SimpleStringProperty();
        setStatusString(0);
        setOrderSizeString(0);
    }

    public Order(int customerId, LocalDate delDate, float nCost, int nStatus, OrderSize nOrderSize, int storeId, String nOrderNotes){
        orderID = new SimpleIntegerProperty();
        customerID = new SimpleIntegerProperty(customerId);
        sDate = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        dDate = new SimpleObjectProperty<LocalDate>(delDate);
        cost = new SimpleFloatProperty(nCost);
        status = new SimpleIntegerProperty(nStatus);
        orderSize = new SimpleIntegerProperty(nOrderSize.ordinal());
        storeID = new SimpleIntegerProperty(storeId);
        orderNotes= new SimpleStringProperty(nOrderNotes);
        orderSizeString = new SimpleStringProperty();
        statusString = new SimpleStringProperty();
        setStatusString(nStatus);
        setOrderSizeString(nOrderSize.ordinal());

    }

    //region Getters and Setters


    public String getStatusString() {
        return statusString.get();
    }

    public SimpleStringProperty statusStringProperty() {
        return statusString;
    }

    public void setStatusString(int status) {
        String s;
        if(status == 0){
            s = "Incomplete";
        } else {
            s = "Complete";
        }
        this.statusString.set(s);
    }

    public String getOrderSizeString() {
        return orderSizeString.get();
    }

    public SimpleStringProperty orderSizeStringProperty() {
        return orderSizeString;
    }

    public void setOrderSizeString(int size) {
        String s = "";
        switch (size){
            case 0:
                s = "Small";
                break;
            case 1:
                s = "Medium";
                break;
            case 2:
                s = "Large";
                break;
        }
        this.orderSizeString.set(s);
    }

    public ObjectProperty<LocalDate> sDateProperty() {
        return sDate;
    }

    public ObjectProperty<LocalDate> dDateProperty() {
        return dDate;
    }

    public int getOrderID() {
        return orderID.get();
    }

    public SimpleIntegerProperty orderIDProperty() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID.set(orderID);
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public LocalDate getsDate() {
        return sDate.get();
    }

    public void setsDate(LocalDate sDate) {
        this.sDate = new SimpleObjectProperty<LocalDate>(sDate);
    }

    public LocalDate getdDate() {
        return dDate.get();
    }

    public void setdDate(LocalDate dDate) {
        this.dDate = new SimpleObjectProperty<LocalDate>(dDate);
    }

    public float getCost() {
        return cost.get();
    }

    public SimpleFloatProperty costProperty() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost.set(cost);
    }

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public int getOrderSize() {
        return orderSize.get();
    }

    public SimpleIntegerProperty orderSizeProperty() {
        return orderSize;
    }

    public void setOrderSize(int orderSize) {
        this.orderSize.set(orderSize);
    }

    public int getStoreID() {
        return storeID.get();
    }

    public SimpleIntegerProperty storeIDProperty() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID.set(storeID);
    }

    public String getOrderNotes() {
        return orderNotes.get();
    }

    public SimpleStringProperty orderNotesProperty() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes.set(orderNotes);
    }
    //endregion


    @Override
    public void addToTable(Connection conn) {
        try{
            Statement stmt = conn.createStatement();

            String SQL = "INSERT INTO JR_ORDERS VALUES (JR_ORDER_SEQ.nextval, '"
                    + this.getCustomerID() + "', TO_DATE('"
                    + this.getsDate() + "', 'yyyy-mm-dd'), TO_DATE('"
                    + this.getdDate() + "', 'yyyy-mm-dd'), '"
                    + this.getCost() + "', '"
                    + this.getStatus() + "', '"
                    + this.getOrderSize() + "', '"
                    + this.getStoreID() + "', '"
                    + this.getOrderNotes()
                    + "')";
            stmt.execute(SQL);
            //refresh boxes


            //this determines date of delivery and picks random employe
            Calendar c = Calendar.getInstance();
            c.setTime(Date.from(this.getdDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            String availString = "";
            switch (dayOfWeek){
                case 1:
                    availString = "WHERE AVAILABILITY LIKE '1_______' ";
                    break;
                case 2:
                    availString = "WHERE AVAILABILITY LIKE '%1______' ";
                    break;
                case 3:
                    availString = "WHERE AVAILABILITY LIKE '%1_____' ";
                    break;
                case 4:
                    availString = "WHERE AVAILABILITY LIKE '%1____' ";
                    break;
                case 5:
                    availString = "WHERE AVAILABILITY LIKE '%1___' ";
                    break;
                case 6:
                    availString = "WHERE AVAILABILITY LIKE '%1__' ";
                    break;
                case 7:
                    availString = "WHERE AVAILABILITY LIKE '%1_' ";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + dayOfWeek);
            }

            for (int i = 0; i < 3; i++){
                String capString = "";
                String role = "";
                switch (i){
                    case 0:
                        capString = "AND CAPABILITIES LIKE '1___' ";
                        role = "Food Prep";
                        break;
                    case 1:
                        capString = "AND CAPABILITIES LIKE '%1__' ";
                        role = "Delivery";
                        break;
                    case 2:
                        capString = "AND CAPABILITIES LIKE '%1_' ";
                        role = "Serving";
                        break;
                }

                String selectEmployees = "SELECT * FROM (SELECT EMPLOYEEID FROM JR_EMPLOYEES " + availString + capString + "ORDER BY DBMS_RANDOM.RANDOM)" +
                        "WHERE ROWNUM = 1 " ;
                ResultSet rs = stmt.executeQuery(selectEmployees);
                while (rs.next()){
                    Statement innerStmt = conn.createStatement();
                    String createOA = "INSERT INTO JR_EMPLOYEEASSIGNMENT VALUES " +
                            "(JR_ORDER_SEQ.currval, " + rs.getInt("EMPLOYEEID") + ", '" + role + "')";
                    innerStmt.executeQuery(createOA);
                    innerStmt.close();
                }
            }


            refreshContainers(stmt);
            clearEntry();
            stmt.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }


    public static void removeFromTable(Connection conn, int UniqueID) {
        try{
            Statement stmt = conn.createStatement();
            String SQL = "DELETE FROM JR_ORDERS WHERE ORDERID =" + UniqueID;
            //ResultSet rs = stmt.executeQuery("SELECT lastname FROM jr_customers WHERE customerid=" + UniqueID);
            //System.out.println("here");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Order");
            alert.setContentText("Are you sure you want to delete order id " + UniqueID);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                stmt.execute(SQL);
            }
            refreshContainers(stmt);
            stmt.close();

        }catch (Exception e){System.out.println(e);}
    }


    public static void editEntry(Connection conn, int UniqueID) {
        try {
            Statement statement = conn.createStatement();
            String SQL = "UPDATE JR_ORDERS SET CUSTOMERID = '" + CateringApp.controller.CustomerComboBox.getValue().id
                    + "', STOREID = '" + CateringApp.controller.oStoreComboBox.getValue().id
                    + "', DELIVERYDATE = TO_DATE('" + CateringApp.controller.OrderDeliveryDate.getValue() + "','yyyy-mm-dd')"
                    + ", COST = " + Float.parseFloat(CateringApp.controller.OrderCost.getText())
                    + ", ORDERSIZE = '" + CateringApp.controller.orderSize.getValue().ordinal()
                    + "', ORDERNOTES = '" + CateringApp.controller.OrderNotes.getText()
                    + "' WHERE ORDERID = " + UniqueID;

            statement.executeQuery(SQL);
            refreshContainers(statement);
            clearEntry();
            statement.close();


        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Order");
            alert.setContentText("Order delete failed, try re-entering the date. " + UniqueID);
            alert.showAndWait();
            //throwables.printStackTrace();
        }
    }


    public static void complete(Connection conn, int UniqueID){
        try {
            Statement statement = conn.createStatement();
            String SQL = "UPDATE JR_ORDERS SET STATUS = 1 WHERE ORDERID = " + UniqueID;

            statement.executeQuery(SQL);
            TableUtil.queryOrderTable(statement);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void refreshContainers(Statement stmt) {
        clearEntry();
        TableUtil.queryOrderTable(stmt);
        TableUtil.refreshOrderCombobox(stmt);
    }


    public static void clearEntry(){
        CateringApp.controller.OrderDeliveryDate.getEditor().setText("");
        CateringApp.controller.OrderCost.setText("");
        CateringApp.controller.eStoreSelector.getSelectionModel().clearSelection();
        CateringApp.controller.CustomerComboBox.getSelectionModel().clearSelection();
        CateringApp.controller.orderSize.getSelectionModel().clearSelection();
        CateringApp.controller.OrderNotes.setText("");
    }
}
