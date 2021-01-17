package com.elkattanman.farmFxml.controllers.spending;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.spending.SpendingAddController;
import com.elkattanman.farmFxml.domain.Spending;
import com.elkattanman.farmFxml.repositories.SpendingRepository;
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
@FxmlView("/FXML/spending/spending.fxml")
public class SpendingController implements Initializable, CallBack<Boolean, Spending> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Spending> table;

    @FXML
    private TableColumn<Spending, Integer> idCol;

    @FXML
    private TableColumn<Spending, String> typeCol, nameCol;

    @FXML
    private TableColumn<Spending, LocalDate> dateCol;

    @FXML
    private TableColumn<Spending, Integer> numCol;

    @FXML
    private TableColumn<Spending, Double> priceCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Spending> list = FXCollections.observableArrayList();
    private final SpendingRepository spendingRepository;

    public SpendingController(SpendingRepository spendingRepository) {
        this.spendingRepository = spendingRepository;
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
        list.setAll(spendingRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Spending> filteredData = new FilteredList<>(list, s -> true);
        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filteredData.setPredicate(spending->{

                            if(newValue == null || newValue.isEmpty() ){
                                return true ;
                            }
                            String lowerCaseFilter = newValue.toLowerCase() ;
                            if(spending.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                                return true ;
                            }
                            return false ;
                        }));

        SortedList<Spending> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Spending selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<SpendingAddController, Parent> load = fxWeaver.load(SpendingAddController.class);
        SpendingAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Spending selectedItem = table.getSelectionModel().getSelectedItem();
        spendingRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(spendingRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<SpendingAddController, Parent> load = fxWeaver.load(SpendingAddController.class);
        SpendingAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Spending());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Spending obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Spending object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}