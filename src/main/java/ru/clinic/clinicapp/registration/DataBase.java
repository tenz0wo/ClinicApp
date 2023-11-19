package ru.clinic.clinicapp.registration;

import ru.clinic.clinicapp.database.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
    SQLiteConnection sqLiteConnection = new SQLiteConnection();

    protected boolean registerUser(String fio, String addr, String polis, String gender, String email, String username, String password) {
        boolean isRegistered = false;
        String addPatientQuery = "INSERT INTO Patient (fio, address, gender, polis_number) VALUES (?, ?, ?, ?)";
        String addUserQuery = "INSERT INTO users (username, password, email, role, id_paramedic, id_patient) VALUES (?, ?, ?, ?, ?, ?)";
        String getIdQuery = "SELECT last_insert_rowid()";

        try {
            Connection connection = sqLiteConnection.getConnection();

            // Вставляем новую запись в таблицу "Patient"
            PreparedStatement statement = connection.prepareStatement(addPatientQuery);
            statement.setString(1, fio);
            statement.setString(2, addr);
            statement.setString(3, gender);
            statement.setString(4, polis);
            statement.executeUpdate();

            // Получаем id_patient, который был автоматически сгенерирован при вставке записи в таблицу "Patient"
            statement = connection.prepareStatement(getIdQuery);
            ResultSet resultSet = statement.executeQuery();
            int id_patient = resultSet.getInt(1);

            // Вставляем новую запись в таблицу "users"
            statement = connection.prepareStatement(addUserQuery);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, null);
            statement.setString(5, null);
            statement.setInt(6, id_patient);
            statement.executeUpdate();

            // Если запросы выполнены успешно, то пользователь зарегистрирован
            isRegistered = true;

            // Закрываем ресурсы
            resultSet.close();
            statement.close();
            sqLiteConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRegistered;
    }

}
