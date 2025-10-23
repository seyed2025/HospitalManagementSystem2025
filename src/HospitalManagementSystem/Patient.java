package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;

    }
    public void addPatient(){
        System.out.print("Enter patient name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient gender: ");
        scanner.nextLine();
        String gener = scanner.nextLine();

        try {
            String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gener);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows>0){
                System.out.println("Patient added successfully !! ");
            } else {
                System.out.println("Failed to add the patient !!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void viewPatient(){
    String query = "SELECT * FROM patients";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("patients: ");
        System.out.println("+------------+------------------+-------------+--------------+");
        System.out.println("| patient id | name             | age         | gender       |");
        System.out.println("+------------+------------------+-------------+--------------+");
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            System.out.printf("| %-11s| %-17s| %-12s| %-15s |\n", id, name, age, gender);

            System.out.println("+------------+------------------+-------------+--------------+");

        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    public boolean getpetienstById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
