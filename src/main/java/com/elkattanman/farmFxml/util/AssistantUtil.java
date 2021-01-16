package com.elkattanman.farmFxml.util;

import com.elkattanman.farmFxml.controllers.MainController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.event.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class AssistantUtil {

    public final String ICON_IMAGE_LOC = "/img/icon.ico";
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private double xoff = 0, yoff = 0;

    public void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(ICON_IMAGE_LOC));
    }

    public Stage getStage(Parent rootPane){
        Stage st=(Stage)rootPane.getScene().getWindow();
        return st;
    }

    public<V extends Parent> void loadWindow(Stage parentStage, V view) {
        Stage stage = null;
        if (parentStage != null) {
            stage = parentStage;
        } else {
            stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Version 1.0");
        }
        stage.setScene(new Scene(view));
        stage.show();
        setStageIcon(stage);
    }

    public void makeDraggable(Pane rootPane){
        rootPane.setOnMousePressed(mouseEvent -> {
            xoff = mouseEvent.getSceneX() ;
            yoff = mouseEvent.getSceneY() ;
        });
        rootPane.setOnMouseDragged(mouseEvent -> {
            Stage stage=(Stage)rootPane.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - xoff);
            stage.setY(mouseEvent.getScreenY() - yoff);
        });
    }

    public void initDrawer(JFXDrawer drawer, JFXHamburger hamburger, VBox toolbar) {
        try {

            drawer.setSidePane(toolbar);
//            ToolbarController controller = loader.getController();
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            drawer.toggle();
        });
        drawer.setOnDrawerOpening((event) -> {
            task.setRate(task.getRate() * -1);
            task.play();
            drawer.toFront();
        });
        drawer.setOnDrawerClosed((event) -> {
            drawer.toBack();
            task.setRate(task.getRate() * -1);
            task.play();
        });
    }

    public String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }

    public String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
    }

    public void openFileWithDesktop(File file) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException ex) {
            log.error("An exception occurred!",ex);
        }
    }
}
