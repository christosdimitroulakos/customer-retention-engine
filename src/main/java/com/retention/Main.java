package com.retention;

import com.retention.model.Customer;
import com.retention.service.ChurnService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Χρήση: java -jar ... [dbPath] [sqlDir] [csvOutDir]
 * Προεπιλογές: retention.sqlite  sql  powerbi
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String dbPath = args.length > 0 ? args[0] : "retention.sqlite";
        Path sqlDir = Paths.get(args.length > 1 ? args[1] : "sql");
        Path csvDir = Paths.get(args.length > 2 ? args[2] : "powerbi");

        boolean freshDb = !Files.exists(Paths.get(dbPath));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath)) {
            if (freshDb) {
                System.out.println("Νέα βάση: εκτέλεση SQL scripts...");
                runScript(conn, sqlDir.resolve("01_schema.sql"));
                runScript(conn, sqlDir.resolve("02_sample_data.sql"));
                runScript(conn, sqlDir.resolve("03_analytics_views.sql"));
            }

            List<Customer> customers = loadCustomers(conn);
            ChurnService service = new ChurnService();
            customers.forEach(service::analyze);

            int updated = updateCustomers(conn, customers);
            System.out.println("Ενημερώθηκαν " + updated + " πελάτες.");
            customers.forEach(System.out::println);

            Files.createDirectories(csvDir);
            exportQuery(conn, "SELECT * FROM customers", csvDir.resolve("customers.csv"));
            exportQuery(conn, "SELECT * FROM vw_revenue_at_risk", csvDir.resolve("vw_revenue_at_risk.csv"));
            exportQuery(conn, "SELECT * FROM vw_retention_priority ORDER BY priority_rank",
                    csvDir.resolve("vw_retention_priority.csv"));
            System.out.println("CSV exports στο: " + csvDir.toAbsolutePath());
        }
    }

    private static List<Customer> loadCustomers(Connection conn) throws SQLException {
        String sql = "SELECT customer_id, tenure_months, contract_type, internet_service, "
                + "tech_support, monthly_charges FROM customers";
        List<Customer> list = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("customer_id"),
                        rs.getInt("tenure_months"),
                        rs.getString("contract_type"),
                        rs.getString("internet_service"),
                        rs.getString("tech_support"),
                        rs.getDouble("monthly_charges")));
            }
        }
        return list;
    }

    private static int updateCustomers(Connection conn, List<Customer> customers) throws SQLException {
        String sql = "UPDATE customers SET churn_probability = ?, risk_level = ?, predicted_ltv = ? "
                + "WHERE customer_id = ?";
        boolean oldAuto = conn.getAutoCommit();
        conn.setAutoCommit(false);
        int total = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Customer c : customers) {
                ps.setDouble(1, c.getChurnProbability());
                ps.setString(2, c.getRiskLevel());
                ps.setDouble(3, c.getPredictedLtv());
                ps.setString(4, c.getCustomerId());
                total += ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(oldAuto);
        }
        return total;
    }

    private static void runScript(Connection conn, Path file) throws IOException, SQLException {
        String script = Files.readString(file, StandardCharsets.UTF_8);
        try (Statement st = conn.createStatement()) {
            for (String stmt : script.split(";")) {
                StringBuilder sb = new StringBuilder();
                for (String line : stmt.split("\n")) {
                    if (!line.trim().startsWith("--")) sb.append(line).append('\n');
                }
                String clean = sb.toString().trim();
                if (!clean.isEmpty()) st.execute(clean);
            }
        }
    }

    private static void exportQuery(Connection conn, String query, Path out) throws SQLException, IOException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query);
             PrintWriter pw = new PrintWriter(Files.newBufferedWriter(out, StandardCharsets.UTF_8))) {
            ResultSetMetaData md = rs.getMetaData();
            int cols = md.getColumnCount();
            StringBuilder header = new StringBuilder();
            for (int i = 1; i <= cols; i++) {
                if (i > 1) header.append(',');
                header.append(md.getColumnLabel(i));
            }
            pw.println(header);
            while (rs.next()) {
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= cols; i++) {
                    if (i > 1) row.append(',');
                    String v = rs.getString(i);
                    row.append(v == null ? "" : v.contains(",") ? "\"" + v + "\"" : v);
                }
                pw.println(row);
            }
        }
    }
}
