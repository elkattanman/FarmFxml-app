package com.elkattanman.farmFxml.controllers.settings;

import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.util.AlertMaker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/Capital.fxml")
public class CapitalController implements Initializable {


    public StackPane rootContainer;
    public AnchorPane toBlur;

    @FXML
    private JFXTextField currentTotalTF;


    private final CapitalRepository capitalRepository;

    public CapitalController(CapitalRepository capitalRepository) {
        this.capitalRepository = capitalRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initDefaultValues();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String currString = StringUtils.trimToEmpty(currentTotalTF.getText());
        if(currString.isEmpty()){
            AlertMaker.showMaterialDialog(rootContainer, toBlur, new ArrayList<>(), "الحقل فارغ", "برجاء ادخال قيمه للحقل");
            return;
        }
        double current=0;
        try {
            current=Double.parseDouble(currString);
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootContainer, toBlur, new ArrayList<>(), "قيمه غير صحيحه", "برجاء ادخال قيمه صحيحه");
            return;
        }
        Capital one = capitalRepository.findById(1).get();
        if(one.getTotalPayments()>current){
            AlertMaker.showMaterialDialog(rootContainer, toBlur, new ArrayList<>(), "قيمه صغيره", "برجاء ادخال قيمه اكبر من الديون "+ one.getTotalPayments());
            return;
        }
        one.setBalance(current);
        one.setCurrentTotal(current);
        capitalRepository.save(one);
        AlertMaker.showMaterialDialog(rootContainer, toBlur, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }

    private Stage getStage() {
        return ((Stage) currentTotalTF.getScene().getWindow());
    }

    private void initDefaultValues() {
        Capital one = capitalRepository.findById(1).get();
        currentTotalTF.setText(""+one.getCurrentTotal());
    }

}
