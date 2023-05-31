package ru.tigran.zakaz.controllers.warehouse;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.WarehouseService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

@Controller
public class EditController implements Initializable {
    private WarehouseModel warehouse;
    private final WarehouseService warehouseService;

    @FXML
    private TextArea countTA;

    @FXML
    private TextArea descriptionTA;

    @FXML
    private TextArea nameTA;

    @FXML
    private TextArea purchasingPriceTA;

    @FXML
    private TextArea retailPriceTA;

    @FXML
    private ImageView imgIV;

    @Autowired
    public EditController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void selectIMGBtnClicked(MouseEvent mouseEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор изображения");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения","*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            byte[] img = Files.readAllBytes(file.toPath());
            warehouse.setImage(img);
            imgIV.setImage(new Image(new ByteArrayInputStream(img)));
        }
    }

    public void cancelBtnClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) nameTA.getScene().getWindow();
        stage.close();
    }

    public void saveBtnClicked(MouseEvent mouseEvent) {
        try {
            ProductModel product = warehouse.getProduct();
            product.setName(nameTA.getText());
            product.setDescription(descriptionTA.getText());
            warehouse.setCount(Integer.parseInt(countTA.getText()));
            warehouse.setRetailPrice(Double.parseDouble(retailPriceTA.getText()));
            warehouse.setPurchasingPrice(Double.parseDouble(purchasingPriceTA.getText()));
            warehouseService.saveWarehouses(warehouse);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Приложение Regalo");
            alert.setHeaderText(null);
            alert.setContentText("Товар был успешно изменен!");
            alert.showAndWait();

            Stage stage = (Stage) nameTA.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка преобразования типов");
            alert.setHeaderText(null);
            alert.setContentText("Проверьте все поля на корректность данных!");
            alert.showAndWait();
        }
    }

    public void setWarehouseModel(WarehouseModel warehouse) {
        this.warehouse = warehouse;
        ProductModel product = warehouse.getProduct();
        nameTA.setText(product.getName());
        descriptionTA.setText(product.getDescription());
        countTA.setText(String.valueOf(warehouse.getCount()));
        retailPriceTA.setText(String.valueOf(warehouse.getRetailPrice()));
        purchasingPriceTA.setText(String.valueOf(warehouse.getPurchasingPrice()));

        InputStream inputStream = new ByteArrayInputStream(warehouse.getImage());
        imgIV.setImage(new Image(inputStream));
    }

}
