package com.elkattanman.farmFxml.controllers.reserve;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/reserve/add_reserve.fxml")
public class ReserveAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, numTF,typeTF, priceTF;


    @FXML
    private JFXButton saveButton, cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;


    private Boolean isInEditMode = Boolean.FALSE;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private boolean makeProduct(){

        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

//    public void inflateUI(Product product) {
//        myProduct=product;
//        nameTF.setText(product.getName());
//        typeTF.setText(product.getType());
//        priceTF.setText(""+product.getPrice());
//        isInEditMode = Boolean.TRUE;
//    }

    private void clearEntries() {
        nameTF.clear();
        typeTF.clear();
        priceTF.clear();
    }

    private void handleEditOperation() {
//        if(!makeProduct())return;
//        Product savedProduct = productRepository.save(myProduct);
//        callBack.callBack(savedProduct);
    }
}
