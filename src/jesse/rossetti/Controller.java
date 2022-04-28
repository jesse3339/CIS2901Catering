package jesse.rossetti;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import jesse.rossetti.SQL.entities.Employee;
import jesse.rossetti.SQL.entities.EmployeeAssignment;
import jesse.rossetti.SQL.util.EditUtil;
import jesse.rossetti.containers.ComboBoxContainer;
import jesse.rossetti.SQL.entities.Customer;
import jesse.rossetti.SQL.entities.Order;
import jesse.rossetti.SQL.util.TableUtil;
import jesse.rossetti.enums.OrderSize;

public class Controller{

    //region connection components and function
    public Connection conn;
    private boolean isConnected;



    /**
     * This function connects the user to the database hosted on the localhost
     * @param actionEvent required for JavaFX
     */
    public void connectToDB(ActionEvent actionEvent){
        try{

            //Establish connection to database
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:" + dbName.getText(), DBUsername.getText(), DBPassword.getText());

            Statement stmt = conn.createStatement();



            //set combobox
            TableUtil.refreshCustomerCombobox(stmt);
            TableUtil.refreshOrderCombobox(stmt);

            //set customer table
            TableUtil.queryCustomerTable(stmt);
            TableUtil.queryEmployeeTable(stmt);
            TableUtil.queryOrderTable(stmt);
            TableUtil.initCombos(stmt);
            EditUtil.initListeners(conn);
            DBConnectionLBL.setText("Connected");
            DBConnectionLBL.setTextFill(Color.GREEN);
            isConnected = true;
            stmt.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, e.getMessage());
            alert.showAndWait();
        }
    }

    //endregion

    //region components
    @FXML
    public PasswordField DBPassword;
    @FXML
    public TextField DBUsername;
    @FXML
    public TextField dbName;
    @FXML
    public Label DBConnectionLBL;
    @FXML
    public ComboBox<ComboBoxContainer> CustomerComboBox;
    @FXML
    public ComboBox<ComboBoxContainer> cCustomerComboBox;
    @FXML
    public TableView<Customer> customerTable = new TableView<>();
    @FXML
    public TableColumn<Customer, Integer> colCID = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCLastName = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCFirstName = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCState = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCCity = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCAddress = new TableColumn<>();
    @FXML
    public TableColumn<Customer, String> colCPhoneNumber = new TableColumn<>();
    @FXML
    public TextField CustomerLastName;
    @FXML
    public TextField CustomerFirstName;
    @FXML
    public TextField CustomerAddress;
    @FXML
    public TextField CustomerCity;
    @FXML
    public TextField CustomerState;
    @FXML
    public TextField CustomerPhoneNumber;
    @FXML
    public TableView<Order> orderTable = new TableView<>();
    @FXML
    public TableColumn<Order, Integer> oColOrderId= new TableColumn<>();
    @FXML
    public TableColumn<Order, Integer> oColCustomerId = new TableColumn<>();
    @FXML
    public TableColumn<Order, LocalDate> oColSDate = new TableColumn<>();
    @FXML
    public TableColumn<Order, LocalDate> oColDDate = new TableColumn<>();
    @FXML
    public TableColumn<Order, Float> oColCost = new TableColumn<>();
    @FXML
    public TableColumn<Order, String> oColStatus = new TableColumn<>();
    @FXML
    public TableColumn<Order, String> oColOrderSize = new TableColumn<>();
    @FXML
    public TableColumn<Order, Integer> oColStoreId = new TableColumn<>();
    @FXML
    public TextField OrderCost;
    @FXML
    public TextArea OrderNotes;
    @FXML
    public DatePicker OrderDeliveryDate;
    @FXML
    public ComboBox<OrderSize> orderSize;
    @FXML
    public ComboBox<ComboBoxContainer> oStoreComboBox;
    @FXML
    public ComboBox<ComboBoxContainer> eStoreSelector;
    @FXML
    public Button oRemoveOrderBtn;
    @FXML
    public ComboBox<ComboBoxContainer> orderSelector;
    @FXML
    public Button oConfirmEditBtn;
    @FXML
    public ComboBox<ComboBoxContainer> eEmployeeSelector;
    @FXML
    public ComboBox<ComboBoxContainer> dOrderSelector;
    @FXML
    public TextField EmployeeLastName;
    @FXML
    public TextField EmployeeFirstName;
    @FXML
    public TextField EmployeePhoneNumber;
    @FXML
    public CheckBox eCheckSunday;
    @FXML
    public CheckBox eCheckMonday;
    @FXML
    public CheckBox eCheckTuesday;
    @FXML
    public CheckBox eCheckWednesday;
    @FXML
    public CheckBox eCheckThursday;
    @FXML
    public CheckBox eCheckFriday;
    @FXML
    public CheckBox eCheckSaturday;
    @FXML
    public CheckBox eCheckFoodPrep;
    @FXML
    public CheckBox eCheckDelivery;
    @FXML
    public CheckBox eCheckServing;
    @FXML
    public Button eAddEmployeeBtn;
    @FXML
    public Button eEditEmployeeBtn;
    @FXML
    public Button eRemoveEmployeeBtn;
    @FXML
    public TableView<Employee> employeeTable = new TableView<>();
    @FXML
    public TableColumn<Employee, Integer> eColEmpID = new TableColumn<>();
    @FXML
    public TableColumn<Employee, Integer> eColEmpStore = new TableColumn<>();
    @FXML
    public TableColumn<Employee, String>  eColName = new TableColumn<>();
    @FXML
    public TableColumn<Employee, String> eColAvail = new TableColumn<>();
    @FXML
    public TableColumn<Employee, String> eColPhone = new TableColumn<>();
    @FXML
    public TableColumn<Employee, String> eColCaps = new TableColumn<>();
    @FXML
    public TextArea dTextArea = new TextArea();
    @FXML
    public TableView<EmployeeAssignment> detailTable = new TableView<>();
    @FXML
    public TableColumn<EmployeeAssignment, String> dColEmployee = new TableColumn<>();
    @FXML
    public TableColumn<EmployeeAssignment, String> dColTask = new TableColumn<>();
    @FXML
    public Button completeOrderBtn = new Button();

    //endregion

    //region customer
    /**
     * Adds a customer based on the value in text fields on the customer tab
     * @param actionEvent required for JavaFX
     */
    public void addCustomer(ActionEvent actionEvent){
        if (cCustomerComboBox.getValue() != null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer currently selected!");
            alert.setContentText("Confirm your edit before adding another customer");
            alert.showAndWait();
            return;
        }
        Customer customer = new Customer(CustomerLastName.getText(), CustomerFirstName.getText(),
                    CustomerAddress.getText(), CustomerCity.getText(), CustomerState.getText(), CustomerPhoneNumber.getText());
        customer.addToTable(conn);

    }

    /**
     * Removes a customer based on the selection on one of the customer ComboBoxes
     * Check the static function in the customer class
     * @param actionEvent required for JavaFX
     */
    public void removeCustomer(ActionEvent actionEvent){

        if(cCustomerComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected!");
            alert.setContentText("No customer was selected, select customer ID in combobox");
            alert.showAndWait();
            return;
        }
        Customer.removeFromTable(conn, cCustomerComboBox.getValue().id);

    }

    public void editCustomer(ActionEvent event){
        if (cCustomerComboBox.getValue()!= null){
            Customer.editEntry(conn, cCustomerComboBox.getValue().id);
        }
    }

    //endregion

    //region order

    public void addOrder(){
        if (CustomerComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected!");
            alert.setContentText("No customer was selected, select customer for the order in the combobox.");
            alert.showAndWait();
            return;
        }
        Order order = new Order(CustomerComboBox.getValue().id, OrderDeliveryDate.getValue(), Float.parseFloat(OrderCost.getText()), 0, orderSize.getValue(), oStoreComboBox.getValue().id, OrderNotes.getText());
        order.addToTable(conn);
    }


    public void removeOrder(){
        if(orderSelector.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No order selected!");
            alert.setContentText("No order was selected, select order ID in combobox");
            alert.showAndWait();
            return;
        }
        Order.removeFromTable(conn, orderSelector.getValue().id);

    }

    public void editOrder(){
        if (CustomerComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No customer selected!");
            alert.setContentText("No customer was selected, select customer for the order in the combobox.");
            alert.showAndWait();
            return;
        }
        if (oStoreComboBox.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No store selected!");
            alert.setContentText("No store was selected, select store for the order in the combobox.");
            alert.showAndWait();
            return;
        }
        if (orderSelector.getValue() != null){
            Order.editEntry(conn, orderSelector.getValue().id);
        }
    }


    public void completeOrder(){
        if (dOrderSelector.getValue() != null){
            Order.complete(conn, dOrderSelector.getValue().id);
        }
    }

    //endregion

    //region employee

    public void addEmployee(){
        //make sure new employee is selected
        if (eEmployeeSelector.getValue() == null){
            //use this if statement if a store is not selected
            if (eStoreSelector.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No store selected!");
                alert.setContentText("No store was selected, select store for the employee in the combobox.");
                alert.showAndWait();
                return;
            }

            Boolean[] availability = new Boolean[8];
            Arrays.fill(availability, Boolean.FALSE);
            if (eCheckSunday.isSelected()){
                availability[1] = true;
            }

            if (eCheckMonday.isSelected()){
                availability[2] = true;
            }

            if (eCheckTuesday.isSelected()){
                availability[3] = true;
            }

            if (eCheckWednesday.isSelected()){
                availability[4] = true;
            }

            if (eCheckThursday.isSelected()){
                availability[5] = true;
            }

            if (eCheckFriday.isSelected()){
                availability[6] = true;
            }

            if (eCheckSaturday.isSelected()){
                availability[7] = true;
            }
            int aval = Integer.parseInt(Employee.convertBoolArrayToInt(availability, 8));

            Boolean[] capability = new Boolean[4];
            Arrays.fill(capability, Boolean.FALSE);
            if(eCheckDelivery.isSelected()){
                capability[1] = true;
            }
            if(eCheckFoodPrep.isSelected()){
                capability[2] = true;
            }
            if(eCheckServing.isSelected()){
                capability[3] = true;
            }
            int cap = Integer.parseInt(Employee.convertBoolArrayToInt(capability, 4));

            Employee emp = new Employee(EmployeeLastName.getText(), EmployeeFirstName.getText(), EmployeePhoneNumber.getText(), aval, cap, eStoreSelector.getValue().id);
            emp.addToTable(conn);
        }
    }

    public void editEmployee(){
        if (eEmployeeSelector.getValue()!= null){
            if(eStoreSelector.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No store selected!");
                alert.setContentText("No store was selected, select store for the employee in the combobox.");
                alert.showAndWait();
                return;
            }

            Boolean[] availability = new Boolean[8];
            Arrays.fill(availability, Boolean.FALSE);
            if (eCheckSunday.isSelected()){
                availability[1] = true;
            }

            if (eCheckMonday.isSelected()){
                availability[2] = true;
            }

            if (eCheckTuesday.isSelected()){
                availability[3] = true;
            }

            if (eCheckWednesday.isSelected()){
                availability[4] = true;
            }

            if (eCheckThursday.isSelected()){
                availability[5] = true;
            }

            if (eCheckFriday.isSelected()){
                availability[6] = true;
            }

            if (eCheckSaturday.isSelected()){
                availability[7] = true;
            }
            int aval = Integer.parseInt(Employee.convertBoolArrayToInt(availability, 8));

            Boolean[] capability = new Boolean[4];
            Arrays.fill(capability, Boolean.FALSE);
            if(eCheckDelivery.isSelected()){
                capability[1] = true;
            }
            if(eCheckFoodPrep.isSelected()){
                capability[2] = true;
            }
            if(eCheckServing.isSelected()){
                capability[3] = true;
            }
            int cap = Integer.parseInt(Employee.convertBoolArrayToInt(capability, 4));

            Employee temp = new Employee(EmployeeLastName.getText(), EmployeeFirstName.getText(), EmployeePhoneNumber.getText(), aval, cap, eStoreSelector.getValue().id);
            temp.editEntry(conn, eEmployeeSelector.getValue().id);

        }
    }

    public void removeEmployee(){
        if(eEmployeeSelector.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No employee selected!");
            alert.setContentText("No employee was selected, select employee ID in combobox");
            alert.showAndWait();
            return;
        }
        Employee.removeFromTable(conn, eEmployeeSelector.getValue().id);
    }
    //endregion

    @FXML
    public void initialize(){
        //this sets up the tables, more stuff needs to be added here but will worry about that later
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        colCAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        colCCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colCFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colCLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCState.setCellValueFactory(new PropertyValueFactory<>("state"));
        colCPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

        oColOrderId.setCellValueFactory(new PropertyValueFactory<>("OrderID"));
        oColCustomerId.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        oColSDate.setCellValueFactory(cellData -> cellData.getValue().sDateProperty());
        oColDDate.setCellValueFactory(cellData -> cellData.getValue().dDateProperty());

        oColSDate.setCellFactory(column -> new TableCell<Order, LocalDate>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        oColDDate.setCellFactory(column -> new TableCell<Order, LocalDate>(){
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        oColCost.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        oColStatus.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
        oColOrderSize.setCellValueFactory(new PropertyValueFactory<>("OrderSizeString"));
        oColStoreId.setCellValueFactory(new PropertyValueFactory<>("StoreID"));

        eColEmpID.setCellValueFactory(new PropertyValueFactory<>("EmployeeID"));
        eColEmpStore.setCellValueFactory(new PropertyValueFactory<>("StoreID"));
        eColName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        eColAvail.setCellValueFactory(new PropertyValueFactory<>("AvailabilityString"));
        eColCaps.setCellValueFactory(new PropertyValueFactory<>("CapabilityString"));
        eColPhone.setCellValueFactory(new PropertyValueFactory<>("PhoneNum"));

        dColEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        dColTask.setCellValueFactory(new PropertyValueFactory<>("Task"));

    }
}
