package com.elkattanman.farmFxml.controllers.bars;

import com.elkattanman.farmFxml.controllers.AboutController;
import com.elkattanman.farmFxml.controllers.MainController;
import com.elkattanman.farmFxml.controllers.borns.BornsFrameController;
import com.elkattanman.farmFxml.controllers.buy.BuyFrameController;
import com.elkattanman.farmFxml.controllers.death.DeathFrameController;
import com.elkattanman.farmFxml.controllers.feed.FeedFrameController;
import com.elkattanman.farmFxml.controllers.reserve.ReserveFrameController;
import com.elkattanman.farmFxml.controllers.sale.SaleFrameController;
import com.elkattanman.farmFxml.controllers.settings.SettingsController;
import com.elkattanman.farmFxml.controllers.spending.SpendingFrameController;
import com.elkattanman.farmFxml.controllers.treatment.TreatmentFrameController;
import com.elkattanman.farmFxml.controllers.types.TypesFrameController;
import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/menuBar.fxml")
public class MenuBarController implements Initializable {

    private final FxWeaver fxWeaver;

    @FXML
    private MenuBar menuBar;


    public MenuBarController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void goToTypes() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),
                fxWeaver.loadView(TypesFrameController.class));
    }

    public void goToBuy() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar), fxWeaver.loadView(BuyFrameController.class));
    }

    public void goToSale() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(SaleFrameController.class));
    }

    public void goToBorns() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(BornsFrameController.class));
    }

    public void goToDeath() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(DeathFrameController.class));
    }

    public void goToTreatment(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(TreatmentFrameController.class));
    }

    public void goToReserve(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(ReserveFrameController.class));

    }

    public void goToSpending(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(SpendingFrameController.class));
    }

    public void goToFeed(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(FeedFrameController.class));
    }

    public void goSettings(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null,fxWeaver.loadView(SettingsController.class));
    }

    public void goHelp(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null,fxWeaver.loadView(AboutController.class));
    }

    public void goToHome(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(menuBar),fxWeaver.loadView(MainController.class));
    }
}
