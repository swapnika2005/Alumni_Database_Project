package alumni.dao;

import alumni.model.Alumni;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumniDAO {

    private Connection conn;

    public AlumniDAO(Connection conn) {
        this.conn = conn;
    }

    // INSERT a new Alumni record
    public boolean addAlumni(Alumni alumni) {
        String sql = "INSERT INTO alumni (name, email, grad_year, department, company, position, location, mobile, marital_status, photo_path) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alumni.getName());
            ps.setString(2, alumni.getEmail());
            ps.setInt(3, alumni.getGradYear());
            ps.setString(4, alumni.getDepartment());
            ps.setString(5, alumni.getCompany());
            ps.setString(6, alumni.getPosition());
            ps.setString(7, alumni.getLocation());
            ps.setString(8, alumni.getMobile());
            ps.setString(9, alumni.getMaritalStatus());
            ps.setString(10, alumni.getPhotoPath());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // FETCH all Alumni records
    public List<Alumni> getAllAlumni() {
        List<Alumni> list = new ArrayList<>();
        String sql = "SELECT * FROM alumni";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Alumni a = new Alumni(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("grad_year"),
                        rs.getString("department"),
                        rs.getString("company"),
                        rs.getString("position"),
                        rs.getString("location"),
                        rs.getString("mobile"),
                        rs.getString("marital_status"),
                        rs.getString("photo_path")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // FETCH single Alumni by ID
    public Alumni getAlumniById(int id) {
        Alumni alumni = null;
        String sql = "SELECT * FROM alumni WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alumni = new Alumni(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("grad_year"),
                        rs.getString("department"),
                        rs.getString("company"),
                        rs.getString("position"),
                        rs.getString("location"),
                        rs.getString("mobile"),
                        rs.getString("marital_status"),
                        rs.getString("photo_path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumni;
    }

    // ✅ FETCH Alumni by Username
    public Alumni getAlumniByUsername(String username) {
        Alumni alumni = null;
        String sql = "SELECT * FROM alumni WHERE name = ?"; // use username if exists
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alumni = new Alumni(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("grad_year"),
                        rs.getString("department"),
                        rs.getString("company"),
                        rs.getString("position"),
                        rs.getString("location"),
                        rs.getString("mobile"),
                        rs.getString("marital_status"),
                        rs.getString("photo_path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alumni;
    }

    // UPDATE Alumni record
    public boolean updateAlumni(Alumni alumni) {
        String sql = "UPDATE alumni SET email=?, grad_year=?, department=?, company=?, position=?, location=?, mobile=?, marital_status=?, photo_path=? "
                   + "WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alumni.getEmail());
            ps.setInt(2, alumni.getGradYear());
            ps.setString(3, alumni.getDepartment());
            ps.setString(4, alumni.getCompany());
            ps.setString(5, alumni.getPosition());
            ps.setString(6, alumni.getLocation());
            ps.setString(7, alumni.getMobile());
            ps.setString(8, alumni.getMaritalStatus());
            ps.setString(9, alumni.getPhotoPath());
            ps.setInt(10, alumni.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE Alumni record
    public boolean deleteAlumni(int id) {
        String sql = "DELETE FROM alumni WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
