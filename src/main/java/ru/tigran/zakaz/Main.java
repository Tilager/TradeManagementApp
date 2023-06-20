package ru.tigran.zakaz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main extends Application {
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        applicationContext = SpringApplication.run(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main-view.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Приложение Regalo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        applicationContext.close();
    }


}