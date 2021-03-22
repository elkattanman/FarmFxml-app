package com.elkattanman.farmFxml.controllers.death;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Death;
import com.elkattanman.farmFxml.domain.Sale;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.DeathRepository;
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
@FxmlView("/FXML/death/add_death.fxml")
public class DeathAddController implements Initializable {

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

    private Death myDeath;

    private final TypeRepository typeRepository;
    private final DeathRepository deathRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public DeathAddController(TypeRepository typeRepository, DeathRepository deathRepository) {
        this.typeRepository = typeRepository;
        this.deathRepository = deathRepository;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setInEditMode(Boolean inEditMode) {
        isInEditMode = inEditMode;
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeList.setAll(typeRepository.findAllByDeathTrue());
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
        try {
            num = Integer.parseInt(StringUtils.trimToEmpty(numTF.getText()));
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل قيمه رقميه ");
            return false;
        }

        if(date==null || selectedItem==null){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل جميع الحقول ");
            return false;
        }

        myDeath= Death.builder()
                .id(myDeath.getId())
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
        if (!makeBuy())return;
        int oldTotal = myDeath.getType().getTotal() ;

        if(myDeath.getType().getTotal() - myDeath.getNumber() < 0 ){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation",   "لا يمكن و لديك "+ oldTotal);
            return;
        }
        myDeath.getType().setTotal( myDeath.getType().getTotal() - myDeath.getNumber() );
        typeRepository.save(myDeath.getType()) ;
        Death save = deathRepository.save(myDeath);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Death death) {
        myDeath=death;
        typeCB.getSelectionModel().select(death.getType());
        dateDP.setValue(death.getDate());
        numTF.setText(""+death.getNumber());
    }

    private void clearEntries() {
        inflateUI(new Death());
    }

    private void handleEditOperation() {
        Death old = myDeath ;
        int oldNumber = myDeath.getNumber() ;
        if(!makeBuy())return;

        //amr alaa
        old.getType().setTotal( old.getType().getTotal() + oldNumber );
        typeRepository.save(old.getType()) ;
        myDeath.getType().setTotal(old.getType().getTotal());
        int oldTotal = old.getType().getTotal() ;

        if(myDeath.getType().getTotal() - myDeath.getNumber() < 0 ){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation",   "لا يمكن و لديك "+ oldTotal);
            return;
        }
        myDeath.getType().setTotal( myDeath.getType().getTotal() - myDeath.getNumber() );
        typeRepository.save(myDeath.getType()) ;
        //end amr alaa

        Death save = deathRepository.save(myDeath);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
