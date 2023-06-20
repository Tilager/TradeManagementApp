package ru.tigran.zakaz.controllers.warehouse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.WarehouseService;
import ru.tigran.zakaz.utils.WindowsUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class WarehouseController implements Initializable {
    @FXML
    private TableColumn<WarehouseModel, Integer> countTC;

    @FXML
    private TableColumn<WarehouseModel, ProductModel> descriptionTC;

    @FXML
    private TableColumn<WarehouseModel, byte[]> imageTC;

    @FXML
    private TableColumn<WarehouseModel, ProductModel> nameTC;

    @FXML
    private TableColumn<WarehouseModel, Double> purchasingPriceTC;

    @FXML
    private TableColumn<WarehouseModel, Double> retailPrice;

    @FXML
    private TableView<WarehouseModel> warehouseTV;

    private final WarehouseService warehouseService;

    private ObservableList<WarehouseModel> allWarehouse;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allWarehouse = FXCollections.observableList(warehouseService.getAllWarehouses());
        warehouseTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        imageTC.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageTC.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WarehouseModel, byte[]> call(TableColumn<WarehouseModel, byte[]> warehouseModelImageTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(byte[] image, boolean b) {
                        super.updateItem(image, b);

                        if (b || image == null) {
                            setGraphic(null);
                        } else {
                            ImageView imageView = new ImageView();
                            imageView.setFitHeight(100);
                            imageView.setFitWidth(100);
                            imageView.setImage(new Image(new ByteArrayInputStream(image)));

                            setGraphic(imageView);
                        }
                    }
                };
            }
        });

        nameTC.setCellValueFactory(new PropertyValueFactory<>("product"));
        nameTC.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WarehouseModel, ProductModel> call(TableColumn<WarehouseModel, ProductModel> warehouseModelStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(ProductModel s, boolean b) {
                    super.updateItem(s, b);

                    if (b || s == null)
                        setGraphic(null);
                    else
                        setGraphic(new Label(s.getName()));
                    }
                };
            }
        });

        descriptionTC.setCellValueFactory(new PropertyValueFactory<>("product"));
        descriptionTC.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WarehouseModel, ProductModel> call(TableColumn<WarehouseModel, ProductModel> warehouseModelStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(ProductModel s, boolean b) {
                        super.updateItem(s, b);

                        if (b || s == null)
                            setGraphic(null);
                        else
                            setGraphic(new Label(s.getDescription()));
                    }
                };
            }
        });

        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        purchasingPriceTC.setCellValueFactory(new PropertyValueFactory<>("purchasingPrice"));
        retailPrice.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        warehouseTV.setItems(allWarehouse);
    }

    @SneakyThrows
    public void leaveBtnClick(MouseEvent mouseEvent) {
        WindowsUtils.openNewWindow("main-view.fxml", mouseEvent);
    }

    public void tvMouseClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2 && warehouseTV.getSelectionModel().getSelectedItems().size() == 1) {
            FXMLLoader fxmlLoader = WindowsUtils.getFxmlLoaderWithSpringContext("warehouse/edit-view.fxml");

            Stage dialogStage = WindowsUtils.openModalWindowWithController(mouseEvent, fxmlLoader);
            EditController editController = fxmlLoader.getController();
            editController.setWarehouseModel(warehouseTV.getSelectionModel().getSelectedItem());
            dialogStage.showAndWait();
            warehouseTV.refresh();
        }
    }

    public void addProductBTNClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = WindowsUtils.getFxmlLoaderWithSpringContext("warehouse/add-view.fxml");

        Stage dialogStage = WindowsUtils.openModalWindowWithController(mouseEvent, fxmlLoader);

        AddController addController = fxmlLoader.getController();
        addController.setAllWarehouse(allWarehouse);

        dialogStage.showAndWait();
        warehouseTV.refresh();
    }

    public void onKeyPressedTV(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE) && warehouseTV.getSelectionModel().getSelectedItems().size() > 0) {
            List<WarehouseModel> removedProducts = warehouseTV.getSelectionModel().getSelectedItems();

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить " + removedProducts.size() + " товаров со склада?", ButtonType.OK, ButtonType.CANCEL);
            conf.setHeaderText(null);
            conf.setTitle("Подтверждения удаления");
            conf.showAndWait();

            if (conf.getResult() == ButtonType.OK) {
                warehouseService.removeByListObjects(removedProducts);
                allWarehouse.removeAll(removedProducts);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setContentText("Товары успешно удалены!");
                info.setTitle("Информация о товаре");
                info.showAndWait();
            }

        }
    }
}
