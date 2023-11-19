package ru.clinic.clinicapp.clientMagic;

import ru.clinic.clinicapp.database.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    SQLiteConnection sqLiteConnection = new SQLiteConnection();

    protected List<String> getClinics() {
        List<String> clinics = new ArrayList<>();
        String query = "SELECT region_name FROM Region";

        try {
            Connection connection = sqLiteConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String clinicName = resultSet.getString("region_name");
                clinics.add(clinicName);
            }

            resultSet.close();
            statement.close();
            sqLiteConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clinics;
    }

    protected List<String> getDoctor(String clinic) {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT fio FROM Paramedic p join region r\n" +
                        "on p.id_region = r.id_region\n" +
                        "where r.region_name = ?" ;

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Устанавливаем значение параметра
            statement.setString(1, clinic);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String doctorName = resultSet.getString("fio");
                    doctors.add(doctorName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }


    protected void createOrder(Integer userId, String doctor, String time) {
        String query = "INSERT INTO Visits (id_patient, id_paramedic, visit_date) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Получаем id доктора на основе doctor
            int doctorId = getDoctorId(doctor);

            // Устанавливаем значения параметров
            statement.setInt(1, userId);
            statement.setInt(2, doctorId);
            statement.setString(3, time);

            // Выполняем запрос на создание заявки
            statement.executeUpdate();

            System.out.println("Заявка успешно создана!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getDoctorId(String doctor) {
        String query = "SELECT id_paramedic FROM Paramedic WHERE fio = ?";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Устанавливаем значение параметра
            statement.setString(1, doctor);

            // Выполняем запрос и получаем результат
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_paramedic");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0; // В случае ошибки или отсутствия записи возвращаем -1
    }

    protected List<String> getAppointments(Integer user_id) {
        List<String> appointments = new ArrayList<>();

        String sqlQuery = "SELECT " +
                "p.fio AS patient_name, " +
                "p.polis_number AS patient_polis, " +
                "pr.fio AS paramedic_name, " +
                "r.region_name AS clinic, " +
                "v.visit_date AS visit_time, " +
                "v.diagnosis AS patient_diagnosis " +
                "FROM " +
                "Visits v " +
                "INNER JOIN Patient p ON v.id_patient = p.id_patient " +
                "INNER JOIN Paramedic pr ON v.id_paramedic = pr.id_paramedic " +
                "INNER JOIN Region r ON pr.id_region = r.id_region " +
                "INNER JOIN users u ON p.id_patient = u.id_patient " +
                "WHERE u.id_user = ?";

        try (Connection connection = sqLiteConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

            statement.setInt(1, user_id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String appointment = resultSet.getString("patient_name") + ", " +
                        resultSet.getString("patient_polis") + ", " +
                        resultSet.getString("paramedic_name") + ", " +
                        resultSet.getString("clinic") + ", " +
                        resultSet.getString("visit_time") + ", " +
                        resultSet.getString("patient_diagnosis");

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
}
