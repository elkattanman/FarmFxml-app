package com.elkattanman.farmFxml.controllers.treatment;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Treatment;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
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
import javafx.scene.text.Text;
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
    @FXML
    private Text infoTXT;

    private ObservableList<Treatment> list = FXCollections.observableArrayList();
    private final TreatmentRepository treatmentRepository;
    private final CapitalRepository capitalRepository;

    public TreatmentController(TreatmentRepository treatmentRepository, CapitalRepository capitalRepository) {
        this.treatmentRepository = treatmentRepository;
        this.capitalRepository = capitalRepository;
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
        double total=0.0;
        total = list.stream().mapToDouble(Treatment::getCost).sum();
        infoTXT.setText("العدد الكلى = "+ list.size() +" والتكلفه الكليه = "+ total);
    }



    private void MakeMyFilter(){
        FilteredList<Treatment> filteredData = new FilteredList<>(list, s -> true);
        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredData.setPredicate(treatment -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (treatment.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        }
                        return false;
                    });
                    double total=0.0;
                    total = filteredData.stream().mapToDouble(Treatment::getCost).sum();
                    infoTXT.setText("العدد الكلى = "+ filteredData.size() +" والتكلفه الكليه = "+ total);
                });

        SortedList<Treatment> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        double total=0.0;
        total = sorted.stream().mapToDouble(Treatment::getCost).sum();
        infoTXT.setText("العدد الكلى = "+ sorted.size() +" والتكلفه الكليه = "+ total);
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
        Capital capital = capitalRepository.findById(1).get();
        capital.setTreatment(capital.getTreatment()-selectedItem.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()+selectedItem.getCost());
        capitalRepository.save(capital);
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
        Capital capital = capitalRepository.findById(1).get();
        for (int i=0 ; i < list.size(); ++i) {
            Treatment object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                capital.setTreatment(capital.getTreatment()-object.getCost()+obj.getCost());
                capital.setCurrentTotal(capital.getCurrentTotal()+object.getCost()-obj.getCost());
                capitalRepository.save(capital);
                list.set(i, obj);
                return true;
            }
        }
        capital.setTreatment(capital.getTreatment()+obj.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()-obj.getCost());
        capitalRepository.save(capital);
        list.add(obj);
        return true;
    }
}