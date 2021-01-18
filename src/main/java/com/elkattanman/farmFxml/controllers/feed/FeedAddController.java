package com.elkattanman.farmFxml.controllers.feed;


import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Feed;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.repositories.FeedRepository;
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
@FxmlView("/FXML/feed/add_feed.fxml")
public class FeedAddController implements Initializable {

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
    private Feed myFeed;

    private final TypeRepository typeRepository;
    private final FeedRepository feedRepository;
    private final CapitalRepository capitalRepository;

    private ObservableList<Type> typeList = FXCollections.observableArrayList();

    public FeedAddController(TypeRepository typeRepository, FeedRepository feedRepository, CapitalRepository capitalRepository) {
        this.typeRepository = typeRepository;
        this.feedRepository = feedRepository;
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
        typeList.setAll(typeRepository.findAllByFeedTrue());
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

    private boolean makeFeed(){
        String name=StringUtils.trimToEmpty(nameTF.getText());
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

        myFeed= Feed.builder()
                .id(myFeed.getId())
                .type(selectedItem)
                .date(date)
                .cost(price)
                .number(num)
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
        if (!makeFeed())return;
        Capital capital = capitalRepository.findById(1).get();
        if(capital.getCurrentTotal()< myFeed.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }
        Feed save = feedRepository.save(myFeed);
        callBack.callBack(save);
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Feed feed) {
        myFeed=feed;
        typeCB.getSelectionModel().select(feed.getType());
        dateDP.setValue(feed.getDate());
        numTF.setText(""+feed.getNumber());
        priceTF.setText(""+feed.getCost());
        nameTF.setText(""+feed.getName());
    }

    private void clearEntries() {
        inflateUI(new Feed());
    }

    private void handleEditOperation() {
        double oldCost=myFeed.getCost();
        if(!makeFeed())return;
        Capital capital = capitalRepository.findById(1).get();
        if(capital.getCurrentTotal()+oldCost < myFeed.getCost()){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Faild operation", "لا يوجد راس مالى كافى. لديك "+ capital.getCurrentTotal());
            return;
        }
        Feed save = feedRepository.save(myFeed);
        callBack.callBack(save);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه التعديل");
    }
}
