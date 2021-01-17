package com.elkattanman.farmFxml.controllers.treatment;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.treatment.TreatmentAddController;
import com.elkattanman.farmFxml.domain.Treatment;
import com.elkattanman.farmFxml.repositories.TreatmentRepository;
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
@FxmlView("/FXML/treatment/treatment.fxml")
public class TreatmentController implements Initializable, CallBack<Boolean, Treatment> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Treatment> table;

    @FXML
    private TableColumn<Treatment, Integer> idCol;

    @FXML
    private TableColumn<Treatment, String> typeCol, nameCol;

    @FXML
    private TableColumn<Treatment, LocalDate> dateCol;

    @FXML
    private TableColumn<Treatment, Double> priceCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Treatment> list = FXCollections.observableArrayList();
    private final TreatmentRepository treatmentRepository;

    public TreatmentController(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(treatmentRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Treatment> filteredData = new FilteredList<>(list, s -> true);
        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filteredData.setPredicate(treatment->{

                            if(newValue == null || newValue.isEmpty() ){
                                return true ;
                            }
                            String lowerCaseFilter = newValue.toLowerCase() ;
                            if(treatment.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                                return true ;
                            }
                            return false ;
                        }));

        SortedList<Treatment> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Treatment selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<TreatmentAddController, Parent> load = fxWeaver.load(TreatmentAddController.class);
        TreatmentAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Treatment selectedItem = table.getSelectionModel().getSelectedItem();
        treatmentRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(treatmentRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<TreatmentAddController, Parent> load = fxWeaver.load(TreatmentAddController.class);
        TreatmentAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Treatment());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Treatment obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Treatment object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}