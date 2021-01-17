package com.elkattanman.farmFxml.controllers.types;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.controllers.borns.BornAddController;
import com.elkattanman.farmFxml.domain.Born;
import com.elkattanman.farmFxml.domain.Type;
import com.elkattanman.farmFxml.repositories.TypeRepository;
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
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/types/types.fxml")
public class TypesController implements Initializable, CallBack<Boolean, Type> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Type> table;

    @FXML
    private TableColumn<Type, Integer> idCol;

    @FXML
    private TableColumn<Type, String> nameCol;

    @FXML
    private JFXTextField searchTF;

    private ObservableList<Type> list = FXCollections.observableArrayList();

    private final TypeRepository typeRepository;

    public TypesController(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(typeRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }



    private void MakeMyFilter(){
        FilteredList<Type> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener( (observable, oldValue, newValue) -> {
            filteredData.setPredicate(type->{

                if(newValue == null || newValue.isEmpty() ){
                    return true ;
                }
                String lowerCaseFilter = newValue.toLowerCase() ;
                if(type.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true ;
                }
                return false ;
            });

        });

        SortedList<Type> sortedBorns = new SortedList<>(filteredData) ;
        sortedBorns.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedBorns);
    }


    @FXML
    void edit(ActionEvent event) {
        Type selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<TypeAddController, Parent> load = fxWeaver.load(TypeAddController.class);
        TypeAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Type selectedItem = table.getSelectionModel().getSelectedItem();
        typeRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(typeRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<TypeAddController, Parent> load = fxWeaver.load(TypeAddController.class);
        TypeAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Type());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Type obj) {
        for (int i=0 ; i < list.size(); ++i) {
            Type object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                list.set(i, obj);
                return true;
            }
        }
        list.add(obj) ;
        return true;
    }
}