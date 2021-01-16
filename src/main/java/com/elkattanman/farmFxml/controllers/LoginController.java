package com.elkattanman.farmFxml.controllers;

import com.elkattanman.farmFxml.controllers.settings.Preferences;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.elkattanman.farmFxml.util.AssistantUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/Login.fxml")
@Slf4j
public class LoginController implements Initializable {

    @Autowired private FxWeaver fxWeaver;

    @FXML private AnchorPane rootPane;

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    private Preferences preference;

    public LoginController() {}

    private void Animation() {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1.5), rootPane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition1.play();
    }

    @FXML
    private void loginAction(){
        doLogin();
    }
    void doLogin(){

        String uname = StringUtils.trimToEmpty(username.getText());
        String pword = DigestUtils.shaHex(password.getText());
        if (uname.equals(preference.getUsername()) && pword.equals(preference.getPassword())) {
            AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane), fxWeaver.loadView(MainController.class));
            log.info("User successfully logged in {}", uname);
        } else {
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
            AlertMaker.showSimpleAlert("Email or password not valid", "اسم المستخدم او كلمة المرور خطأ");
        }
    }

    @FXML
    private void closeButtonAction(){
        Stage st=(Stage)rootPane.getScene().getWindow();st.close();
    }

    @FXML
    private void minimizeButtonAction(){
        Stage st=(Stage)rootPane.getScene().getWindow();st.setIconified(true);
    }

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animation();
        preference=Preferences.getPreferences();
    }


    public void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            doLogin();
        }
    }
}
