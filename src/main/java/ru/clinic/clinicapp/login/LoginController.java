package ru.clinic.clinicapp.login;

import javafx.stage.Stage;
import lombok.Getter;
import ru.clinic.clinicapp.client.UserDashboardController;
import ru.clinic.clinicapp.doctor.DoctorDashboardController;
import ru.clinic.clinicapp.models.UserModel;
import ru.clinic.clinicapp.registration.RegistrationController;
import java.util.prefs.Preferences;


public class LoginController {

    private final DataBase dataBase = new DataBase();
    private final Preferences prefs;
    private final LoginView view;
    @Getter
    private Stage stage;


    public LoginController(Stage stage) {
        setStage(stage);
        prefs = Preferences.userNodeForPackage(LoginController.class);
        view = new LoginView(stage);

        // Получение сохраненных имени пользователя и пароля из Preferences
        String savedUsername = getSavedUsername();
        String savedPassword = getSavedPassword();

        // Если имя пользователя и пароль сохранены, автоматически заполнить поля ввода
        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            view.setUsername(savedUsername);
            view.setPassword(savedPassword);
            view.setRememberMeSelected(true);
        }

        view.setLoginButtonAction(this::onLoginButtonClicked);
        view.setRegistrationButtonAction(this::onRegistrationButtonClicked);

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getSavedUsername() {
        return prefs.get("username", "");
    }

    public String getSavedPassword() {
        return prefs.get("password", "");
    }

    public void show() {
        view.show();
    }


    private void onLoginButtonClicked() {
        String username = view.getUsername();
        String password = view.getPassword();

        // Проверка введенных данных на правильность и авторизация
        UserModel userModel = authenticateUser(username, password);

        if (userModel.getId() != null) {
            // Проверка состояния чекбокса "Запомнить меня"
            if (view.isRememberMeSelected()) {
                // Запомнить логин и пароль
                rememberLoginCredentials(username, password);
            }

            // Переход на экран администратора или пользователя в зависимости от роли
            if (userModel.getId_paramedic() != 0) {
                DoctorDashboardController doctorDashboardController = new DoctorDashboardController(stage, userModel);
                doctorDashboardController.show();
            } else {
                UserDashboardController userDashboardController = new UserDashboardController(stage, userModel);
                userDashboardController.show();
            }

            // Закрытие формы авторизации
            view.show();
        } else {
            // Отображение сообщения об ошибке авторизации
            view.showErrorAlert("Ошибка авторизации", "Неправильный логин или пароль. Пожалуйста, попробуйте снова.");
        }
    }

    private void onRegistrationButtonClicked() {
        RegistrationController registrationController = new RegistrationController(new Stage());
        registrationController.show();
    }

    private UserModel authenticateUser(String username, String password) {
        return dataBase.authenticateUser(username, password);
    }

    public void rememberLoginCredentials(String username, String password) {
        prefs.put("username", username);
        prefs.put("password", password);
    }

    private boolean checkUserRole(Integer id) {
        // Здесь можно добавить логику для проверки роли пользователя
        // Например, проверка данных в базе данных или файле

        return false; // Заглушка, всегда false
    }
}
