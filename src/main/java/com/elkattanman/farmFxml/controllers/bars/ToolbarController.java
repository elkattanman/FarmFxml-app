package com.elkattanman.farmFxml.controllers.bars;

import com.elkattanman.farmFxml.controllers.borns.BornsFrameController;
import com.elkattanman.farmFxml.controllers.buy.BuyFrameController;
import com.elkattanman.farmFxml.controllers.death.DeathFrameController;
import com.elkattanman.farmFxml.controllers.feed.FeedFrameController;
import com.elkattanman.farmFxml.controllers.reserve.ReserveFrameController;
import com.elkattanman.farmFxml.controllers.sale.SaleFrameController;
import com.elkattanman.farmFxml.controllers.spending.SpendingFrameController;
import com.elkattanman.farmFxml.controllers.treatment.TreatmentFrameController;
import com.elkattanman.farmFxml.controllers.types.TypesFrameController;
import com.elkattanman.farmFxml.util.AssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/toolbar.fxml")
public class ToolbarController implements Initializable {



    @FXML
    private VBox rootPane;

    private final FxWeaver fxWeaver;

    public ToolbarController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void goToTypes() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),
                fxWeaver.loadView(TypesFrameController.class));
    }

    public void goToBuy() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane), fxWeaver.loadView(BuyFrameController.class));
    }

    public void goToSale() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(SaleFrameController.class));
    }

    public void goToBorns() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(BornsFrameController.class));
    }

    public void goToDeath() {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(DeathFrameController.class));
    }

    public void goToTreatment(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(TreatmentFrameController.class));
    }

    public void goToReserve(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(ReserveFrameController.class));

    }

    public void goToSpending(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(SpendingFrameController.class));
    }

    public void goToFeed(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane),fxWeaver.loadView(FeedFrameController.class));
    }

    public void gotoTransaction(ActionEvent actionEvent) {

    }
}
