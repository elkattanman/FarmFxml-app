package com.elkattanman.farmFxml.controllers.borns;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Born;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.BornRepository;
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
@FxmlView("/FXML/borns/add_born.fxml")
public class BornAddController implements Initializable {


    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    private JFXComboBox<Type> typeCB;

    @FXML
    private JFXDatePicker dateDP;

    @FXML
    private JFXTextField numTF;

    private Boolean isInEditMode = Boolean.FALSE;

    private CallBack callBack;

    private Born myBorn;


    private final TypeRepository typeRepository;
    private final BornRepository bornRepository;

    private ObservableList<Type> typeList;

    public BornAddController(TypeRepository typeRepository, BornRepository bornRepository) {
        this.typeRepository = typeRepository;
        this.bornRepository = bornRepository;
        typeList = FXCollections.observableArrayList();
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setInEditMode(Boolean inEditMode) {
        isInEditMode = inEditMode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeList.setAll(typeRepository.findAllByBornsTrue());
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

    private boolean makeBorn() {
        Type selectedItem = typeCB.getSelectionModel().getSelectedItem();
        LocalDate date = dateDP.getValue();
        int num = 0;
        double price = 0;
        try {
            num = Integer.parseInt(StringUtils.trimToEmpty(numTF.getText()));
        } catch (Exception ex) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل قيمه رقميه ");
            return false;
        }

        if (date == null || selectedItem == null) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل جميع الحقول ");
            return false;
        }
        myBorn= Born.builder()
                .id(myBorn.getId())
                .type(selectedItem)
                .date(date)
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
        if (!makeBorn())return;
        myBorn.getType().setTotal(myBorn.getNumber());
        Born save = bornRepository.save(myBorn);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Born born) {
        myBorn=born;
        typeCB.getSelectionModel().select(born.getType());
        dateDP.setValue(born.getDate());
        numTF.setText(""+born.getNumber());
    }

    private void clearEntries() {
        inflateUI(new Born());
    }

    private void handleEditOperation() {
        int old = myBorn.getNumber();
        if(!makeBorn())return;
        myBorn.getType().setTotal(myBorn.getType().getTotal()-old+myBorn.getNumber());
        Born save = bornRepository.save(myBorn);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
