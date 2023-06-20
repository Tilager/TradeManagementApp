package ru.tigran.zakaz.controllers.expenses;

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
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.tigran.zakaz.models.ExpensesModel;
import ru.tigran.zakaz.services.ExpensesService;
import ru.tigran.zakaz.utils.Alerts;
import ru.tigran.zakaz.utils.WindowsUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ExpensesController implements Initializable {
    @FXML
    private Button menuAddExpensesBtn;

    @FXML
    private Button menuAllExpensesBtn;

    @FXML
    private TableColumn<ExpensesModel, Integer> countTC;

    @FXML
    private TableColumn<ExpensesModel, String> nameTC;

    @FXML
    private TableView<ExpensesModel> expensesTV;

    @FXML
    private TableColumn<ExpensesModel, Double> priceTC;

    @FXML
    private TableColumn<ExpensesModel, Double> costTC;

    @FXML
    private GridPane addExpensesGP;
    @FXML
    private GridPane allExpensesGP;

    @FXML
    private TextArea priceInputTA;

    @FXML
    private TextArea nameInputTA;

    @FXML
    private TextArea countInputTA;

    private ObservableList<ExpensesModel> expenses;

    private final ExpensesService expensesService;

    @Autowired
    public ExpensesController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        // setting table
        expenses = FXCollections.observableList(expensesService.getAllExpenses());
        expensesTV.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameTC.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameTC.setCellFactory(TextFieldTableCell.forTableColumn());
        nameTC.setOnEditCommit(e -> {
            ExpensesModel expense = e.getRowValue();
            expense.setName(e.getNewValue());
            expensesService.saveExpense(expense);
            expensesTV.refresh();
        });

        countTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        countTC.setCellFactory(col -> {
                    TextFieldTableCell<ExpensesModel, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());

                    // Настройка валидации ввода
                    cell.setConverter(new IntegerStringConverter() {
                        @Override
                        public Integer fromString(String value) {
                            // Проверка, что введенное значение является числом
                            if (!value.matches("\\d+(\\.\\d+)?")) {
                                // В случае неверного формата ввода, возвращаем текущее значение ячейки
                                Alerts.alertErrorTypes();
                                return cell.getItem();
                            }

                            // Возвращаем числовое значение
                            return super.fromString(value);
                        }
                    });

                    return cell;
                }
        );
        countTC.setOnEditCommit(e -> {
            ExpensesModel expense = e.getRowValue();
            expense.setCount(e.getNewValue());
            expensesService.saveExpense(expense);
            expensesTV.refresh();
        });

        priceTC.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceTC.setCellFactory(col -> {
                    TextFieldTableCell<ExpensesModel, Double> cell = new TextFieldTableCell<>(new DoubleStringConverter());

                    // Настройка валидации ввода
                    cell.setConverter(new DoubleStringConverter() {
                        @Override
                        public Double fromString(String value) {
                            // Проверка, что введенное значение является числом
                            if (!value.matches("\\d+(\\.\\d+)?")) {
                                // В случае неверного формата ввода, возвращаем текущее значение ячейки
                                Alerts.alertErrorTypes();
                                return cell.getItem();
                            }

                            // Возвращаем числовое значение
                            return super.fromString(value);
                        }
                    });

                    return cell;
                }
        );
        priceTC.setOnEditCommit(e -> {
            ExpensesModel expense = e.getRowValue();
            expense.setPrice(e.getNewValue());
            expensesService.saveExpense(expense);
            expensesTV.refresh();
        });

        costTC.setCellValueFactory(new PropertyValueFactory<>("cost"));

        expensesTV.setItems(expenses);
    }

    public void addExpensesBtnClick(MouseEvent mouseEvent) {
        try {
            if (!nameInputTA.getText().equals("")
                    && !countInputTA.getText().equals("")
                    && !priceInputTA.getText().equals("")) {
                ExpensesModel newExpense = new ExpensesModel();
                newExpense.setName(nameInputTA.getText());
                newExpense.setCount(Integer.parseInt(countInputTA.getText()));
                newExpense.setPrice(Double.parseDouble(priceInputTA.getText()));

                expensesService.saveExpense(newExpense);
                expenses.add(newExpense);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setContentText("Расход успешно добавлен!");
                info.setTitle("Информация о расходах");
                info.showAndWait();

                nameInputTA.setText("");
                countInputTA.setText("");
                priceInputTA.setText("");

            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Заполните все поля!", ButtonType.OK);
                error.setTitle("Ошибка сохранения");
                error.setHeaderText(null);
                error.showAndWait();
            }
        } catch (NumberFormatException e) {
            Alerts.alertErrorTypes();
        }
    }


    public void onKeyPressedTV(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE) && expensesTV.getSelectionModel().getSelectedItems().size() > 0) {
            List<ExpensesModel> removedExpenses = expensesTV.getSelectionModel().getSelectedItems();

            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Вы действительно хотите удалить " + removedExpenses.size() + " товаров?", ButtonType.OK, ButtonType.CANCEL);
            conf.setHeaderText(null);
            conf.setTitle("Подтверждения удаления");
            conf.showAndWait();
            if (conf.getResult() == ButtonType.OK) {
                expensesService.removeByListObjects(removedExpenses);
                expenses.removeAll(removedExpenses);

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setHeaderText(null);
                info.setContentText("Товары успешно удалены!");
                info.setTitle("Информация о товаре");
                info.showAndWait();
            }

        }
    }


    public void menuAddExpensesBtnClick(MouseEvent mouseEvent) {
        allExpensesGP.setVisible(false);
        addExpensesGP.setVisible(true);

        menuAddExpensesBtn.setDisable(true);
        menuAllExpensesBtn.setDisable(false);
    }

    public void menuAllExpensesBtnClick(MouseEvent mouseEvent) {
        allExpensesGP.setVisible(true);
        addExpensesGP.setVisible(false);

        menuAddExpensesBtn.setDisable(false);
        menuAllExpensesBtn.setDisable(true);
    }

    public void leaveBtnClick(MouseEvent mouseEvent) {
        WindowsUtils.openNewWindow("main-view.fxml", mouseEvent);
    }
}
