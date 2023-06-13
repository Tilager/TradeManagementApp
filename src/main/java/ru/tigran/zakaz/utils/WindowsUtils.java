package ru.tigran.zakaz.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.tigran.zakaz.Main;

import java.io.IOException;

@Component
public class WindowsUtils {
    private static ApplicationContext applicationContext;

    @Autowired
    public WindowsUtils(ApplicationContext appContext) {
        applicationContext = appContext;
    }

    public static Stage changeWindow(String view, Event e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/" + view));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        stage.setTitle("Приложение Regalo");
        stage.setScene(scene);
        return stage;
    }

    public static void openDialogWindow(String view, Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/" + view));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent page = fxmlLoader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Приложение Regalo");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public static void openNewWindow(String view, Event event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/" + view));
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Приложение Regalo");
            stage.setScene(new Scene(root));
            stage.show();

            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage openModalWindowWithController(Event event, FXMLLoader fxmlLoader) throws IOException {
        Parent page = fxmlLoader.load();
        Stage dialogStage = new Stage();
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        dialogStage.setTitle("Приложение Regalo");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        return dialogStage;
    }

    public static FXMLLoader getFxmlLoaderWithSpringContext(String view) {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/" + view));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        return fxmlLoader;
    }
}
