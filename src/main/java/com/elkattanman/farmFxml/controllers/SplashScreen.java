/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elkattanman.farmFxml.controllers;

import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
@FxmlView("/FXML/SplashScreen.fxml")
public class SplashScreen implements Initializable {

    @FXML
    private ImageView logoLabel;

    @FXML
    private Pane spinnerPane;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label helloLabel, nameLabel;

    private final FxWeaver fxWeaver;

    public SplashScreen(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StartAnimation();
    }

    void StartAnimation() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.1), logoLabel);
        translateTransition.setByY(700);
        translateTransition.play();

        TranslateTransition translateTransition0 = new TranslateTransition(Duration.seconds(0.1), nameLabel);
        translateTransition0.setByY(700);
        translateTransition0.play();

        TranslateTransition translateTransition00 = new TranslateTransition(Duration.seconds(0.1), helloLabel);
        translateTransition00.setByY(700);
        translateTransition00.play();

        translateTransition.setOnFinished(event -> {
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(1), logoLabel);
            translateTransition1.setByY(-700);
            translateTransition1.play();

            translateTransition1.setOnFinished(event1 -> {

                nameLabel.setVisible(true);

                TranslateTransition translateTransition11 = new TranslateTransition(Duration.seconds(1), nameLabel);
                translateTransition11.setByY(-700);
                translateTransition11.play();

                translateTransition11.setOnFinished(event2 -> {

                    helloLabel.setVisible(true);
                    TranslateTransition translateTransition111 = new TranslateTransition(Duration.seconds(1), helloLabel);
                    translateTransition111.setByY(-700);
                    translateTransition111.play();

                    translateTransition111.setOnFinished(event3 -> {
                        spinnerPane.setVisible(true);

                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), spinnerPane);
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(1);
                        fadeTransition.play();

                        fadeTransition.setOnFinished(event4 -> {

                            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1.5), rootPane);
                            fadeTransition1.setFromValue(1);
                            fadeTransition1.setToValue(0.2);
                            fadeTransition1.play();

                            fadeTransition1.setOnFinished(event5 -> {
                                //main.closeStage();
                                System.out.println("------- splash screen is closed --------");
                                AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane), fxWeaver.loadView(LoginController.class));
                            });

                        }
                        );

                    });

                });
            });

        });
    }

}
