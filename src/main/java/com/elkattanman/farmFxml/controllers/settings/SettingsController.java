package com.elkattanman.farmFxml.controllers.settings;

import com.elkattanman.farmFxml.util.AlertMaker;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/settings.fxml")
public class SettingsController implements Initializable {


    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String uname = username.getText();
        String pass = password.getText();

        Preferences preferences = Preferences.getPreferences();

        preferences.setUsername(uname);
        preferences.setPassword(pass);

        Preferences.writePreferenceToFile(preferences);
        AlertMaker.showMaterialDialog(null, null, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }

    private Stage getStage() {
        return ((Stage) username.getScene().getWindow());
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        username.setText(String.valueOf(preferences.getUsername()));
        password.setText(String.valueOf(preferences.getPassword()));
    }


    public void handleDatabaseExportAction(ActionEvent actionEvent) {

    }
}
