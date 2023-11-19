package ru.clinic.clinicapp.login;

import ru.clinic.clinicapp.database.SQLiteConnection;
import ru.clinic.clinicapp.models.UserModel;

import java.sql.*;

public class DataBase {
    SQLiteConnection sqLiteConnection = new SQLiteConnection();

    protected UserModel authenticateUser(String username, String password) {
        boolean isAuthenticated = false;
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        UserModel userModel = new UserModel();

        try {
            Connection connection = sqLiteConnection.getConnection();

            // Выполняем запрос к базе данных для проверки наличия пользователя с указанным именем и паролем
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userModel.setId(resultSet.getInt("id_user"));
                userModel.setUsername(resultSet.getString("username"));
                userModel.setPassword(resultSet.getString("password"));
                userModel.setEmail(resultSet.getString("email"));
                userModel.setRole(resultSet.getString("role"));
                userModel.setId_paramedic(resultSet.getInt("id_paramedic"));
                userModel.setId_patient(resultSet.getInt("id_patient"));
            }

            // Закрываем ресурсы
            resultSet.close();
            statement.close();
            sqLiteConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userModel;
    }
}
