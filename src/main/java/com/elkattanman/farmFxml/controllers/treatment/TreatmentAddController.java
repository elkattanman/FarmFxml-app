package com.elkattanman.farmFxml.controllers.treatment;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Treatment;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.repositories.TreatmentRepository;
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
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/treatment/add_treatment.fxml")
public class TreatmentAddController implements Initializable {
    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXComboBox<Type> typeCB;

    @FXML
    private JFXDatePicker dateDP;

    @FXML
    private JFXTextField nameTF, priceTF;

    private Boolean isInEditMode = Boolean.FALSE;
    private CallBack callBack;
    private Treatment myTreatment;

    private final TypeRepository typeRepository;
    private final TreatmentRepository treatmentRepository;
    private final CapitalRepository capitalRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public TreatmentAddController(TypeRepository typeRepository, TreatmentRepository treatmentRepository, CapitalRepository capitalRepository) {
        this.typeRepository = typeRepository;
        this.treatmentRepository = treatmentRepository;
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
        typeList.setAll(typeRepository.findAllByTreatmentTrue());
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

    private boolean makeTreatment(){
        String name= StringUtils.trimToEmpty(nameTF.getText());
        Type selectedItem = typeCB.getSelectionModel().getSelectedItem();
        LocalDate date = dateDP.getValue();
        int num=0;
        double price=0;
        try {
            price = Double.parseDouble(StringUtils.trimToEmpty(priceTF.getText()));
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل قيمه رقميه ");
            return false;
        }

        if(date==null || selectedItem==null || name.isEmpty()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل جميع الحقول ");
            return false;
        }

        myTreatment= Treatment.builder()
                .id(myTreatment.getId())
                .type(selectedItem)
                .date(date)
                .cost(price)
                .name(name)
                .build();
        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (isInEditMode) {
            handleEditOperation();
            return;
        }
        if (!makeTreatment())return;
        Capital capital = capitalRepository.findById(1).get();
        if(capital.getCurrentTotal()< myTreatment.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }

        Treatment save = treatmentRepository.save(myTreatment);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Treatment treatment) {
        myTreatment=treatment;
        typeCB.getSelectionModel().select(treatment.getType());
        dateDP.setValue(treatment.getDate());
        priceTF.setText(""+treatment.getCost());
        nameTF.setText(""+treatment.getName());
    }

    private void clearEntries() {
        inflateUI(new Treatment());
    }

    private void handleEditOperation() {
        double oldCost=myTreatment.getCost();
        if(!makeTreatment())return;
        Capital capital = capitalRepository.findById(1).get();
        if(capital.getCurrentTotal()+oldCost < myTreatment.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }
        Treatment save = treatmentRepository.save(myTreatment);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
