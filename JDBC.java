// JDBC.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JDBC {

    // Fungsi untuk mendapatkan koneksi ke database
    private static Connection getConnection() {
        try {
            String url = "jdbc:postgresql://localhost:5432/LandbouwMart";
            String username = "postgres";
            String password = "Nabeel1818";
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Koneksi ke database berhasil!");
            return connection;
        } catch (SQLException e) {
            System.out.println("Error saat menghubungkan ke database: " + e.getMessage());
            return null;
        }
    }

    // Fungsi untuk melakukan operasi CREATE untuk Barang
public static void createBarang(Supermarket.Barang barang) {
    String sql = "INSERT INTO products (kode_barang, nama_barang, harga_barang) VALUES (?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, barang.getKodeBarang());
        preparedStatement.setString(2, barang.getNamaBarang());
        preparedStatement.setDouble(3, barang.getHargaBarang());
        preparedStatement.executeUpdate();
        System.out.println("Produk berhasil ditambahkan.");
    } catch (SQLException e) {
        System.out.println("Error saat menambah produk: " + e.getMessage());
    }
    }

    // Fungsi untuk melakukan operasi READ untuk Barang
    public static List<Supermarket.Barang> readBarang() {
        List<Supermarket.Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Supermarket.Barang barang = new Supermarket.Barang(
                        resultSet.getString("kode_barang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getDouble("harga_barang")
                );
                barangList.add(barang);
            }
        } catch (SQLException e) {
            System.out.println("Error saat membaca produk: " + e.getMessage());
        }
        return barangList;
    }

    // Fungsi untuk melakukan operasi UPDATE untuk Barang
    public static void updateBarang(Supermarket.Barang barang) {
        String sql = "UPDATE products SET nama_barang = ?, harga_barang = ? WHERE kode_barang = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, barang.getNamaBarang());
            preparedStatement.setDouble(2, barang.getHargaBarang());
            preparedStatement.setString(3, barang.getKodeBarang());
            preparedStatement.executeUpdate();
            System.out.println("Produk berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("Error saat memperbarui produk: " + e.getMessage());
        }
    }

    // Fungsi untuk melakukan operasi DELETE untuk Barang
    public static void deleteBarang(String kodeBarang) {
        String sql = "DELETE FROM products WHERE kode_barang = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, kodeBarang);
            preparedStatement.executeUpdate();
            System.out.println("Produk berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Error saat menghapus produk: " + e.getMessage());
        }
    }

    // Fungsi untuk melakukan operasi CREATE untuk Transaksi
    public static void createTransaksi(Supermarket.Transaksi transaksi) {
        String sql = "INSERT INTO transactions (no_faktur, kode_barang, nama_barang, harga_barang, jumlah_beli, total, nama_kasir, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaksi.getnoFaktur());
            preparedStatement.setString(2, transaksi.getkodeBarang());
            preparedStatement.setString(3, transaksi.getnamaBarang());
            preparedStatement.setDouble(4, transaksi.gethargaBarang());
            preparedStatement.setInt(5, transaksi.getJumlah());
            preparedStatement.setDouble(6, transaksi.hitungTotal()); 
            preparedStatement.setString(7, transaksi.getNamaKasir()); 
            preparedStatement.executeUpdate();
            System.out.println("Transaksi berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("Error saat menambah transaksi: " + e.getMessage());
        }
    }

    // Fungsi untuk membaca semua transaksi
    public static List<Supermarket.Transaksi> readTransaksi() {
        List<Supermarket.Transaksi> transaksiList = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Supermarket.Transaksi transaksi = new Supermarket.Transaksi(
                        resultSet.getString("no_faktur"),
                        resultSet.getString("kode_barang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getDouble("harga_barang"),
                        resultSet.getInt("jumlah_beli"),
                        resultSet.getString("nama_kasir")
                );
                transaksiList.add(transaksi);
            }
        } catch (SQLException e) {
            System.out.println("Error saat membaca transaksi: " + e.getMessage());
        }
        return transaksiList;
    }

    // Fungsi untuk memperbarui transaksi
    public static void updateTransaksi(Supermarket.Transaksi transaksi) {
        String sql = "UPDATE transactions SET nama_barang = ?, harga_barang = ?, jumlah_beli = ? WHERE no_faktur = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transaksi.getnamaBarang());
            preparedStatement.setDouble(2, transaksi.gethargaBarang());
            preparedStatement.setInt(3, transaksi.getJumlah());
            preparedStatement.setString(4, transaksi.getnoFaktur());
            preparedStatement.executeUpdate();
            System.out.println("Transaksi berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("Error saat memperbarui transaksi: " + e.getMessage());
        }
    }

    // Fungsi untuk menghapus transaksi
    public static void deleteTransaksi(String noFaktur) {
        String sql = "DELETE FROM transactions WHERE no_faktur = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, noFaktur);
            preparedStatement.executeUpdate();
            System.out.println("Transaksi berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("Error saat menghapus transaksi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Supermarket.Barang barang = new Supermarket.Barang("B001", "Barang 1", 10000.0);
        createBarang(barang);

        Supermarket.Transaksi transaksi = new Supermarket.Transaksi("T001", "B001", "Barang 1", 10000.0, 2, "Kasir 1");
        createTransaksi(transaksi);
    }
}