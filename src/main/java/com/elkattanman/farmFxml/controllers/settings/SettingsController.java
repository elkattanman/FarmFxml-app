package com.elkattanman.farmFxml.controllers.settings;

import com.elkattanman.farmFxml.domain.User;
import com.elkattanman.farmFxml.repositories.UserRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.elkattanman.farmFxml.util.AssistantUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Slf4j
@FxmlView("/FXML/settings.fxml")
public class SettingsController implements Initializable {


    @Value( "${spring.datasource.username}" )
    private String dbUsername;
    @Value( "${spring.datasource.password}" )
    private String dbPassword;

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    private UserRepository userRepository;

    public SettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        Preferences preferences = Preferences.getPreferences();
        String uname = StringUtils.trimToEmpty(username.getText());
        String pword =StringUtils.trimToEmpty(password.getText());
        log.info("user {}", preferences.getUsername());
        Optional<User> byUsername = userRepository.findByUsername(preferences.getUsername());
        if(byUsername.isPresent()){
            User user = byUsername.get();
            user.setUsername(uname);
            user.setPassword(pword);
            userRepository.save(user);
            preferences.setUsername(uname);
            preferences.setPassword(pword);
            Preferences.writePreferenceToFile(preferences);
            AlertMaker.showSimpleAlert("Success", "Settings updated");
            initDefaultValues();
        }
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
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Location to Create Backup");
            File selectedDirectory = directoryChooser.showDialog(getStage());
            if (selectedDirectory == null) {
                AlertMaker.showErrorMessage("Export cancelled", "No Valid Directory Found");
            } else {
                boolean backup = AssistantUtil.backup(dbUsername, dbPassword, "farm_v2", selectedDirectory);
                if(backup)
                    AlertMaker.showSimpleAlert("Success", "تم حفظ نسخه احتياطيه من البيانات");
                else
                    AlertMaker.showErrorMessage("Failed","هفوا هاك خطا فى عمل نسخه احتياطيه للبيانات");
            }
        } catch (IOException e) {
            log.error("error : {}", e);
        } catch (InterruptedException e) {
            log.error("error : {}", e);
        }
    }
}
