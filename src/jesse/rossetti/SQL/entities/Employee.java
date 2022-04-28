package jesse.rossetti.SQL.entities;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jesse.rossetti.CateringApp;
import jesse.rossetti.SQL.interfaces.IEntity;
import jesse.rossetti.SQL.util.TableUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


// This class represents employees
// Some things could be simplified, a method used was converting the checkboxes into a bool array,
// and then storing it as a binary string. This allowed for easy use of comparative statements.
public class Employee implements IEntity<Employee> {

    public SimpleIntegerProperty employeeID;
    public SimpleStringProperty lastName;
    public SimpleStringProperty firstName;
    public SimpleStringProperty name;
    public SimpleStringProperty phoneNum;
    public SimpleIntegerProperty capability;
    public SimpleIntegerProperty availability;
    public SimpleIntegerProperty storeID;
    public SimpleStringProperty availabilityString;
    public SimpleStringProperty capabilityString;

    public Employee(){
        availabilityString = new SimpleStringProperty();
        capabilityString = new SimpleStringProperty();
        name = new SimpleStringProperty();
        employeeID = new SimpleIntegerProperty();
        lastName = new SimpleStringProperty();
        firstName = new SimpleStringProperty();
        phoneNum = new SimpleStringProperty();
        capability = new SimpleIntegerProperty();
        availability = new SimpleIntegerProperty();
        storeID = new SimpleIntegerProperty();
    }

    public Employee(String lastname, String firstname, String phonenum, int availabilities, int capabilities, int storeid) {
        name = new SimpleStringProperty(lastname + ", " + firstname);
        employeeID = new SimpleIntegerProperty();
        lastName = new SimpleStringProperty(lastname);
        firstName = new SimpleStringProperty(firstname);
        phoneNum = new SimpleStringProperty(phonenum);
        capability = new SimpleIntegerProperty(capabilities);
        availability = new SimpleIntegerProperty(availabilities);
        storeID = new SimpleIntegerProperty(storeid);
        availabilityString = new SimpleStringProperty(createAvailabilityStr());
        capabilityString = new SimpleStringProperty(createCapabilityStr());
    }

    //region getters and setters
    public int getStoreID() {
        return storeID.get();
    }

    public SimpleIntegerProperty storeIDProperty() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID.set(storeID);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public SimpleIntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getPhoneNum() {
        return phoneNum.get();
    }

