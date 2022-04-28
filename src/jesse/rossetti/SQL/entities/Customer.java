package jesse.rossetti.SQL.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jesse.rossetti.CateringApp;
import jesse.rossetti.SQL.util.TableUtil;
import jesse.rossetti.SQL.interfaces.IEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Customer implements IEntity<Customer> {

    public SimpleIntegerProperty customerID;
    public SimpleStringProperty lastName;
    public SimpleStringProperty firstName;
    public SimpleStringProperty address;
    public SimpleStringProperty city;
    public SimpleStringProperty state;
    public SimpleStringProperty phoneNum;

    public Customer(){
        this.customerID = new SimpleIntegerProperty();
        this.lastName = new SimpleStringProperty();
        this.firstName = new SimpleStringProperty();
        this.address = new SimpleStringProperty();
        this.city = new SimpleStringProperty();
        this.state = new SimpleStringProperty();
        this.phoneNum = new SimpleStringProperty();
    }

    public Customer(String lastName, String firstName, String address, String city, String state, String phoneNum) {
        this.customerID = new SimpleIntegerProperty();
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.phoneNum = new SimpleStringProperty(phoneNum);
    }

    //region Getters and setters
    public int getCustomerID() {
        return customerID.get();
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }
    //endregion

    @Override
    public String toString(){
        return customerID + " " + lastName;
    }


    @Override
    public void addToTable(Connection conn) {
        try{
            Statement stmt = conn.createStatement();

            String SQL = "INSERT INTO jr_customers VALUES (jr_seq_customer.nextval, '"
                    + this.getLastName() + "', '"
                    + this.getFirstName() + "', '"
                    + this.getAddress() + "', '"
                    + this.getCity() + "', '"
                    + this.getState() + "', '"
                    + this.getPhoneNum() + "')";
            System.out.println(SQL);
            stmt.execute(SQL);
            System.out.println("customer added");
            //refresh boxes
            refreshContainers(stmt);

            clearTextFields();
            stmt.close();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    //@Override
    public static void removeFromTable(Connection conn, int UniqueID) {
        try{
            Statement stmt = conn.createStatement();
            String SQL = "DELETE FROM JR_Customers WHERE customerid =" + UniqueID;
            ResultSet rs = stmt.executeQuery("SELECT lastname FROM jr_customers WHERE customerid=" + UniqueID);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setContentText("Are you sure you want to delete customer id " + UniqueID + "\n This should be done only after you have checked to see if the customer has any active orders.");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                stmt.execute(SQL);

            }
            refreshContainers(stmt);
            clearTextFields();
            stmt.close();

        }catch (Exception e){System.out.println(e);}

    }


    public static void editEntry(Connection conn, int UniqueID) {
        try {
            Statement statement = conn.createStatement();
            String SQL = "UPDATE JR_CUSTOMERS SET LASTNAME = '" + CateringApp.controller.CustomerLastName.getText()
                                        + "', FIRSTNAME = '" + CateringApp.controller.CustomerFirstName.getText()
                                        + "', ADDRESS = '" + CateringApp.controller.CustomerAddress.getText()
                                        + "', CITY = '" + CateringApp.controller.CustomerCity.getText()
                                        + "', STATE = '" + CateringApp.controller.CustomerState.getText()
                                        + "', PHONENUMBER = '" + CateringApp.controller.CustomerPhoneNumber.getText()
                                        + "' WHERE CUSTOMERID = " + UniqueID;

            statement.executeQuery(SQL);
            refreshContainers(statement);
            clearTextFields();
            statement.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void refreshContainers(Statement stmt) {
        TableUtil.refreshCustomerCombobox(stmt);
        TableUtil.queryCustomerTable(stmt);
        TableUtil.queryOrderTable(stmt);
    }

    public static void clearTextFields(){
        CateringApp.controller.CustomerLastName.setText("");
        CateringApp.controller.CustomerFirstName.setText("");
        CateringApp.controller.CustomerAddress.setText("");
        CateringApp.controller.CustomerCity.setText("");
        CateringApp.controller.CustomerState.setText("");
        CateringApp.controller.CustomerPhoneNumber.setText("");
    }


}
