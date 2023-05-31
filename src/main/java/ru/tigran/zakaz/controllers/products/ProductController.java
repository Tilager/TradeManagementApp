package ru.tigran.zakaz.controllers.products;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.services.ProductsService;
import ru.tigran.zakaz.utils.WindowsUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ProductController implements Initializable {
    @FXML
    private Button menuAddProductBtn;

    @FXML
    private Button menuAllProductsBtn;

    @FXML
    private TableColumn<ProductModel, String> descriptionTC;

    @FXML
    private TableColumn<ProductModel, String> nameTC;

    @FXML
    private TableView<ProductModel> productsTV;

    @FXML
    private TableColumn<ProductModel, String> providerTC;

    @FXML
    private GridPane addProductGP;
    @FXML
    private GridPane allProductsGP;

    @FXML
    private TextArea providerInputTA;

    @FXML
    private TextArea nameInputTA;

    @FXML
    private TextArea descriptionInputTA;

    private ObservableList<ProductModel> products;

    private final ProductsService productsService;

    @Autowired
    public ProductController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // setting table
        products = FXCollections.observableList(productsService.getAllProducts());
        productsTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTC.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTC.setOnEditCommit(e -> {
            ProductModel product = e.getRowValue();
            product.setName(e.getNewValue());
            productsService.saveProduct(product);
        });

        descriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionTC.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionTC.setOnEditCommit(e -> {
            ProductModel product = e.getRowValue();
            product.setDescription(e.getNewValue());
            productsService.saveProduct(product);
        });

        providerTC.setCellValueFactory(new PropertyValueFactory<>("provider"));
        providerTC.setCellFactory(TextFieldTableCell.forTableColumn());
        providerTC.setOnEditCommit(e -> {
            ProductModel product = e.getRowValue();
            product.setProvider(e.getNewValue());
            productsService.saveProduct(product);
        });

        productsTV.setItems(products);
    }

    public void addProductBtnClick(MouseEvent mouseEvent) {
        if (!nameInputTA.getText().equals("")
                && !descriptionInputTA.getText().equals("")
                && !providerInputTA.getText().equals("")) {
            ProductModel newProduct = new ProductModel();
            newProduct.setName(nameInputTA.getText());
            newProduct.setDescription(descriptionInputTA.getText());
            newProduct.setProvider(providerInputTA.getText());

            productsService.saveProduct(newProduct);
            products.add(newProduct);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText(null);
            info.setContentText("Товар успешно добавлен!");
            info.setTitle("Информация о товаре");
            info.showAndWait();

            nameInputTA.setText("");
            descriptionInputTA.setText("");
            providerInputTA.setText("");

        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, "Заполните все поля!", ButtonType.OK);
            error.setTitle("Ошибка сохранения");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }


    public void onKeyPressedTV(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE) && productsTV.getSelectionModel().getSelectedItems().size() > 0) {
            List<ProductModel> removedProducts = productsTV.getSelectionModel().getSelectedItems();

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить " + removedProducts.size() + " товаров?", ButtonType.OK, ButtonType.CANCEL);
            conf.setHeaderText(null);
            conf.setTitle("Подтверждения удаления");
            conf.showAndWait();
            if (conf.getResult() == ButtonType.OK) {
                productsService.removeByListObjects(removedProducts);
                products.removeAll(removedProducts);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setContentText("Товары успешно удалены!");
                info.setTitle("Информация о товаре");
                info.showAndWait();
            }

        }
    }


    public void menuAddProductBtnClick(MouseEvent mouseEvent) {
        allProductsGP.setVisible(false);
        addProductGP.setVisible(true);

        menuAddProductBtn.setDisable(true);
        menuAllProductsBtn.setDisable(false);
    }

    public void menuAllProductsBtnClick(MouseEvent mouseEvent) {
        allProductsGP.setVisible(true);
        addProductGP.setVisible(false);

        menuAddProductBtn.setDisable(false);
        menuAllProductsBtn.setDisable(true);
    }

    public void leaveBtnClick(MouseEvent mouseEvent) {
        WindowsUtils.openNewWindow("main-view.fxml", mouseEvent);
    }
}
