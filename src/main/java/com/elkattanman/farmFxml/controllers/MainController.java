package com.elkattanman.farmFxml.controllers;

import com.elkattanman.farmFxml.controllers.bars.ToolbarController;
import com.elkattanman.farmFxml.controllers.borns.BornsFrameController;
import com.elkattanman.farmFxml.controllers.buy.BuyFrameController;
import com.elkattanman.farmFxml.controllers.death.DeathFrameController;
import com.elkattanman.farmFxml.controllers.feed.FeedFrameController;
import com.elkattanman.farmFxml.controllers.reserve.ReserveFrameController;
import com.elkattanman.farmFxml.controllers.sale.SaleFrameController;
import com.elkattanman.farmFxml.controllers.spending.SpendingFrameController;
import com.elkattanman.farmFxml.controllers.treatment.TreatmentFrameController;
import com.elkattanman.farmFxml.controllers.types.TypesFrameController;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static com.elkattanman.farmFxml.util.AssistantUtil.*;


@Component
@FxmlView("/FXML/main.fxml")
public class MainController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML public AnchorPane rootAnchorPane;
    @FXML public JFXTabPane mainTabPane;

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;

    @FXML
    private PieChart gained, paid;

    @FXML
    private Text gainTXT, paidTXT, currentTotalTXT;

    @FXML
    private Tab graphicTap;


    @Autowired private FxWeaver fxWeaver;

    private final CapitalRepository capitalRepository;

    public MainController(CapitalRepository capitalRepository) {
        this.capitalRepository = capitalRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable(rootPane);
        initComponents();
        initDrawer(drawer, hamburger, fxWeaver.loadView(ToolbarController.class));
        graphicTap.setOnSelectionChanged(event -> {
            if(graphicTap.isSelected()) initGraphics();
        });
    }

    private void initGraphics(){
        Capital capital = capitalRepository.findById(1).get();

        currentTotalTXT.setText(""+capital.getCurrentTotal());

        ObservableList<PieChart.Data> data1 = FXCollections.observableArrayList();
        data1.add(new PieChart.Data("مبيعات ( "+capital.getSales()+")",capital.getSales()));
        data1.add(new PieChart.Data("حجز ( "+capital.getReserve()+")",capital.getReserve()));
        gained.setData(data1);
        gainTXT.setText(""+ (capital.getSales() + capital.getReserve() ));

        ObservableList<PieChart.Data> data2 = FXCollections.observableArrayList();
        data2.add(new PieChart.Data("الشراء ( "+capital.getBuy()+")",capital.getBuy()));
        data2.add(new PieChart.Data("الاعلاف ( "+capital.getFeed()+")",capital.getFeed()));
        data2.add(new PieChart.Data("المصاريف ( "+capital.getSpending()+")",capital.getSpending()));
        data2.add(new PieChart.Data("العلاج ( "+capital.getTreatment()+")",capital.getTreatment()));
        paid.setData(data2);
        paidTXT.setText(""+( capital.getBuy() + capital.getFeed() +capital.getSpending() + capital.getTreatment()  ));


    }

    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }

    public void goToTypes() {
        loadWindow(getStage(rootPane),fxWeaver.loadView(TypesFrameController.class));
    }

    public void goToBuy() {
        loadWindow(getStage(rootPane),fxWeaver.loadView(BuyFrameController.class));
    }

    public void goToSale() {
        loadWindow(getStage(rootPane),fxWeaver.loadView(SaleFrameController.class));
    }

    public void goToBorns() {
        loadWindow(getStage(rootPane),fxWeaver.loadView(BornsFrameController.class));
    }

    public void goToDeath() {
        loadWindow(getStage(rootPane),fxWeaver.loadView(DeathFrameController.class));
    }

    public void goToTreatment(ActionEvent actionEvent) {
        loadWindow(getStage(rootPane),fxWeaver.loadView(TreatmentFrameController.class));
    }

    public void goToReserve(ActionEvent actionEvent) {
        loadWindow(getStage(rootPane),fxWeaver.loadView(ReserveFrameController.class));
    }

    public void goToSpending(ActionEvent actionEvent) {
        loadWindow(getStage(rootPane),fxWeaver.loadView(SpendingFrameController.class));
    }

    public void goToFeed(ActionEvent actionEvent) {
        loadWindow(getStage(rootPane),fxWeaver.loadView(FeedFrameController.class));
    }
}
