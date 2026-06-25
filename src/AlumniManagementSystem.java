package alumni.ui;

import alumni.dao.AlumniDAO;
import alumni.model.Alumni;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class AlumniManagementSystem {
    private Connection conn;
    private AlumniDAO dao;
    private Scanner sc;

    public AlumniManagementSystem() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alumni_db", "root", ""); // ✅ Update DB name/password if needed
            dao = new AlumniDAO(conn);
            sc = new Scanner(System.in);
            System.out.println("✅ Connected to Database Successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== 🎓 Alumni Management System ===");
            System.out.println("1. Add Alumni");
            System.out.println("2. View All Alumni");
            System.out.println("3. Update Alumni");
            System.out.println("4. Delete Alumni");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addAlumni();
                    break;
                case 2:
                    viewAllAlumni();
                    break;
                case 3:
                    updateAlumni();
                    break;
                case 4:
                    deleteAlumni();
                    break;
                case 5:
                    System.out.println("Exiting... 👋");
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again!");
                    break;
            }
        }
    }

    private void addAlumni() {
        System.out.println("\n--- Add New Alumni ---");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Graduation Year: ");
        int year = sc.nextInt();
        sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Company: ");
        String company = sc.nextLine();
        System.out.print("Position: ");
        String position = sc.nextLine();
        System.out.print("Location: ");
        String location = sc.nextLine();
        System.out.print("Mobile: ");
        String mobile = sc.nextLine();
        System.out.print("Marital Status: ");
        String marital = sc.nextLine();

        Alumni a = new Alumni(name, email, year, dept, company, position, location, mobile, marital, null);
        if (dao.addAlumni(a))
            System.out.println("✅ Alumni added successfully!");
        else
            System.out.println("❌ Failed to add alumni!");
    }

    private void viewAllAlumni() {
        System.out.println("\n--- Alumni List ---");
        List<Alumni> list = dao.getAllAlumni();
        if (list.isEmpty()) {
            System.out.println("No alumni found.");
            return;
        }

        for (Alumni a : list) {
            System.out.printf("%d | %s | %s | %d | %s | %s | %s | %s | %s | %s\n",
                    a.getId(), a.getName(), a.getEmail(), a.getGradYear(),
                    a.getDepartment(), a.getCompany(), a.getPosition(),
                    a.getLocation(), a.getMobile(), a.getMaritalStatus());
        }
    }

    private void updateAlumni() {
        System.out.print("\nEnter Alumni ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        Alumni existing = dao.getAlumniById(id);
        if (existing == null) {
            System.out.println("❌ Alumni not found!");
            return;
        }

        System.out.print("New Department: ");
        String dept = sc.nextLine();
        existing.setDepartment(dept);

        System.out.print("New Company: ");
        String company = sc.nextLine();
        existing.setCompany(company);

        if (dao.updateAlumni(existing))
            System.out.println("✅ Alumni updated successfully!");
        else
            System.out.println("❌ Update failed!");
    }

    private void deleteAlumni() {
        System.out.print("\nEnter Alumni ID to delete: ");
        int id = sc.nextInt();
        if (dao.deleteAlumni(id))
            System.out.println("✅ Alumni deleted successfully!");
        else
            System.out.println("❌ Failed to delete alumni!");
    }

    public static void main(String[] args) {
        AlumniManagementSystem app = new AlumniManagementSystem();
        app.showMenu();
    }
}
