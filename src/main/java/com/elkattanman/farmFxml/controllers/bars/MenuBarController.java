package com.elkattanman.farmFxml.controllers.bars;

import com.elkattanman.farmFxml.controllers.AboutController;
import com.elkattanman.farmFxml.controllers.settings.SettingsController;
import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/menuBar.fxml")
public class MenuBarController implements Initializable {

    private final FxWeaver fxWeaver;


    public MenuBarController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void goSettings(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null,fxWeaver.loadView(SettingsController.class));
    }

    public void goHelp(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null,fxWeaver.loadView(AboutController.class));
    }
}