    public SimpleStringProperty phoneNumProperty() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum.set(phoneNum);
    }

    public String getAvailabilityString() {
        return availabilityString.get();
    }

    public SimpleStringProperty availabilityStringProperty() {
        return availabilityString;
    }

    public void setAvailabilityString(String availabilityString) {
        this.availabilityString.set(availabilityString);
    }

    public int getCapability() {
        return capability.get();
    }

    public SimpleIntegerProperty capabilityProperty() {
        return capability;
    }

    public void setCapability(int capability) {
        this.capability.set(capability);
    }

    public int getAvailability() {
        return availability.get();
    }

    public SimpleIntegerProperty availabilityProperty() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability.set(availability);
    }

    //endregion

    public String getCapabilityString() {
        return capabilityString.get();
    }

    public SimpleStringProperty capabilityStringProperty() {
        return capabilityString;
    }


    public void setCapabilityString(String capabilityString) {
        this.capabilityString.set(capabilityString);
    }


    @Override
    public void addToTable(Connection conn) {

        try{
            Statement stmt = conn.createStatement();

            String SQL = "INSERT INTO JR_EMPLOYEES VALUES (JR_EMPLOYEE_SEQ.nextval, '"
                    + this.getLastName() + "', '"
                    + this.getFirstName() + "', '"
                    + this.getPhoneNum() + "', '"
                    + this.getAvailability() + "', '"
                    + this.getCapability() + "', '"
                    + this.getStoreID() + "')";

            stmt.executeQuery(SQL);
            refreshContainers(stmt);
            clearEntry();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void editEntry(Connection conn, int UniqueID) {
        try {
            Statement statement = conn.createStatement();
            String SQL = "UPDATE JR_EMPLOYEES SET LASTNAME = '" + this.getLastName()
                    + "', FIRSTNAME = '" + this.getFirstName()
                    + "', AVAILABILITY = '" + this.getAvailability()
                    + "', CAPABILITIES = '" + this.getCapability()
                    + "', STOREID = '" + this.getStoreID()
                    + "', PHONENUMBER = '" + this.getPhoneNum()
                    + "' WHERE EMPLOYEEID = " + UniqueID;
            statement.executeQuery(SQL);
            refreshContainers(statement);
            clearEntry();
            statement.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void removeFromTable(Connection conn, int uniqueID){
        try{
            Statement stmt = conn.createStatement();
            String SQL = "DELETE FROM JR_EMPLOYEES WHERE EMPLOYEEID =" + uniqueID;
            ResultSet rs = stmt.executeQuery("SELECT LASTNAME, FIRSTNAME FROM JR_EMPLOYEES WHERE EMPLOYEEID=" + uniqueID);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Employee");
            alert.setContentText("Are you sure you want to delete employee id " + uniqueID);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                stmt.execute(SQL);
            }
            refreshContainers(stmt);
            clearEntry();
            stmt.close();

        }catch (Exception e){System.out.println(e);}

    }


    public static void refreshContainers(Statement stmt){
        TableUtil.queryEmployeeTable(stmt);
        TableUtil.refreshEmployeeCombobox(stmt);
    }


    public static void clearEntry(){
        CateringApp.controller.eStoreSelector.getEditor().clear();
        CateringApp.controller.EmployeeLastName.setText("");
        CateringApp.controller.EmployeeFirstName.setText("");
        CateringApp.controller.EmployeePhoneNumber.setText("");
        CateringApp.controller.eCheckSunday.setSelected(false);
        CateringApp.controller.eCheckMonday.setSelected(false);
        CateringApp.controller.eCheckTuesday.setSelected(false);
        CateringApp.controller.eCheckWednesday.setSelected(false);
        CateringApp.controller.eCheckThursday.setSelected(false);
        CateringApp.controller.eCheckFriday.setSelected(false);
        CateringApp.controller.eCheckSaturday.setSelected(false);
        CateringApp.controller.eCheckFoodPrep.setSelected(false);
        CateringApp.controller.eCheckDelivery.setSelected(false);
        CateringApp.controller.eCheckServing.setSelected(false);
    }


    public String createAvailabilityStr(){
        String rs = "";
        Boolean[] bAvailArr = convertIntToBoolArray(availability.getValue().toString(), 8);
        if (bAvailArr[0]){
            rs += "Sun, ";
        }
        if (bAvailArr[1]){
            rs += "Mon, ";
        }
        if (bAvailArr[2]){
            rs += "Tues, ";
        }
        if (bAvailArr[3]){
            rs += "Wed, ";
        }
        if (bAvailArr[4]){
            rs += "Thur, ";
        }
        if (bAvailArr[5]){
            rs += "Fri, ";
        }
        if (bAvailArr[6]){
            rs += "Sat, ";
        }
        if (rs.length() > 2){
            rs = rs.substring(0, rs.length() - 2);
        }
        return rs;
    }


    public String createCapabilityStr(){
        String rs = "";
        Boolean[] bAvailArr = convertIntToBoolArray(capability.getValue().toString(), 4);
        if (bAvailArr[0]){
            rs += "D";
        }
        if (bAvailArr[1]){
            rs += "Fp";
        }
        if (bAvailArr[2]){
            rs += "S";
        }

        return rs;
    }


    //used to store days available and capabilities
    public static String convertBoolArrayToInt(Boolean[] source, int size){
        int result = 0;
        int index = size - source.length;

        for (Boolean b : source) {
            if(b){
                result |= (1 << (size-index));
            }
            index++;
        }

        String rs = Integer.toBinaryString(result);
        if(rs.length() < size){
            while (rs.length() < size){
                rs = "0" + rs;
            }
        }
        return rs;
    }


    //used to read days available and capabilities from int
    public static Boolean[] convertIntToBoolArray(String source1, int size){
        Boolean[] result = new Boolean[size];
        int source = Integer.parseInt(source1, 2);
        for (int i = 0; i < size; i++){
            result[i] = (source & (1 << i)) != 0;
        }
        List<Boolean> tempList = new ArrayList<>(Arrays.asList(result));
        Collections.reverse(tempList);
        result = tempList.toArray(new Boolean[0]);
        return result;
    }
}
