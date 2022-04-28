package jesse.rossetti.SQL.util;

import jesse.rossetti.CateringApp;
import jesse.rossetti.SQL.entities.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

// This class contains a series of listeners that will handle events during a combobox selection.
// They will fill corresponding fields or tables on change.
public class EditUtil {

    public static void onCustomerSelection(Connection connection){
        CateringApp.controller.cCustomerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            try {
                Statement statement = connection.createStatement();
                String SQL = "SELECT * FROM JR_CUSTOMERS WHERE CUSTOMERID=" + newValue.id;
                ResultSet rs = statement.executeQuery(SQL);

                while (rs.next()){
                    CateringApp.controller.CustomerLastName.setText(rs.getString("LASTNAME"));
                    CateringApp.controller.CustomerFirstName.setText(rs.getString("FIRSTNAME"));
                    CateringApp.controller.CustomerAddress.setText(rs.getString("ADDRESS"));
                    CateringApp.controller.CustomerCity.setText(rs.getString("CITY"));
                    CateringApp.controller.CustomerState.setText(rs.getString("STATE"));
                    CateringApp.controller.CustomerPhoneNumber.setText(rs.getString("PHONENUMBER"));
                }
                statement.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void onOrderSelection(Connection conn){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        CateringApp.controller.orderSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            try {
                Statement statement = conn.createStatement();
                String SQL = "SELECT * FROM JR_ORDERS WHERE ORDERID=" + newValue.id;
                ResultSet rs = statement.executeQuery(SQL);

                while (rs.next()){
                    CateringApp.controller.oStoreComboBox.getSelectionModel().clearSelection();
                    CateringApp.controller.CustomerComboBox.getSelectionModel().clearSelection();
                    CateringApp.controller.OrderDeliveryDate.getEditor().setText(rs.getDate("DELIVERYDATE").toLocalDate().format(dateTimeFormatter));
                    CateringApp.controller.OrderCost.setText(Float.toString(rs.getFloat("COST")));
                    CateringApp.controller.orderSize.getSelectionModel().select(rs.getInt("ORDERSIZE"));
                    CateringApp.controller.OrderNotes.setText(rs.getString("ORDERNOTES"));
                }
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void onEmployeeSelection(Connection conn){
        CateringApp.controller.eEmployeeSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            try {
                Statement statement = conn.createStatement();
                String SQL = "SELECT * FROM JR_EMPLOYEES WHERE EMPLOYEEID=" + newValue.id;
                ResultSet rs = statement.executeQuery(SQL);

                while (rs.next()){
                    CateringApp.controller.eStoreSelector.getSelectionModel().clearSelection();
                    CateringApp.controller.EmployeeLastName.setText(rs.getString("LASTNAME"));
                    CateringApp.controller.EmployeeFirstName.setText(rs.getString("FIRSTNAME"));
                    CateringApp.controller.EmployeePhoneNumber.setText(rs.getString("PHONENUMBER"));


                    int availability = rs.getInt("AVAILABILITY");
                    Boolean[] bAvail = Employee.convertIntToBoolArray(String.valueOf(availability), 8);

                    if(bAvail[0]){
                        CateringApp.controller.eCheckSunday.setSelected(true);
                    }
                    if(bAvail[1]){
                        CateringApp.controller.eCheckMonday.setSelected(true);
                    }
                    if(bAvail[2]){
                        CateringApp.controller.eCheckTuesday.setSelected(true);
                    }
                    if(bAvail[3]){
                        CateringApp.controller.eCheckWednesday.setSelected(true);
                    }
                    if(bAvail[4]){
                        CateringApp.controller.eCheckThursday.setSelected(true);
                    }
                    if(bAvail[5]){
                        CateringApp.controller.eCheckFriday.setSelected(true);
                    }
                    if(bAvail[6]){
                        CateringApp.controller.eCheckSaturday.setSelected(true);
                    }

                    int capability = rs.getInt("CAPABILITIES");
                    Boolean[] bCaps = Employee.convertIntToBoolArray(String.valueOf(capability), 4);

                    if(bCaps[0]){
                        CateringApp.controller.eCheckDelivery.setSelected(true);
                    }
                    if(bCaps[1]){
                        CateringApp.controller.eCheckFoodPrep.setSelected(true);
                    }
                    if(bCaps[2]){
                        CateringApp.controller.eCheckServing.setSelected(true);
                    }

                }
                statement.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void onDetailView(Connection conn){
        CateringApp.controller.dOrderSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null){
                return;
            }
            try {
                Statement statement = conn.createStatement();
                String SQL = "SELECT ORDERNOTES FROM JR_ORDERS WHERE ORDERID=" + newValue.id;
                ResultSet rs = statement.executeQuery(SQL);

                while (rs.next()){
                    CateringApp.controller.dTextArea.setText(rs.getString("ORDERNOTES"));
                }
                TableUtil.queryAssignments(statement, newValue.id);
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void initListeners(Connection conn){
        onDetailView(conn);
        onEmployeeSelection(conn);
        onOrderSelection(conn);
        onCustomerSelection(conn);
    }
}
