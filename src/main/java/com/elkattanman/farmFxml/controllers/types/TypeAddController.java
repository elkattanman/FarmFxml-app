package com.elkattanman.farmFxml.controllers.types;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.TypeRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/types/add_type.fxml")
public class TypeAddController implements Initializable {


    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXTextField nameTF;

    @FXML
    private JFXCheckBox saleCB, buyCB, bornsCB, deathCB, feedCB, reserveCB, treatmentCB, spendingCB;

    private Boolean isInEditMode = Boolean.FALSE;

    private CallBack callBack;

    private Type myType;

    private final TypeRepository typeRepository;

    public TypeAddController(TypeRepository typeRepository) {
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

    }

    private boolean makeType(){
        String name = StringUtils.trimToEmpty(nameTF.getText());
        if (name.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return false;
        }
        if(!saleCB.isSelected()&& !buyCB.isSelected()&& !bornsCB.isSelected() &&!deathCB.isSelected()&& feedCB.isSelected() && reserveCB.isSelected() && treatmentCB.isSelected() && spendingCB.isSelected()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك اختر البنود");
            return false;
        }
        myType=Type.builder()
                .id(myType.getId())
                .name(name)
                .sale(saleCB.isSelected())
                .buy(buyCB.isSelected())
                .borns(bornsCB.isSelected())
                .death(deathCB.isSelected())
                .feed(feedCB.isSelected())
                .reserve(reserveCB.isSelected())
                .treatment(treatmentCB.isSelected())
                .spending(spendingCB.isSelected())
                .total(myType.getTotal())
                .build();
        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (!makeType())return;

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Type save = typeRepository.save(myType);
        callBack.callBack(save);
        System.out.println(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Type type) {
        myType=type;
        System.out.println("myType :: :" + myType.getTotal() );
        nameTF.setText(type.getName());
        saleCB.setSelected(type.getSale());
        buyCB.setSelected(type.getBuy());
        bornsCB.setSelected(type.getBorns());
        deathCB.setSelected(type.getDeath());
        feedCB.setSelected(type.getFeed());
        reserveCB.setSelected(type.getReserve());
        treatmentCB.setSelected(type.getTreatment());
        spendingCB.setSelected(type.getSpending());
    }

    private void clearEntries() {
        inflateUI(new Type());
    }

    private void handleEditOperation() {
        if(!makeType())return;
        Type save = typeRepository.save(myType);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
