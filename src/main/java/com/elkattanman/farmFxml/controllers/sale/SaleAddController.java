package com.elkattanman.farmFxml.controllers.sale;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Buy;
import com.elkattanman.farmFxml.domain.Sale;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.SaleRepository;
import com.elkattanman.farmFxml.repositories.TypeRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.elkattanman.farmFxml.util.AssistantUtil;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/sale/add_sale.fxml")
public class SaleAddController implements Initializable {


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

    private Sale mySale;

    private final TypeRepository typeRepository;
    private final SaleRepository saleRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public SaleAddController(TypeRepository typeRepository, SaleRepository saleRepository) {
        this.typeRepository = typeRepository;
        this.saleRepository = saleRepository;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setInEditMode(Boolean inEditMode) {
        isInEditMode = inEditMode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeList.setAll(typeRepository.findAllBySaleTrue());
        typeCB.itemsProperty().setValue(typeList);
        typeCB.setConverter(convertComboDisplayList());
    }

    private StringConverter<Type> convertComboDisplayList() {
        return new StringConverter<Type>() {
            @Override
            public String toString(Type type) {
                return type.getName();
            }
            @Override
            public Type fromString(final String string) {
                return typeList.stream().filter(type -> type.getName().equals(string)).findFirst().orElse(null);
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

        mySale= Sale.builder()
                .id(mySale.getId())
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

        int oldTotal = mySale.getType().getTotal() ;

        if(mySale.getType().getTotal() - mySale.getNumber() < 0 ){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation",   "لا يمكن و لديك "+ oldTotal);
            return;
        }
        mySale.getType().setTotal( mySale.getType().getTotal() - mySale.getNumber() );
        typeRepository.save(mySale.getType()) ;
        Sale save = saleRepository.save(mySale);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Sale sale) {
        mySale=sale;
        typeCB.getSelectionModel().select(sale.getType());
        dateDP.setValue(sale.getDate());
        numTF.setText(""+sale.getNumber());
        priceTF.setText(""+sale.getCost());
    }

    private void clearEntries() {
        inflateUI(new Sale());
    }

    private void handleEditOperation() {
        Sale old = mySale ;
        int oldNumber = mySale.getNumber() ;
        if(!makeBuy())return;

        //amr alaa
        old.getType().setTotal( old.getType().getTotal() + oldNumber );
        typeRepository.save(old.getType()) ;
        mySale.getType().setTotal(old.getType().getTotal());
        int oldTotal = old.getType().getTotal() ;
        if( mySale.getType().getTotal() - mySale.getNumber() < 0 ){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation",   "لا يمكن و لديك "+ oldTotal);
            return;
        }
        mySale.getType().setTotal( mySale.getType().getTotal() - mySale.getNumber() );
        typeRepository.save(mySale.getType()) ;
        //end amr alaa
        Sale save = saleRepository.save(mySale);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
