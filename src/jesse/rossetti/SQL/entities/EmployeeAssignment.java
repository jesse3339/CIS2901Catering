package jesse.rossetti.SQL.entities;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import jesse.rossetti.SQL.interfaces.IEntity;

import java.sql.Connection;

public class EmployeeAssignment implements IEntity<EmployeeAssignment> {

    public int employeeid;
    public String name;
    public SimpleStringProperty employee;
    public SimpleStringProperty task;

    public EmployeeAssignment(int empNum, String tsk, String name){
        employeeid = empNum;
        this.name = name;
        employee = new SimpleStringProperty(empNum + ", " + name);
        task = new SimpleStringProperty(tsk);
    }

    public String getEmployee() {
        return employee.get();
    }

    public SimpleStringProperty employeeProperty() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee.set(employee);
    }

    public String getTask() {
        return task.get();
    }

    public SimpleStringProperty taskProperty() {
        return task;
    }

    public void setTask(String task) {
        this.task.set(task);
    }

    @Override
    public void addToTable(Connection conn) {

    }
}
