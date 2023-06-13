package ru.tigran.zakaz.controllers.sales;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.SaleModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.ProductsService;
import ru.tigran.zakaz.services.SalesService;
import ru.tigran.zakaz.services.WarehouseService;

import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.util.ResourceBundle;

@Controller
public class SaleProductController implements Initializable {
    private final SalesService salesService;
    private final ProductsService productsService;
    private final WarehouseService warehouseService;

    @FXML
    private TextArea countTA;

    @FXML
    private TextArea priceTA;

    @FXML
    private ComboBox<ProductModel> productsCB;

    private ObservableList<SaleModel> allSales;
    private ObservableList<ProductModel> allProducts;

    @Autowired
    public SaleProductController(SalesService salesService, ProductsService productsService, WarehouseService warehouseService) {
        this.salesService = salesService;
        this.productsService = productsService;
        this.warehouseService = warehouseService;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         allProducts = FXCollections.observableList(productsService.getAllProductsInStock());

        Callback<ListView<ProductModel>, ListCell<ProductModel>> cellFactoryCB = new Callback<>() {
            @Override
            public ListCell<ProductModel> call(ListView<ProductModel> productModelListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProductModel productModel, boolean b) {
                        super.updateItem(productModel, b);
                        if (b) {
                            setGraphic(null);
                        } else {
                            setText(productModel.getName());
                        }
                    }
                };
            }
        };

        productsCB.setButtonCell(cellFactoryCB.call(null));
        productsCB.setCellFactory(cellFactoryCB);
        productsCB.setItems(allProducts);
    }

    public void setAllSales(ObservableList<SaleModel> allSales) {
        this.allSales = allSales;
    }

    public void cancelBtnClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) countTA.getScene().getWindow();
        stage.close();
    }

    public void saleProductBtnClicked(MouseEvent mouseEvent) {
        try {
            if (countTA.getText().strip().equals("")|| priceTA.getText().strip().equals("") || productsCB.getSelectionModel().getSelectedItem() == null) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Заполните все поля!", ButtonType.OK);
                error.setTitle("Ошибка сохранения");
                error.setHeaderText(null);
                error.showAndWait();
                return;
            }

            SaleModel sale = new SaleModel();
            ProductModel product = productsCB.getSelectionModel().getSelectedItem();
            WarehouseModel warehouse = product.getWarehouse();

            int count = Integer.parseInt(countTA.getText());
            if (count > warehouse.getCount()) {
                Alert error = new Alert(Alert.AlertType.ERROR, "На складе нет такого количество товара!", ButtonType.OK);
                error.setTitle("Ошибка сохранения");
                error.setHeaderText(null);
                error.showAndWait();
                return;
            } else if (count < 1) {
                Alert error = new Alert(Alert.AlertType.ERROR, "Количество не может быть меньше 1!", ButtonType.OK);
                error.setTitle("Ошибка сохранения");
                error.setHeaderText(null);
                error.showAndWait();
                return;
            }

            sale.setCount(count);
            warehouse.setCount(warehouse.getCount() - sale.getCount());

            sale.setDate(Date.from(Instant.now()));
            sale.setPrice(Double.parseDouble(priceTA.getText()));
            sale.setWarehouse(warehouse);

            warehouseService.saveWarehouses(warehouse);
            salesService.saveSale(sale);

            allSales.add(sale);

            if (warehouse.getCount() == 0) {
                allProducts.remove(product);
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Товар успешно продан!", ButtonType.OK);
            alert.setTitle("Сохранение");
            alert.setHeaderText(null);
            alert.showAndWait();

            clearForm();
        } catch (NumberFormatException | NullPointerException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Неверные типы данных!", ButtonType.OK);
            error.setTitle("Ошибка сохранения");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    private void clearForm() {
        countTA.setText("");
        priceTA.setText("");
        productsCB.setValue(null);
    }
}
