package jesse.rossetti.SQL.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jesse.rossetti.CateringApp;
import jesse.rossetti.SQL.entities.Employee;
import jesse.rossetti.SQL.entities.EmployeeAssignment;
import jesse.rossetti.containers.ComboBoxContainer;
import jesse.rossetti.SQL.entities.Customer;
import jesse.rossetti.SQL.entities.Order;
import jesse.rossetti.enums.OrderSize;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


// This class serves as the primary handler for querying the main tables. It works closely with the EditUtil class in order to
// reflect changes made while using the program.
public class TableUtil {

    public static ObservableList<Customer> customerData;
    public static ObservableList<Order> orderData;
    public static ObservableList<Employee> employeeData;
    public static ObservableList<EmployeeAssignment> detailedData;
    public static ObservableList<ComboBoxContainer> cComboBoxCustomerIDs = FXCollections.observableArrayList();
    public static ObservableList<ComboBoxContainer> comboBoxCustomerIDs = FXCollections.observableArrayList();
    public static ObservableList<ComboBoxContainer> orderComboBoxData = FXCollections.observableArrayList();
    public static ObservableList<ComboBoxContainer> employeeComboBoxData = FXCollections.observableArrayList();


    public static void queryOrderTable(Statement stmt){
        orderData = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT * FROM JR_Orders";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                Order temp = new Order();
                temp.setCustomerID(rs.getInt("CUSTOMERID"));
                temp.setOrderID(rs.getInt("ORDERID"));
                temp.setdDate(rs.getDate("DELIVERYDATE").toLocalDate());
                temp.setsDate(rs.getDate("SUBMISSIONDATE").toLocalDate());
                temp.setCost(rs.getFloat("COST"));
                temp.setStatus(rs.getInt("STATUS"));
                temp.setOrderSize(rs.getInt("ORDERSIZE"));
                temp.setOrderNotes(rs.getString("ORDERNOTES"));
                temp.setStoreID(rs.getInt("STOREID"));
                temp.setStatusString(temp.getStatus());
                temp.setOrderSizeString(temp.getOrderSize());
                orderData.add(temp);
            }

