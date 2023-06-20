package ru.tigran.zakaz.controllers.warehouse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.ProductsService;
import ru.tigran.zakaz.services.WarehouseService;
import ru.tigran.zakaz.utils.Alerts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

@Controller
public class AddController implements Initializable {
    private final ProductsService productsService;
    private final WarehouseService warehouseService;

    @FXML
    private ImageView addImageIV;

    @FXML
    private TextArea countTA;

    @FXML
    private ComboBox<ProductModel> productsCB;

    @FXML
    private TextArea purchasingPriceTA;

    @FXML
    private TextArea retailPriceTA;

    private ObservableList<WarehouseModel> allWarehouse;
    private ObservableList<ProductModel> allProducts;

    private Image emptyImg;
    private byte[] productImage;

    @Autowired
    public AddController(ProductsService productsService, WarehouseService warehouseService) {
        this.productsService = productsService;
        this.warehouseService = warehouseService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emptyImg = addImageIV.getImage();
        allProducts = FXCollections.observableList(productsService.getAllProductsWithoutWarehouse());

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

    public void addProductBtnClick(MouseEvent mouseEvent) throws IOException {
        if (productsCB.getSelectionModel().getSelectedItem() != null &&
                !countTA.getText().equals("") && !purchasingPriceTA.getText().equals("") && !retailPriceTA.getText().equals("")
                && productImage != null) {
            try {
                WarehouseModel warehouse = new WarehouseModel();
                warehouse.setProduct(productsCB.getSelectionModel().getSelectedItem());
                warehouse.setCount(Integer.parseInt(countTA.getText()));
                warehouse.setPurchasingPrice(Double.parseDouble(purchasingPriceTA.getText()));
                warehouse.setRetailPrice(Double.parseDouble(retailPriceTA.getText()));
                warehouse.setImage(productImage);

                warehouseService.saveWarehouses(warehouse);
                allWarehouse.add(warehouse);
                allProducts.remove(productsCB.getSelectionModel().getSelectedItem());
                clearForm();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Товар успешно добавлен!", ButtonType.OK);
                alert.setTitle("Сохранение");
                alert.setHeaderText(null);
                alert.showAndWait();
            } catch (NumberFormatException e) {
                Alerts.alertErrorTypes();
            }


        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Заполните все поля!", ButtonType.OK);
            error.setTitle("Ошибка сохранения");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }

    public void selectAddImageBTN(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор изображения");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения","*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            byte[] img = Files.readAllBytes(file.toPath());
            addImageIV.setImage(new Image(new ByteArrayInputStream(img)));
            productImage = img;
        }
    }


    public void cancelBtnClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) purchasingPriceTA.getScene().getWindow();
        stage.close();
    }

    private void clearForm() {
        countTA.setText("");
        purchasingPriceTA.setText("");
        retailPriceTA.setText("");
        addImageIV.setImage(emptyImg);
        productsCB.setValue(null);
    }

    public void setAllWarehouse(ObservableList<WarehouseModel> allWarehouse) {
        this.allWarehouse = allWarehouse;
    }
}
