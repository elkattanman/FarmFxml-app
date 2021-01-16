package com.elkattanman.farmFxml.controllers.feed;

import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/feed/feeds.fxml")
public class FeedController implements Initializable {
    @Autowired
    private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void refresh(ActionEvent actionEvent) {
    }

    public void edit(ActionEvent actionEvent) {
    }

    public void remove(ActionEvent actionEvent) {
    }

    public void add(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null, fxWeaver.loadView(FeedAddController.class));
    }
}