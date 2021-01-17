package com.elkattanman.farmFxml.controllers.reserve;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Reserve;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.ReserveRepository;
import com.elkattanman.farmFxml.repositories.TypeRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.jfoenix.controls.JFXButton;
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
@FxmlView("/FXML/reserve/add_reserve.fxml")
public class ReserveAddController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXComboBox<Type> typeCB;

    @FXML
    private JFXDatePicker dateDP;

    @FXML
    private JFXTextField nameTF, numTF, priceTF;

    private Boolean isInEditMode = Boolean.FALSE;
    private CallBack callBack;
    private Reserve myReserve;

    private final TypeRepository typeRepository;
    private final ReserveRepository reserveRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public ReserveAddController(TypeRepository typeRepository, ReserveRepository reserveRepository) {
        this.typeRepository = typeRepository;
        this.reserveRepository = reserveRepository;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setInEditMode(Boolean inEditMode) {
        isInEditMode = inEditMode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeList.setAll(typeRepository.findAllByReserveTrue());
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

    private boolean makeReserve(){
        String name= StringUtils.trimToEmpty(nameTF.getText());
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

        if(date==null || selectedItem==null || name.isEmpty()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل جميع الحقول ");
            return false;
        }

        myReserve= Reserve.builder()
                .id(myReserve.getId())
                .type(selectedItem)
                .date(date)
                .cost(price)
                .number(num)
                .clientName(name)
                .build();
        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (!makeReserve())return;
        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Reserve save = reserveRepository.save(myReserve);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Reserve reserve) {
        myReserve=reserve;
        typeCB.getSelectionModel().select(reserve.getType());
        dateDP.setValue(reserve.getDate());
        numTF.setText(""+reserve.getNumber());
        priceTF.setText(""+reserve.getCost());
        nameTF.setText(""+reserve.getClientName());
    }

    private void clearEntries() {
        inflateUI(new Reserve());
    }

    private void handleEditOperation() {
        if(!makeReserve())return;
        Reserve save = reserveRepository.save(myReserve);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
