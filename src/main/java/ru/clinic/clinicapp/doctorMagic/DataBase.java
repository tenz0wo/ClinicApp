package ru.clinic.clinicapp.doctorMagic;

import ru.clinic.clinicapp.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DataBase {
    SQLiteConnection sqLiteConnection = new SQLiteConnection();

    protected List<List> getAppointments(Integer user_id) {

        List<List> docAppointments = new ArrayList<>();

        String sqlQuery = "SELECT " +
                "p.fio AS patient_name, " +
                "p.polis_number AS patient_polis, " +
                "pr.fio AS paramedic_name, " +
                "r.region_name AS clinic, " +
                "v.visit_date AS visit_time, " +
                "v.diagnosis AS patient_diagnosis, " +
                "p.id_patient AS id_user " +
                "FROM " +
                "Visits v " +
                "INNER JOIN Patient p ON v.id_patient = p.id_patient " +
                "INNER JOIN Paramedic pr ON v.id_paramedic = pr.id_paramedic " +
                "INNER JOIN Region r ON pr.id_region = r.id_region " +
                "INNER JOIN users u ON p.id_patient = u.id_patient " +
                "WHERE pr.id_paramedic = ?;";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                List<String> order = new ArrayList<>();
                order.add(resultSet.getString("patient_name"));
                order.add(resultSet.getString("patient_polis"));
                order.add(resultSet.getString("paramedic_name"));
                order.add(resultSet.getString("clinic"));
                order.add(resultSet.getString("visit_time"));
                order.add(resultSet.getString("patient_diagnosis"));
                order.add(resultSet.getString("id_user"));

                docAppointments.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return docAppointments;
    }

    protected List<String> getTitleAppointment(Integer user_id) {

        List<String> orderTitle = new ArrayList<>();

        String sqlQuery = "SELECT " +
                "p.fio AS patient_name " +
                "FROM " +
                "Visits v " +
                "INNER JOIN Patient p ON v.id_patient = p.id_patient " +
                "INNER JOIN Paramedic pr ON v.id_paramedic = pr.id_paramedic " +
                "WHERE pr.id_paramedic = ?";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                orderTitle.add(resultSet.getString("patient_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderTitle;
    }

    protected void saveAppointment(Integer user_id, String patientDiagnosis) {
        String sqlQuery = "UPDATE visits SET diagnosis = ? WHERE id_patient = ?";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, patientDiagnosis);
            statement.setInt(2, user_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error occurred while saving the appointment.");
            e.printStackTrace();
        }
    }
}
