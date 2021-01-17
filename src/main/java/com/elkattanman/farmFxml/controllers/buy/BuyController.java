package com.elkattanman.farmFxml.controllers.buy;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.types.TypeAddController;
import com.elkattanman.farmFxml.domain.Buy;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.BuyRepository;
import com.elkattanman.farmFxml.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/buy/buy.fxml")
public class BuyController implements Initializable, CallBack<Boolean, Buy> {
    @Autowired
    private FxWeaver fxWeaver;


    @FXML
    private TableView<Buy> table;

    @FXML
    private TableColumn<Buy, Integer> idCol;

    @FXML
    private TableColumn<Buy, String> typeCol;

    @FXML
    private TableColumn<Buy, LocalDate> dateCol;

    @FXML
    private TableColumn<Buy, Integer> numCol;

    @FXML
    private TableColumn<Buy, Double> priceCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Buy> list = FXCollections.observableArrayList();

    private final BuyRepository buyRepository;

    public BuyController(BuyRepository buyRepository) {
        this.buyRepository = buyRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(buyRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Buy> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener( (observable, oldValue, newValue) -> {
            filteredData.setPredicate(buy->{

                if(newValue == null || newValue.isEmpty() ){
                    return true ;
                }
                String lowerCaseFilter = newValue.toLowerCase() ;
                if(buy.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true ;
                }
                return false ;
            });

        });

        SortedList<Buy> sortedBorns = new SortedList<>(filteredData) ;
        sortedBorns.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedBorns);
    }


    @FXML
    void edit(ActionEvent event) {
        Buy selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<BuyAddController, Parent> load = fxWeaver.load(BuyAddController.class);
        BuyAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Buy selectedItem = table.getSelectionModel().getSelectedItem();
        buyRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(buyRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<BuyAddController, Parent> load = fxWeaver.load(BuyAddController.class);
        BuyAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Buy());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Buy obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Buy object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}