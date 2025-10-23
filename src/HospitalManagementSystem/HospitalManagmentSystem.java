package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagmentSystem {
    private static final String url ="jdbc:mysql://localhost:3306/hospital";
    private static final String usernam = "root";
    private static final String password = "Besme786";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url,usernam,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("***************************");
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("***************************");
                System.out.println("1---> Add Patient: ");
                System.out.println("2---> View Patient: ");
                System.out.println("3---> View Doctor: ");
                System.out.println("4---> Book Appointment: ");
                System.out.println("5---> Exit: ");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice){
                    case 1: patient.addPatient();
                    break;
                    case 2: patient.viewPatient();
                    break;
                    case 3: doctor.viewDoctors();
                    break;
                    case 4: bookAppointment(patient, doctor, connection,scanner);
                    break;
                    case 5:
                System.out.println("***************************");
                System.out.println("     HAVE A NICE TIME");
                System.out.println("***************************");
                    return ;
                    default:
                        System.out.println("Enter valid choice !!!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter patient id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter doctor id: ");
        int doctotId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        scanner.nextLine();
        String appointmetDate = scanner.nextLine();
        if (patient.getpetienstById(patientId) && doctor.getdoctorById(doctotId)){
            if (chechDoctorAvailability(doctotId, appointmetDate, connection)){
                String appointmentQuery = "INSERT INTO appointment(patient_id, doctor_id, appointment_data) VALUES (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctotId);
                    preparedStatement.setString(3, appointmetDate);
                    int rowAfficted = preparedStatement.executeUpdate();
                    if (rowAfficted>0){
                        System.out.println("Appointment booked ");
                    }else {
                        System.out.println("Booking failed !!!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is NOT available on this date: ");
            }
        }else {
            System.out.println("Doctor or patient dose NOT exist!!");
        }

    }
    public static boolean chechDoctorAvailability(int doctorID, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_data = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorID);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
