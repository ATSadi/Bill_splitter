package com.example.roomshare;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:roomshare.db";
    private static DatabaseHelper instance;
    private int currentRoomId = -1;

    private DatabaseHelper() {
        initializeDatabase();
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public void setCurrentRoomId(int roomId) {
        this.currentRoomId = roomId;
    }

    public int getCurrentRoomId() {
        return currentRoomId;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS roommates (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT, phone TEXT, room_id INTEGER NOT NULL, FOREIGN KEY(room_id) REFERENCES rooms(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS bills (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, amount REAL NOT NULL, room_id INTEGER NOT NULL, FOREIGN KEY(room_id) REFERENCES rooms(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS expenses (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, amount REAL NOT NULL, payer_id INTEGER NOT NULL, room_id INTEGER NOT NULL, split_count INTEGER NOT NULL, date TEXT DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(payer_id) REFERENCES roommates(id), FOREIGN KEY(room_id) REFERENCES rooms(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS chores (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, assigned_to_id INTEGER, room_id INTEGER NOT NULL, completed INTEGER DEFAULT 0, date TEXT DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(assigned_to_id) REFERENCES roommates(id), FOREIGN KEY(room_id) REFERENCES rooms(id))");
            
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_roommate_room ON roommates(room_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_expense_room ON expenses(room_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_chore_room ON chores(room_id)");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllRooms() {
        List<String> rooms = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM rooms ORDER BY name")) {
            
            while (rs.next()) {
                rooms.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean deleteRoom(String roomName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM rooms WHERE name = ?")) {
            
            pstmt.setString(1, roomName);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            int roomId = rs.getInt("id");
            
            conn.setAutoCommit(false);
            try {
                PreparedStatement deleteChores = conn.prepareStatement("DELETE FROM chores WHERE room_id = ?");
                deleteChores.setInt(1, roomId);
                deleteChores.executeUpdate();
                
                PreparedStatement deleteExpenses = conn.prepareStatement("DELETE FROM expenses WHERE room_id = ?");
                deleteExpenses.setInt(1, roomId);
                deleteExpenses.executeUpdate();
                
                PreparedStatement deleteBills = conn.prepareStatement("DELETE FROM bills WHERE room_id = ?");
                deleteBills.setInt(1, roomId);
                deleteBills.executeUpdate();
                
                PreparedStatement deleteRoommates = conn.prepareStatement("DELETE FROM roommates WHERE room_id = ?");
                deleteRoommates.setInt(1, roomId);
                deleteRoommates.executeUpdate();
                
                PreparedStatement deleteRoom = conn.prepareStatement("DELETE FROM rooms WHERE id = ?");
                deleteRoom.setInt(1, roomId);
                deleteRoom.executeUpdate();
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createOrGetRoom(String roomName) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT OR IGNORE INTO rooms (name) VALUES (?)");
             PreparedStatement selectStmt = conn.prepareStatement("SELECT id FROM rooms WHERE name = ?")) {
            
            pstmt.setString(1, roomName);
            pstmt.executeUpdate();
            
            selectStmt.setString(1, roomName);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                int roomId = rs.getInt("id");
                setCurrentRoomId(roomId);
                return roomId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isPersonInAnyRoom(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM roommates WHERE LOWER(name) = LOWER(?)")) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addRoommate(String name, String email, String phone, int roomId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO roommates (name, email, phone, room_id) VALUES (?, ?, ?, ?)")) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setInt(4, roomId);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> getRoommates(int roomId) {
        List<String> roommates = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT id, name, email, phone FROM roommates WHERE room_id = ?")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String info = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                if (email != null && !email.isEmpty()) info += " - " + email;
                if (phone != null && !phone.isEmpty()) info += " - " + phone;
                roommates.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roommates;
    }

    public List<Integer> getRoommateIds(int roomId) {
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM roommates WHERE room_id = ?")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public List<String> getRoommateNames(int roomId) {
        List<String> names = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT name FROM roommates WHERE room_id = ?")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public Integer getRoommateIdByName(int roomId, String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM roommates WHERE room_id = ? AND LOWER(name) = LOWER(?)")) {
            
            pstmt.setInt(1, roomId);
            pstmt.setString(2, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBill(String name, double amount, int roomId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO bills (name, amount, room_id) VALUES (?, ?, ?)")) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, amount);
            pstmt.setInt(3, roomId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getBills(int roomId) {
        List<String> bills = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT name, amount FROM bills WHERE room_id = ?")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bills.add(rs.getString("name") + " - $" + String.format("%.2f", rs.getDouble("amount")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    public void addExpense(String name, double amount, int payerId, int roomId, int splitCount) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO expenses (name, amount, payer_id, room_id, split_count) VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, amount);
            pstmt.setInt(3, payerId);
            pstmt.setInt(4, roomId);
            pstmt.setInt(5, splitCount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getExpenses(int roomId) {
        List<String> expenses = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT e.name, e.amount, e.split_count, r.name as payer_name FROM expenses e " +
                 "JOIN roommates r ON e.payer_id = r.id WHERE e.room_id = ? ORDER BY e.date DESC")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                double amount = rs.getDouble("amount");
                int splitCount = rs.getInt("split_count");
                double perPerson = amount / splitCount;
                expenses.add(rs.getString("name") + " - $" + String.format("%.2f", amount) + 
                            " paid by " + rs.getString("payer_name") + 
                            " (Split " + splitCount + " ways, $" + String.format("%.2f", perPerson) + " each)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public void addChore(String name, Integer assignedToId, int roomId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO chores (name, assigned_to_id, room_id) VALUES (?, ?, ?)")) {
            
            pstmt.setString(1, name);
            if (assignedToId != null) {
                pstmt.setInt(2, assignedToId);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setInt(3, roomId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getChores(int roomId) {
        List<String> chores = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT c.id, c.name, c.completed, r.name as assigned_name FROM chores c " +
                 "LEFT JOIN roommates r ON c.assigned_to_id = r.id WHERE c.room_id = ? ORDER BY c.date DESC")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String status = rs.getInt("completed") == 1 ? "[X]" : "[ ]";
                String assigned = rs.getString("assigned_name");
                if (assigned == null) assigned = "Unassigned";
                chores.add(status + " " + rs.getString("name") + " - Assigned to: " + assigned);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chores;
    }

    public void markChoreComplete(int choreId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE chores SET completed = 1 WHERE id = ?")) {
            
            pstmt.setInt(1, choreId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getChoreIds(int roomId) {
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM chores WHERE room_id = ? ORDER BY date DESC")) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public String calculateBalance(int roomId) {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Balance & Settlement Summary ===\n\n");
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT r.id, r.name, " +
                "COALESCE(SUM(CASE WHEN e.payer_id = r.id THEN e.amount ELSE 0 END), 0) as paid " +
                "FROM roommates r " +
                "LEFT JOIN expenses e ON e.room_id = r.room_id " +
                "WHERE r.room_id = ? " +
                "GROUP BY r.id, r.name");
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            
            List<String> roommateData = new ArrayList<>();
            while (rs.next()) {
                roommateData.add(rs.getInt("id") + "|" + rs.getString("name") + "|" + rs.getDouble("paid"));
            }
            
            Map<Integer, Double> owedMap = new HashMap<>();
            for (String data : roommateData) {
                String[] parts = data.split("\\|");
                int id = Integer.parseInt(parts[0]);
                owedMap.put(id, 0.0);
            }
            
            PreparedStatement expenseStmt = conn.prepareStatement(
                "SELECT amount, split_count FROM expenses WHERE room_id = ?");
            expenseStmt.setInt(1, roomId);
            ResultSet expenseRs = expenseStmt.executeQuery();
            
            int roommateCount = roommateData.size();
            while (expenseRs.next()) {
                double amount = expenseRs.getDouble("amount");
                int splitCount = expenseRs.getInt("split_count");
                double perPersonShare = amount / splitCount;
                double perRoommate = roommateCount > 0 ? perPersonShare / roommateCount : 0;
                
                for (Integer id : owedMap.keySet()) {
                    owedMap.put(id, owedMap.get(id) + perRoommate);
                }
            }
            
            List<String> summaries = new ArrayList<>();
            for (String data : roommateData) {
                String[] parts = data.split("\\|");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double paid = Double.parseDouble(parts[2]);
                double owed = owedMap.getOrDefault(id, 0.0);
                double net = paid - owed;
                
                summary.append(name).append(":\n");
                summary.append("  Paid: $").append(String.format("%.2f", paid)).append("\n");
                summary.append("  Owed: $").append(String.format("%.2f", owed)).append("\n");
                summary.append("  Net Balance: ").append(net >= 0 ? "+" : "").append("$")
                       .append(String.format("%.2f", net))
                       .append(net >= 0 ? " (Others owe " : " (").append(name).append(" owes others)").append("\n\n");
                
                summaries.add(name + "|" + net);
            }
            
            summary.append("Summary:\n");
            for (String s : summaries) {
                String[] parts = s.split("\\|");
                double net = Double.parseDouble(parts[1]);
                if (net > 0) {
                    summary.append("- ").append(parts[0]).append(" should receive $")
                           .append(String.format("%.2f", net)).append(" from others\n");
                } else if (net < 0) {
                    summary.append("- ").append(parts[0]).append(" owes $")
                           .append(String.format("%.2f", Math.abs(net))).append("\n");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            summary.append("Error calculating balance.");
        }
        
        return summary.toString();
    }

    public List<String> getHistory(int roomId, String filter) {
        List<String> history = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (filter == null || filter.trim().isEmpty()) {
                PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 'Chore' as type, c.name, c.completed, r.name as person, c.date FROM chores c " +
                    "LEFT JOIN roommates r ON c.assigned_to_id = r.id WHERE c.room_id = ? " +
                    "UNION ALL " +
                    "SELECT 'Expense' as type, e.name, 0, r.name as person, e.date FROM expenses e " +
                    "JOIN roommates r ON e.payer_id = r.id WHERE e.room_id = ? " +
                    "ORDER BY date DESC");
                pstmt.setInt(1, roomId);
                pstmt.setInt(2, roomId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String person = rs.getString("person");
                    if (type.equals("Chore")) {
                        String status = rs.getInt("completed") == 1 ? "[X]" : "[ ]";
                        history.add(status + " " + name + " - Assigned to: " + (person != null ? person : "Unassigned") + 
                                   (rs.getInt("completed") == 1 ? " (Completed)" : " (Pending)"));
                    } else {
                        history.add(name + " - Paid by: " + person);
                    }
                }
            } else {
                PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT 'Chore' as type, c.name, c.completed, r.name as person FROM chores c " +
                    "LEFT JOIN roommates r ON c.assigned_to_id = r.id " +
                    "WHERE c.room_id = ? AND (LOWER(c.name) LIKE LOWER(?) OR LOWER(r.name) LIKE LOWER(?)) " +
                    "UNION ALL " +
                    "SELECT 'Expense' as type, e.name, 0, r.name as person FROM expenses e " +
                    "JOIN roommates r ON e.payer_id = r.id " +
                    "WHERE e.room_id = ? AND (LOWER(e.name) LIKE LOWER(?) OR LOWER(r.name) LIKE LOWER(?)) " +
                    "ORDER BY date DESC");
                String filterPattern = "%" + filter + "%";
                pstmt.setInt(1, roomId);
                pstmt.setString(2, filterPattern);
                pstmt.setString(3, filterPattern);
                pstmt.setInt(4, roomId);
                pstmt.setString(5, filterPattern);
                pstmt.setString(6, filterPattern);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    String person = rs.getString("person");
                    if (type.equals("Chore")) {
                        String status = rs.getInt("completed") == 1 ? "[X]" : "[ ]";
                        history.add(status + " " + name + " - Assigned to: " + (person != null ? person : "Unassigned"));
                    } else {
                        history.add(name + " - Paid by: " + person);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public String generateReport(int roomId) {
        StringBuilder report = new StringBuilder();
        report.append("=== RoomShare Reports & Statistics ===\n\n");
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT COUNT(*) as total, SUM(CASE WHEN completed = 1 THEN 1 ELSE 0 END) as completed " +
                "FROM chores WHERE room_id = ?");
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int completed = rs.getInt("completed");
                report.append("Chore Statistics:\n");
                report.append("- Total Chores: ").append(total).append("\n");
                report.append("- Completed: ").append(completed).append("\n");
                report.append("- Pending: ").append(total - completed).append("\n\n");
            }
            
            pstmt = conn.prepareStatement(
                "SELECT COUNT(*) as count, SUM(amount) as total FROM expenses WHERE room_id = ?");
            pstmt.setInt(1, roomId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                double total = rs.getDouble("total");
                report.append("Expense Statistics:\n");
                report.append("- Total Expenses: ").append(count).append("\n");
                report.append("- Total Amount: $").append(String.format("%.2f", total)).append("\n");
                if (count > 0) {
                    report.append("- Average per Expense: $").append(String.format("%.2f", total / count)).append("\n");
                }
                report.append("\n");
            }
            
            pstmt = conn.prepareStatement(
                "SELECT r.name, COUNT(c.id) as chore_count FROM roommates r " +
                "LEFT JOIN chores c ON r.id = c.assigned_to_id AND c.completed = 1 " +
                "WHERE r.room_id = ? GROUP BY r.id, r.name");
            pstmt.setInt(1, roomId);
            rs = pstmt.executeQuery();
            report.append("Fairness Metrics:\n");
            while (rs.next()) {
                report.append("- ").append(rs.getString("name")).append(" completed ")
                       .append(rs.getInt("chore_count")).append(" chores\n");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            report.append("Error generating report.");
        }
        
        return report.toString();
    }
}

