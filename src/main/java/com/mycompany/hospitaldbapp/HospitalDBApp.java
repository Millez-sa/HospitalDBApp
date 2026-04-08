/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.hospitaldbapp;

/**
 *
 * @author Faranani Matsa
 */
import java.sql.*;
import java.util.Scanner;

public class HospitalDBApp {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Password";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            return;
        }

        while (true) {
            System.out.println("\nPatient Database Menu:");
            System.out.println("1. View Patients");
            System.out.println("2. Add Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewPatients();
                    break;
                case "2":
                    addPatient();
                    break;
                case "3":
                    deletePatient();
                    break;
                case "4":
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void viewPatients() {
        System.out.println("\nPatient Records:");
        String sql = "SELECT * FROM patients";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("full_name");
                String kin = rs.getString("next_of_kin");
                String addr = rs.getString("address");
                System.out.println(id + " | " + name + " | " + kin + " | " + addr);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving patients: " + e.getMessage());
        }
    }

    private static void addPatient() {
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter next of kin: ");
        String kin = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        String sql = "INSERT INTO patients (full_name, next_of_kin, address) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, kin);
            ps.setString(3, address);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Patient added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
    }

    private static void deletePatient() {
        System.out.print("Enter patient ID to delete: ");
        String id = scanner.nextLine();

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("No patient found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
        }
    }
}

