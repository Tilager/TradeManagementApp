package ru.tigran.zakaz.controllers.sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.SaleModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.SalesService;
import ru.tigran.zakaz.services.WarehouseService;
import ru.tigran.zakaz.utils.WindowsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class SaleController implements Initializable {
    @FXML
    private TableColumn<SaleModel, Void> actionTC;

    @FXML
    private TableColumn<SaleModel, Integer> countTC;

    @FXML
    private TableColumn<SaleModel, Date> dateTC;

    @FXML
    private TableColumn<SaleModel, WarehouseModel> nameTC;

    @FXML
    private TableColumn<SaleModel, Double> priceTC;

    @FXML
    private TableView<SaleModel> salesTV;

    private ObservableList<SaleModel> allSales;
    private final SalesService salesService;
    private final WarehouseService warehouseService;

    @Autowired
    public SaleController(SalesService salesService, WarehouseService warehouseService) {
        this.salesService = salesService;
        this.warehouseService = warehouseService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allSales = FXCollections.observableList(salesService.getAllSales());
        salesTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameTC.setCellValueFactory(new PropertyValueFactory<>("warehouse"));
        nameTC.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SaleModel, WarehouseModel> call(TableColumn<SaleModel, WarehouseModel> saleModelWarehouseModelTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(WarehouseModel warehouseModel, boolean b) {
                        super.updateItem(warehouseModel, b);

                        if (b || warehouseModel == null)
                            setGraphic(null);
                        else
                            setGraphic(new Label(warehouseModel.getProduct().getName()));
                    }
                };
            }
        });

        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        priceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateTC.setCellValueFactory(new PropertyValueFactory<>("date"));
        actionTC.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SaleModel, Void> call(TableColumn<SaleModel, Void> saleModelVoidTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Void unused, boolean b) {
                        super.updateItem(unused, b);
                        if (b)
                            setGraphic(null);
                        else {
                            Button refundBtn = new Button();
                            refundBtn.setText("Возврат");
                            refundBtn.setOnMouseClicked(mouseEvent -> {
                                Button btn = (Button) mouseEvent.getSource();
                                TableRow row = (TableRow) btn.getParent().getParent().getParent();
                                SaleModel sale = (SaleModel) row.getItem();

                                try {
                                    FXMLLoader fxml = WindowsUtils.getFxmlLoaderWithSpringContext("sales/sale-refund-view.fxml");
                                    Stage stage = WindowsUtils.openModalWindowWithController(mouseEvent, fxml);

                                    SaleRefundController controller = fxml.getController();
                                    controller.setRefundSale(sale);
                                    controller.setAllSales(allSales);
                                    stage.showAndWait();

                                    salesTV.refresh();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                            });

                            HBox hBox = new HBox(refundBtn);
                            hBox.setFillHeight(true);
                            hBox.setAlignment(Pos.CENTER);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });

        salesTV.setItems(allSales);
    }

    public void saleProductBTNClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = WindowsUtils.getFxmlLoaderWithSpringContext("sales/sale-product-view.fxml");

        Stage stage = WindowsUtils.openModalWindowWithController(mouseEvent, fxmlLoader);

        SaleProductController saleProductController = fxmlLoader.getController();
        saleProductController.setAllSales(allSales);

        stage.showAndWait();
    }


    public void leaveBtnClick(MouseEvent mouseEvent) {
        WindowsUtils.openNewWindow("main-view.fxml", mouseEvent);
    }

    public void onKeyPressedTV(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE) && salesTV.getSelectionModel().getSelectedItems().size() > 0) {
            List<SaleModel> removedSales = salesTV.getSelectionModel().getSelectedItems();

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить " + removedSales.size() + " продаж?", ButtonType.OK, ButtonType.CANCEL);
            conf.setHeaderText(null);
            conf.setTitle("Подтверждения удаления");
            conf.showAndWait();

            if (conf.getResult() == ButtonType.OK) {
                for (SaleModel sale: removedSales) {
                    WarehouseModel warehouse = sale.getWarehouse();
                    warehouse.setCount(warehouse.getCount() + sale.getCount());
                    warehouseService.saveWarehouses(warehouse);
                }

                salesService.removeByListObjects(removedSales);
                allSales.removeAll(removedSales);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setContentText("Продажи успешно удалены!");
                info.setTitle("Информация о продажах");
                info.showAndWait();
            }

        }
    }
}
