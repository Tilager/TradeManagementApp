package ru.tigran.zakaz.controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.Main;
import ru.tigran.zakaz.utils.WindowsUtils;

import java.io.IOException;

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
}