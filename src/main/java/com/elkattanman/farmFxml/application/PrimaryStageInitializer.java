package com.elkattanman.farmFxml.application;

import com.elkattanman.farmFxml.controllers.MainController;
import com.elkattanman.farmFxml.controllers.SplashScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="moustafaQattan8@gmail.com">Mustafa Khaled</a>
 */

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {

    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.stage;

//        TODO uncomment it when u ready to use
//        Scene scene = new Scene(fxWeaver.loadView(SplashScreen.class), 1200, 700);

        Scene scene = new Scene(fxWeaver.loadView(MainController.class), 1200, 700);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}