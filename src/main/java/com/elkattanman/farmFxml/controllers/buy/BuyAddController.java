package com.elkattanman.farmFxml.controllers.buy;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Buy;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.BuyRepository;
import com.elkattanman.farmFxml.repositories.TypeRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/buy/add_buy.fxml")
public class BuyAddController implements Initializable {

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXComboBox<Type> typeCB;

    @FXML
    private JFXDatePicker dateDP;

    @FXML
    private JFXTextField numTF, priceTF;

    private Boolean isInEditMode = Boolean.FALSE;

    private CallBack callBack;

    private Buy myBuy;

    private final BuyRepository buyRepository;
    private final TypeRepository typeRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public BuyAddController(BuyRepository buyRepository, TypeRepository typeRepository) {
        this.buyRepository = buyRepository;
        this.typeRepository = typeRepository;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setInEditMode(Boolean inEditMode) {
        isInEditMode = inEditMode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeList.setAll(typeRepository.findAllByBuyTrue());
        typeCB.itemsProperty().setValue(typeList);
        typeCB.setConverter(convertComboDisplayList());
    }

    private StringConverter<Type> convertComboDisplayList() {
        return new StringConverter<Type>() {
            @Override
            public String toString(Type product) {
                return product.getName();
            }
            @Override
            public Type fromString(final String string) {
                return typeList.stream().filter(product -> product.getName().equals(string)).findFirst().orElse(null);
            }
        };
    }

    private boolean makeBuy(){
        Type selectedItem = typeCB.getSelectionModel().getSelectedItem();
        LocalDate date = dateDP.getValue();
        int num=0;
        double price=0;
        try {
            num = Integer.parseInt(StringUtils.trimToEmpty(numTF.getText()));
            price = Double.parseDouble(StringUtils.trimToEmpty(priceTF.getText()));
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل قيمه رقميه ");
            return false;
        }

        if(date==null || selectedItem==null){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل جميع الحقول ");
            return false;
        }

        myBuy= Buy.builder()
                .id(myBuy.getId())
                .type(selectedItem)
                .date(date)
                .cost(price)
                .number(num)
                .build();
        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (!makeBuy())return;
        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Buy save = buyRepository.save(myBuy);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Buy buy) {
        myBuy=buy;
        typeCB.getSelectionModel().select(buy.getType());
        dateDP.setValue(buy.getDate());
        numTF.setText(""+buy.getNumber());
        priceTF.setText(""+buy.getCost());
    }

    private void clearEntries() {
        inflateUI(new Buy());
    }

    private void handleEditOperation() {
        if(!makeBuy())return;
        Buy save = buyRepository.save(myBuy);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}





//        Callback<ListView<Type>, ListCell<Type>> factory = lv -> new ListCell<Type>() {
//            @Override
//            protected void updateItem(Type item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? "" : item.getName());
//            }
//        };
//        typeCB.setCellFactory(factory);