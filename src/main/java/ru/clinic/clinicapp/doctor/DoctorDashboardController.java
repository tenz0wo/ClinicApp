package ru.clinic.clinicapp.doctor;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.clinic.clinicapp.clientMagic.AppointmentsController;
import ru.clinic.clinicapp.doctorMagic.EditAppointmentController;
import ru.clinic.clinicapp.login.LoginController;
import ru.clinic.clinicapp.models.UserModel;

public class DoctorDashboardController {
    private final DoctorDashboardView view;
    private final Stage stage;
    private final UserModel userModel;

    public DoctorDashboardController(Stage stage, UserModel userModel) {
        this.stage = stage;
        this.userModel = userModel;
        view = new DoctorDashboardView(stage);
        bindEvents();
    }

    private void bindEvents() {
        Button editAppointmentButton = view.getEditAppointmentButton();
        Button logoutButton = view.getLogoutButton();

        editAppointmentButton.setOnAction(event -> openEditAppointmentGUI());
        logoutButton.setOnAction(event -> logout());
    }

    public void show() {
        view.show();
    }

    private void openEditAppointmentGUI() {
        EditAppointmentController editAppointmentController = new EditAppointmentController(stage, userModel);
        editAppointmentController.show();
    }

    private void logout() {
        stage.close();
        LoginController loginController = new LoginController(new Stage());
        loginController.show();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}