package com.elkattanman.farmFxml.controllers.death;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.sale.SaleAddController;
import com.elkattanman.farmFxml.domain.Death;
import com.elkattanman.farmFxml.domain.Sale;
import com.elkattanman.farmFxml.repositories.DeathRepository;
import com.elkattanman.farmFxml.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/death/deaths.fxml")
public class DeathController implements Initializable, CallBack<Boolean, Death> {
    @Autowired private FxWeaver fxWeaver;
    
    @FXML
    private TableView<Death> table;

    @FXML
    private TableColumn<Death, Integer> idCol;

    @FXML
    private TableColumn<Death, String> typeCol;

    @FXML
    private TableColumn<Death, LocalDate> dateCol;

    @FXML
    private TableColumn<Death, Integer> numCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Death> list = FXCollections.observableArrayList();
    private final DeathRepository deathRepository;

    public DeathController(DeathRepository deathRepository) {
        this.deathRepository = deathRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(deathRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Death> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filteredData.setPredicate(death->{

                            if(newValue == null || newValue.isEmpty() ){
                                return true ;
                            }
                            String lowerCaseFilter = newValue.toLowerCase() ;
                            if(death.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                                return true ;
                            }
                            return false ;
                        }));

        SortedList<Death> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Death selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<DeathAddController, Parent> load = fxWeaver.load(DeathAddController.class);
        DeathAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Death selectedItem = table.getSelectionModel().getSelectedItem();
        deathRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(deathRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<DeathAddController, Parent> load = fxWeaver.load(DeathAddController.class);
        DeathAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Death());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Death obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Death object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}