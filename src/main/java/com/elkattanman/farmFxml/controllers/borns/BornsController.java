package com.elkattanman.farmFxml.controllers.borns;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Born;
import com.elkattanman.farmFxml.domain.Sale;
import com.elkattanman.farmFxml.repositories.BornRepository;
import com.elkattanman.farmFxml.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
@FxmlView("/FXML/borns/borns.fxml")
public class BornsController implements Initializable, CallBack<Boolean, Born> {
    @Autowired
    private FxWeaver fxWeaver;


    @FXML
    private TableView<Born> table;

    @FXML
    private TableColumn<Born, Integer> idCol;

    @FXML
    private TableColumn<Born, String> typeCol;

    @FXML
    private TableColumn<Born, LocalDate> dateCol;

    @FXML
    private TableColumn<Born, Integer> numCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Born> list = FXCollections.observableArrayList();

    private final BornRepository bornRepository;

    public BornsController(BornRepository bornRepository) {
        this.bornRepository = bornRepository;
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
        list.setAll(bornRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Born> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filteredData.setPredicate(born->{

                            if(newValue == null || newValue.isEmpty() ){
                                return true ;
                            }
                            String lowerCaseFilter = newValue.toLowerCase() ;
                            if(born.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                                return true ;
                            }
                            return false ;
                        }));

        SortedList<Born> sortedBorns = new SortedList<>(filteredData) ;
        sortedBorns.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedBorns);
    }


    @FXML
    void edit(ActionEvent event) {
        Born selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<BornAddController, Parent> load = fxWeaver.load(BornAddController.class);
        BornAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Born selectedItem = table.getSelectionModel().getSelectedItem();
        bornRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(bornRepository.findAll());
    }

    public void add(ActionEvent actionEvent) {
        FxControllerAndView<BornAddController, Parent> load = fxWeaver.load(BornAddController.class);
        BornAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Born());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());    }

    @Override
    public Boolean callBack(Born obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Born object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}