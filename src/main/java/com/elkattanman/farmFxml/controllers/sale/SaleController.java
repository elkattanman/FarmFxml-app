package com.elkattanman.farmFxml.controllers.sale;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Sale;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.repositories.SaleRepository;
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
@FxmlView("/FXML/sale/sale.fxml")
public class SaleController implements Initializable, CallBack<Boolean, Sale> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Sale> table;

    @FXML
    private TableColumn<Sale, Integer> idCol;

    @FXML
    private TableColumn<Sale, String> typeCol;

    @FXML
    private TableColumn<Sale, LocalDate> dateCol;

    @FXML
    private TableColumn<Sale, Integer> numCol;

    @FXML
    private TableColumn<Sale, Double> priceCol;

    @FXML
    private JFXTextField searchTF;
    @FXML
    private Text infoTXT;

    private ObservableList<Sale> list = FXCollections.observableArrayList();
    private final SaleRepository saleRepository;
    private final CapitalRepository capitalRepository;


    public SaleController(SaleRepository saleRepository, CapitalRepository capitalRepository) {
        this.saleRepository = saleRepository;
        this.capitalRepository = capitalRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(saleRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
        double total=0.0;
        total = list.stream().mapToDouble(Sale::getCost).sum();
        infoTXT.setText("العدد الكلى = "+ list.size() +" والتكلفه الكليه = "+ total);
    }



    private void MakeMyFilter(){
        FilteredList<Sale> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredData.setPredicate(sale -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (sale.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        }
                        return false;
                    });
                    double total=0.0;
                    total = filteredData.stream().mapToDouble(Sale::getCost).sum();
                    infoTXT.setText("العدد الكلى = "+ filteredData.size() +" والتكلفه الكليه = "+ total);
                });

        SortedList<Sale> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        double total=0.0;
        total = sorted.stream().mapToDouble(Sale::getCost).sum();
        infoTXT.setText("العدد الكلى = "+ sorted.size() +" والتكلفه الكليه = "+ total);
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Sale selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<SaleAddController, Parent> load = fxWeaver.load(SaleAddController.class);
        SaleAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Sale selectedItem = table.getSelectionModel().getSelectedItem();
        saleRepository.delete(selectedItem);
        list.remove(selectedItem) ;
        Capital capital = capitalRepository.findById(1).get();
        capital.setSales(capital.getSales()-selectedItem.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()-selectedItem.getCost());
        capitalRepository.save(capital);
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(saleRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<SaleAddController, Parent> load = fxWeaver.load(SaleAddController.class);
        SaleAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Sale());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Sale obj) {
        Capital capital = capitalRepository.findById(1).get();
        for (int i=0 ; i < list.size(); ++i) {
            Sale object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                capital.setSales(capital.getSales()-object.getCost()+obj.getCost());
                capital.setCurrentTotal(capital.getCurrentTotal()-object.getCost()+obj.getCost());
                capitalRepository.save(capital);
                list.set(i, obj);
                return true;
            }
        }
        capital.setSales(capital.getSales()+obj.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()+obj.getCost());
        capitalRepository.save(capital);
        list.add(obj) ;
        return true;
    }
}