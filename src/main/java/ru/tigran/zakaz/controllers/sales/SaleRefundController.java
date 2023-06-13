package ru.tigran.zakaz.controllers.sales;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.SaleModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.services.SalesService;
import ru.tigran.zakaz.services.WarehouseService;

@Controller
public class SaleRefundController {
    @FXML
    private TextField countTA;

    private SaleModel refundSale;
    private ObservableList<SaleModel> allSales;
    private final SalesService salesService;
    private final WarehouseService warehouseService;

    @Autowired
    public SaleRefundController(SalesService salesService, WarehouseService warehouseService) {
        this.salesService = salesService;
        this.warehouseService = warehouseService;
    }

    public void cancelBtnClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) countTA.getScene().getWindow();
        stage.close();
    }

    public void refundBtnClick(MouseEvent mouseEvent) {
        WarehouseModel warehouse = refundSale.getWarehouse();

        int count;
        try {
            count = Integer.parseInt(countTA.getText());
        } catch (NumberFormatException e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Неверные типы данных!", ButtonType.OK);
            error.setTitle("Ошибка возврата");
            error.setHeaderText(null);
            error.showAndWait();

            return;
        }

        if (refundSale.getCount() < count || count <= 0) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Количество больше проданного товара и меньше 0", ButtonType.OK);
            error.setTitle("Ошибка возврата");
            error.setHeaderText(null);
            error.showAndWait();

            return;
        }

        refundSale.setCount(refundSale.getCount() - count);
        warehouse.setCount(warehouse.getCount() + count);

        if (refundSale.getCount() == 0) {
            salesService.remove(refundSale);
            allSales.remove(refundSale);
        }
        else
            salesService.saveSale(refundSale);

        warehouseService.saveWarehouses(warehouse);

        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText(null);
        info.setContentText("Возврат произошел успешно!");
        info.setTitle("Информация о возврате");
        info.showAndWait();

        Stage stage = (Stage) countTA.getScene().getWindow();
        stage.close();
    }

    public void setRefundSale(SaleModel refundSale) {
        this.refundSale = refundSale;
    }

    public void setAllSales(ObservableList<SaleModel> allSales) {
        this.allSales = allSales;
    }
}