            CateringApp.controller.orderTable.setItems(orderData);

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void queryCustomerTable(Statement stmt){
        customerData = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT * FROM JR_Customers";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                Customer temp = new Customer();
                temp.setCustomerID(rs.getInt("CUSTOMERID"));
                temp.setAddress(rs.getString("ADDRESS"));
                temp.setCity(rs.getString("CITY"));
                temp.setState(rs.getString("STATE"));
                temp.setFirstName(rs.getString("FIRSTNAME"));
                temp.setLastName(rs.getString("LASTNAME"));
                temp.setPhoneNum(rs.getString("PHONENUMBER"));
                customerData.add(temp);
            }
            CateringApp.controller.customerTable.setItems(customerData);

        } catch(Exception e){
            e.printStackTrace();
        }
    }


    public static void queryEmployeeTable(Statement stmt){
        employeeData = FXCollections.observableArrayList();
        try{
            String SQL = "SELECT * FROM JR_Employees";
            ResultSet rs = stmt.executeQuery(SQL);
            while (rs.next()){
                Employee temp = new Employee();
                temp.setFirstName(rs.getString("FIRSTNAME"));
                temp.setLastName(rs.getString("LASTNAME"));
                temp.setEmployeeID(rs.getInt("EMPLOYEEID"));
                temp.setAvailability(Integer.parseInt(rs.getString("AVAILABILITY")));
                temp.setCapability(Integer.parseInt(rs.getString("CAPABILITIES")));
                temp.setPhoneNum(rs.getString("PHONENUMBER"));
                temp.setStoreID(rs.getInt("STOREID"));
                temp.setName(temp.getLastName() + ", " + temp.getFirstName());
                temp.setAvailabilityString(temp.createAvailabilityStr());
                temp.setCapabilityString(temp.createCapabilityStr());
                employeeData.add(temp);
            }
            CateringApp.controller.employeeTable.setItems(employeeData);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void queryAssignments(Statement statement, int id){
        detailedData = FXCollections.observableArrayList();
        detailedData.clear();
        try {
            String SQL = "SELECT EMPLOYEEID, JR_EMPLOYEEASSIGNMENT.TASK, JR_EMPLOYEES.LASTNAME, JR_EMPLOYEES.FIRSTNAME\n" +
                    "FROM JR_EMPLOYEEASSIGNMENT\n" +
                    "JOIN JR_EMPLOYEES USING(EMPLOYEEID)\n" +
                    "WHERE ORDERID =  " + id;
            ResultSet rs = statement.executeQuery(SQL);
            while (rs.next()) {
                EmployeeAssignment temp = new EmployeeAssignment(rs.getInt("EMPLOYEEID"), rs.getString("TASK"),
                        rs.getString("LASTNAME")+ ",  " + rs.getString("FIRSTNAME"));
                detailedData.add(temp);
            }
            CateringApp.controller.detailTable.setItems(detailedData);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void refreshCustomerCombobox(Statement stmt){
        try {
            comboBoxCustomerIDs.clear();
            cComboBoxCustomerIDs.clear();

            CateringApp.controller.cCustomerComboBox.setItems(cComboBoxCustomerIDs);
            CateringApp.controller.CustomerComboBox.setItems(comboBoxCustomerIDs);
            ResultSet rs = stmt.executeQuery("SELECT CUSTOMERID, LASTNAME, FIRSTNAME FROM JR_Customers");

            while (rs.next()) {
                cComboBoxCustomerIDs.add(new ComboBoxContainer(rs.getInt("CustomerID"),  rs.getString("LASTNAME") + ", " + rs.getString("FIRSTNAME")));
                comboBoxCustomerIDs.add(new ComboBoxContainer(rs.getInt("CustomerID"),  rs.getString("LASTNAME") + ", " + rs.getString("FIRSTNAME")));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }


    public static void refreshEmployeeCombobox(Statement stmt){
        try {
            employeeComboBoxData.clear();
            CateringApp.controller.eEmployeeSelector.setItems(employeeComboBoxData);
            ResultSet rs = stmt.executeQuery("SELECT EMPLOYEEID, LASTNAME, FIRSTNAME FROM JR_EMPLOYEES");
            while (rs.next()){
                employeeComboBoxData.add(new ComboBoxContainer(rs.getInt("EMPLOYEEID"), rs.getString("LASTNAME") + ", " + rs.getString("FIRSTNAME")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void refreshOrderCombobox(Statement stmt){
        try {
            orderComboBoxData.clear();
            CateringApp.controller.orderSelector.setItems(orderComboBoxData);
            CateringApp.controller.dOrderSelector.setItems(orderComboBoxData);
            ResultSet rs = stmt.executeQuery("SELECT ORDERID, CUSTOMERID FROM JR_ORDERS");

            while (rs.next()) {
                orderComboBoxData.add(new ComboBoxContainer(rs.getInt("ORDERID"),  "for customer id: "+ rs.getInt("CUSTOMERID")));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }


    public static void initCombos(Statement stmt){
        initStoreCombo(stmt);
        initOrderSizeCombo();
        refreshEmployeeCombobox(stmt);
    }


    public static void initStoreCombo(Statement stmt){
        try {
            ObservableList<ComboBoxContainer> storeContainer = FXCollections.observableArrayList();
            ResultSet rs = stmt.executeQuery("SELECT STOREID, PHONENUMBER FROM JR_STORES");

            while (rs.next()){
                storeContainer.add(new ComboBoxContainer(rs.getInt("STOREID"), rs.getString("PHONENUMBER")));
            }

            CateringApp.controller.oStoreComboBox.setItems(storeContainer);
            CateringApp.controller.eStoreSelector.setItems(storeContainer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void initOrderSizeCombo(){
        ObservableList<OrderSize> sizeData = FXCollections.observableArrayList();
        sizeData.addAll(Arrays.asList(OrderSize.values()));
        CateringApp.controller.orderSize.setItems(sizeData);
    }
}
