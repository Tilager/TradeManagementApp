package ru.tigran.zakaz.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.utils.WindowsUtils;

@Controller
public class MainController{
    @FXML
    private void purchaseButtonClick(MouseEvent event) {
        WindowsUtils.openNewWindow("warehouse/warehouse-view.fxml", event);
    }

    @FXML
    public void productManagementBTNClick(MouseEvent event) {
        WindowsUtils.openNewWindow("products/product-view.fxml", event);
    }

    public void salesButtonClick(MouseEvent event) {
        WindowsUtils.openNewWindow("sales/sale-view.fxml", event);
    }

    public void expenseBTNClick(MouseEvent event) {
        WindowsUtils.openNewWindow("expenses/expenses-view.fxml", event);
    }
}