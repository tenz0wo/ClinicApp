package ru.clinic.clinicapp.registration;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegistrationView {

    private final Stage stage;
    private TextField txtFio;
    private TextField txtAddr;
    private TextField txtPolis;
    private TextField txtEmail;
    private TextField txtUsername;
    private PasswordField txtPassword;
    private PasswordField txtConfirmPassword;
    private ComboBox<String> genderComboBox;

    private Button btnRegister;

    public RegistrationView(Stage stage) {
        this.stage = stage;
        initializeComponents();
    }

    private void initializeComponents() {
        stage.setTitle("Регистрация");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid-pane");

        Label txtMain = new Label("Регистрация");
        txtMain.setId("txtMain");
        grid.add(txtMain, 0, 0);

        //Личная информация
        txtFio = new TextField();
        txtFio.setId("inputText");
        txtFio.setPromptText("Фио");
        grid.add(txtFio, 0, 1);

        //Адрес
        txtAddr = new TextField();
        txtAddr.setId("inputText");
        txtAddr.setPromptText("Адрес");
        grid.add(txtAddr, 1, 1);

        //Полис ОМС
        txtPolis = new TextField();
        txtPolis.setId("inputText");
        txtPolis.setPromptText("Полис ОМС");
        grid.add(txtPolis, 0, 2);

        //Полис ОМС
        txtEmail = new TextField();
        txtEmail.setId("inputText");
        txtEmail.setPromptText("Эл. Почта");
        grid.add(txtEmail, 1, 2);

        // Выпадающий список для выбора пола
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Мужской", "Женский", "Другой");
        genderComboBox.setPromptText("Выберите пол");
        grid.add(genderComboBox, 0, 3);

        // Логин
        txtUsername = new TextField();
        txtUsername.setId("inputText");
        txtUsername.setPromptText("Логин");
        grid.add(txtUsername, 0, 5);

        // Пароль
        txtPassword = new PasswordField();
        txtPassword.setId("inputText");
        txtPassword.setPromptText("Пароль");
        grid.add(txtPassword, 0, 6);

        // Подтверждение пароля
        txtConfirmPassword = new PasswordField();
        txtConfirmPassword.setId("inputText");
        txtConfirmPassword.setPromptText("Подтвердите пароль");
        grid.add(txtConfirmPassword, 0, 7);

        // Кнопка Регистрация
        btnRegister = new Button("Регистрация");
        btnRegister.setId("Btn");
        grid.add(btnRegister, 0, 8);

        Scene scene = new Scene(grid, 600, 400);
        scene.getStylesheets().add(0, "registration.css");
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }


    public void setRegisterButtonAction(Runnable action) {
        btnRegister.setOnAction(event -> action.run());
    }

    public void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}