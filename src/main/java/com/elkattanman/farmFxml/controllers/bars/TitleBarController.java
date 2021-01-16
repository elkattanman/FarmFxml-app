package com.elkattanman.farmFxml.controllers.bars;

import com.elkattanman.farmFxml.controllers.MainController;
import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/titleBar.fxml")
public class TitleBarController implements Initializable {

    @Autowired private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void closeScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.close();
    }

    private Stage getStage(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    @FXML
    public void minimizeScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.setIconified(true);
    }

    public void fullScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.setFullScreen(!stage.isFullScreen());
    }

    public void getHome(MouseEvent mouseEvent) {
        AssistantUtil.loadWindow(getStage(mouseEvent), fxWeaver.loadView(MainController.class));
    }
}
