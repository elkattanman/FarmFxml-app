package com.elkattanman.farmFxml.controllers.reserve;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.reserve.ReserveAddController;
import com.elkattanman.farmFxml.domain.Reserve;
import com.elkattanman.farmFxml.repositories.ReserveRepository;
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
@FxmlView("/FXML/reserve/reserves.fxml")
public class ReserveController implements Initializable, CallBack<Boolean, Reserve> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Reserve> table;

    @FXML
    private TableColumn<Reserve, Integer> idCol;

    @FXML
    private TableColumn<Reserve, String> typeCol, nameCol;

    @FXML
    private TableColumn<Reserve, LocalDate> dateCol;

    @FXML
    private TableColumn<Reserve, Integer> numCol;

    @FXML
    private TableColumn<Reserve, Double> priceCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Reserve> list = FXCollections.observableArrayList();
    private final ReserveRepository reserveRepository;

    public ReserveController(ReserveRepository reserveRepository) {
        this.reserveRepository = reserveRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(reserveRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Reserve> filteredData = new FilteredList<>(list, s -> true);
        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filteredData.setPredicate(reserve->{

                            if(newValue == null || newValue.isEmpty() ){
                                return true ;
                            }
                            String lowerCaseFilter = newValue.toLowerCase() ;
                            if(reserve.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                                return true ;
                            }
                            return false ;
                        }));

        SortedList<Reserve> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Reserve selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<ReserveAddController, Parent> load = fxWeaver.load(ReserveAddController.class);
        ReserveAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Reserve selectedItem = table.getSelectionModel().getSelectedItem();
        reserveRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(reserveRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<ReserveAddController, Parent> load = fxWeaver.load(ReserveAddController.class);
        ReserveAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Reserve());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Reserve obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Reserve object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}