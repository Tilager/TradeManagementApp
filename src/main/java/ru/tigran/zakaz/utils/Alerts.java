package ru.tigran.zakaz.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alerts {
    public static void alertErrorTypes() {
        Alert error = new Alert(Alert.AlertType.ERROR, "Неверные типы данных!", ButtonType.OK);
        error.setTitle("Ошибка сохранения");
        error.setHeaderText(null);
        error.showAndWait();
    }
}
