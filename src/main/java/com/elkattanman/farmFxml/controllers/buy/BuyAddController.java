package com.elkattanman.farmFxml.controllers.buy;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Buy;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.BuyRepository;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
    private final CapitalRepository capitalRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public BuyAddController(BuyRepository buyRepository, TypeRepository typeRepository, CapitalRepository capitalRepository) {
        this.buyRepository = buyRepository;
        this.typeRepository = typeRepository;
        this.capitalRepository = capitalRepository;
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
            price = Double.parseDouble(StringUtils.trimToEmpty(priceTF.getText()))  ;
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
        if (isInEditMode) {
            handleEditOperation();
            return;
        }
        if (!makeBuy())return;
        Capital capital = capitalRepository.findById(1).get();

        if(capital.getCurrentTotal()< myBuy.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }
        myBuy.getType().setTotal(myBuy.getNumber() + myBuy.getType().getTotal() );
        typeRepository.save(myBuy.getType()) ;
        capital.setBuy(capital.getBuy()+myBuy.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()-myBuy.getCost());
        capitalRepository.save(capital);
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
        System.out.println("ediit add " + myBuy.getType().getTotal());
        Buy old=myBuy;
        double oldCost=myBuy.getCost();
        int oldNumber =myBuy.getNumber() ;

        if(!makeBuy())return;
        Capital capital = capitalRepository.findById(1).get();
        if(capital.getCurrentTotal()+oldCost < myBuy.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }
        //amr alaa

        if(old.getType().getTotal() - oldNumber < 0 ){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation",   "لا يمكن و لديك "+ old.getType().getTotal());
            return;
        }
        old.getType().setTotal( old.getType().getTotal() - oldNumber );
        typeRepository.save(old.getType()) ;
        myBuy.getType().setTotal(old.getType().getTotal());
        myBuy.getType().setTotal( myBuy.getType().getTotal() + myBuy.getNumber());
        typeRepository.save(myBuy.getType()) ;
        //end amr alaa
        capital.setBuy(capital.getBuy()-old.getCost()+myBuy.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()+old.getCost()-myBuy.getCost());
        capitalRepository.save(capital);
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