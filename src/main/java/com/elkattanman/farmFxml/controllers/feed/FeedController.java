package com.elkattanman.farmFxml.controllers.feed;

import com.elkattanman.farmFxml.callback.CallBack;
import com.elkattanman.farmFxml.domain.Capital;
import com.elkattanman.farmFxml.domain.Feed;
import com.elkattanman.farmFxml.repositories.CapitalRepository;
import com.elkattanman.farmFxml.repositories.FeedRepository;
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
@FxmlView("/FXML/feed/feeds.fxml")
public class FeedController implements Initializable, CallBack<Boolean, Feed> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Feed> table;

    @FXML
    private TableColumn<Feed, Integer> idCol;

    @FXML
    private TableColumn<Feed, String> typeCol, nameCol;

    @FXML
    private TableColumn<Feed, LocalDate> dateCol;

    @FXML
    private TableColumn<Feed, Integer> numCol;

    @FXML
    private TableColumn<Feed, Double> priceCol;

    @FXML
    private JFXTextField searchTF;

    @FXML
    private Text infoTXT;

    private ObservableList<Feed> list = FXCollections.observableArrayList();
    private final FeedRepository feedRepository;
    private final CapitalRepository capitalRepository;

    public FeedController(FeedRepository feedRepository, CapitalRepository capitalRepository) {
        this.feedRepository = feedRepository;
        this.capitalRepository = capitalRepository;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(feedRepository.findAll());
        table.setItems(list);
        double total=0.0;
        total = list.stream().mapToDouble(Feed::getCost).sum();
        infoTXT.setText("العدد الكلى = "+ list.size() +" والتكلفه الكليه = "+ total);
        MakeMyFilter();
    }

    private void MakeMyFilter(){
        FilteredList<Feed> filteredData = new FilteredList<>(list, s -> true);
        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    filteredData.setPredicate(feed -> {

                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (feed.getType().getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                            return true;
                        }
                        return false;
                    });
                    double total=0.0;
                    total = filteredData.stream().mapToDouble(Feed::getCost).sum();
                    infoTXT.setText("العدد الكلى = "+ filteredData.size() +" والتكلفه الكليه = "+ total);
                });

        SortedList<Feed> sorted = new SortedList<>(filteredData) ;
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);
    }


    @FXML
    void edit(ActionEvent event) {
        Feed selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<FeedAddController, Parent> load = fxWeaver.load(FeedAddController.class);
        FeedAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.setInEditMode(true);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Feed selectedItem = table.getSelectionModel().getSelectedItem();
        feedRepository.delete(selectedItem);
        list.remove(selectedItem) ;
        Capital capital = capitalRepository.findById(1).get();
        capital.setFeed(capital.getFeed()-selectedItem.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()+selectedItem.getCost());
        capitalRepository.save(capital);
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(feedRepository.findAll());
    }


    public void add(ActionEvent actionEvent) {
        FxControllerAndView<FeedAddController, Parent> load = fxWeaver.load(FeedAddController.class);
        FeedAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Feed());
        controller.setInEditMode(false);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @Override
    public Boolean callBack(Feed obj) {
        Capital capital = capitalRepository.findById(1).get();
        for (int i=0 ; i < list.size(); ++i) {
            Feed object= list.get(i);
            if (object.getId().equals(obj.getId())) {
                capital.setFeed(capital.getFeed()-object.getCost()+obj.getCost());
                capital.setCurrentTotal(capital.getCurrentTotal()+object.getCost()-obj.getCost());
                capitalRepository.save(capital);
                list.set(i, obj);
                return true;
            }
        }
        capital.setFeed(capital.getFeed()+obj.getCost());
        capital.setCurrentTotal(capital.getCurrentTotal()-obj.getCost());
        capitalRepository.save(capital);
        list.add(obj);
        return true;
    }
}