package jesse.rossetti.SQL.util;


import javafx.scene.control.TextFormatter;
import jesse.rossetti.CateringApp;

import java.util.function.UnaryOperator;

public class GUIUtil {
    static UnaryOperator<TextFormatter.Change> filter = t -> {
        if (t.isReplaced())
            if(t.getText().matches("[^0-9]"))
                t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


        if (t.isAdded()) {
            if (t.getControlText().contains(".")) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            } else if (t.getText().matches("[^0-9.]")) {
                t.setText("");
            }
        }

        return t;
    };

    public static void formatTextBoxes(){
        formatCostTextField();
        formatStateField();
        formatPhoneNumberField();
    }

    public static void formatCostTextField(){
        CateringApp.controller.OrderCost.setTextFormatter(new TextFormatter<>(filter));
    }

    public static void formatStateField(){
        CateringApp.controller.CustomerState.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (CateringApp.controller.CustomerState.getText().length() >= 2) {
                    CateringApp.controller.CustomerState.setText(CateringApp.controller.CustomerState.getText().substring(0, 2));
                }
            }
        });
    }

    public static void formatPhoneNumberField(){
        CateringApp.controller.CustomerPhoneNumber.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (CateringApp.controller.CustomerPhoneNumber.getText().length() >= 10) {
                    CateringApp.controller.CustomerPhoneNumber.setText(CateringApp.controller.CustomerPhoneNumber.getText().substring(0, 10));
                }
            }
        });
        CateringApp.controller.EmployeePhoneNumber.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (CateringApp.controller.EmployeePhoneNumber.getText().length() >= 10) {
                    CateringApp.controller.EmployeePhoneNumber.setText(CateringApp.controller.EmployeePhoneNumber.getText().substring(0, 10));
                }
            }
        });

    }
}
