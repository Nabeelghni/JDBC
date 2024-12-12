import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Supermarket {
    private static Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LandbouwMart";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "Nabeel1818";

    @SuppressWarnings("unused")
    private static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }
    public static void main(String[] args) {
        if (!login()) {
            System.out.println("Login gagal. Silahkan Coba Lagi.");
            return;
        }
    
        while (true) {
            String supermarketName = "LandbouwMart"; 
            displayWelcome(supermarketName);
            System.out.print("Nama Kasir: ");
            String kasir = scanner.nextLine();
            Transaksi transaksi = getTransactionDetails(kasir);
            transaksi.tampilkanTransaksi(kasir);
    
            // Tambahkan transaksi ke database
            JDBC.createTransaksi(transaksi); 
    
            System.out.print("Apakah ingin melakukan transaksi lagi? (y/n): ");
            String jawaban = scanner.nextLine();
            if (jawaban.equalsIgnoreCase("n")) {
                break;
            }
        }
    }
    
    // Fungsi untuk menambahkan barang ke database
    public static void addBarangToDatabase(Barang barang) {
        JDBC.createBarang(barang);
    }

    // Fungsi untuk menampilkan semua barang dari database
    public static void displayBarangFromDatabase() {
        List<Barang> barangList = JDBC.readBarang();
        for (Barang barang : barangList) {
            System.out.println("Kode Barang: " + barang.getKodeBarang());
            System.out.println("Nama Barang: " + barang.getNamaBarang());
            System.out.println("Harga Barang: " + barang.getHargaBarang());
        }
    }

    // Fungsi untuk menampilkan semua transaksi dari database
    public static void displayTransaksiFromDatabase() {
        List<Transaksi> transaksiList = JDBC.readTransaksi();
        for (Transaksi transaksi : transaksiList) {
            System.out.println("No. Faktur: " + transaksi.getnoFaktur());
            System.out.println("Kode Barang: " + transaksi.getkodeBarang());
            System.out.println("Nama Barang: " + transaksi.getnamaBarang());
            System.out.println("Harga Barang: " + transaksi.gethargaBarang());
            System.out.println("Jumlah Beli: " + transaksi.getJumlah());
        }
    }    
    public static String generateCaptcha(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            captcha.append(chars.charAt(random.nextInt(chars.length())));
        }

        return captcha.toString();
    }

    private static Transaksi getTransactionDetails(String namaKasir) {
        System.out.print("No. Faktur: ");
        String noFaktur = scanner.nextLine();
        System.out.print("Kode Barang: ");
        String kodeBarang = scanner.nextLine();
        System.out.print("Nama Barang: ");
        String namaBarang = scanner.nextLine();
        System.out.print("Harga Barang: ");
        double hargaBarang = scanner.nextDouble();
        System.out.print("Jumlah Beli: ");
        int jumlahBeli = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character after nextInt()

        return new Transaksi(noFaktur, kodeBarang, namaBarang, hargaBarang, jumlahBeli, namaKasir);
    }

    private static void displayWelcome(String message) {
        System.out.println("                    Selamat datang             ");
        System.out.println("                     "+message                  );
        System.out.println("                                               ");
    }

    private static boolean login() {
        System.out.println("+-----------------------------------------------------+");
        System.out.println("                        Login                          ");
        System.out.println("+-----------------------------------------------------+");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String captcha = generateCaptcha(6);
        System.out.println("Captcha: " + captcha);
        System.out.print("Masukkan Captcha: ");
        String inputCaptcha = scanner.nextLine();
        System.out.println("+----------------------------------------------------+");

        if (username.equals("admin") && password.equals("admin") && inputCaptcha.equals(captcha)) {
            System.out.println("                    Login berhasil!                  ");
            System.out.println("+-----------------------------------------------------+");
            return true;
        } else {
            System.out.println("Login gagal. Silakan coba lagi.");
            System.out.println("+-----------------------------------------------------+");
            return false;
        }
    }

    // Kelas Barang sebagai public inner class
    public static class Barang {
        protected String kodeBarang;
        protected String namaBarang;
        protected double hargaBarang;

        // Konstruktor untuk kelas Barang
        public Barang(String kodeBarang, String namaBarang, double hargaBarang) {
            if (hargaBarang <= 0) {
                throw new IllegalArgumentException("Harga barang harus lebih dari 0.");
            }
            this.kodeBarang = kodeBarang;
            this.namaBarang = namaBarang;
            this.hargaBarang = hargaBarang;
        }

        // Getter dan setter untuk kodeBarang, namaBarang, hargaBarang
        public String getKodeBarang() {
            return kodeBarang;
        }

        public void setKodeBarang(String kodeBarang) {
            this.kodeBarang = kodeBarang;
        }

        public String getNamaBarang() {
            return namaBarang;
        }

        public void setNamaBarang(String namaBarang) {
            this.namaBarang = namaBarang;
        }

        public double getHargaBarang() {
            return hargaBarang;
        }

        public void setHargaBarang(double hargaBarang) {
            if (hargaBarang <= 0) {
                throw new IllegalArgumentException("Harga barang harus lebih dari 0.");
            }
            this.hargaBarang = hargaBarang;
        }
    }

    // Kelas Transaksi sebagai public inner class
    public static class Transaksi {
        private String noFaktur;
        private String kodeBarang;
        private String namaBarang;
        private double hargaBarang;
        private int jumlahBeli;
        private double total;
        private String namaKasir;

        public Transaksi(String noFaktur, String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli, String namaKasir) {
            this.noFaktur = noFaktur;
            this.kodeBarang = kodeBarang;
            this.namaBarang = namaBarang;
            this.hargaBarang = hargaBarang;
            this.jumlahBeli = jumlahBeli;
            this.namaKasir = namaKasir;
        }
        public String getnoFaktur() {
            return noFaktur;
        }
        public String getnamaBarang() {
            return namaBarang;
        }
        public String getkodeBarang() {
            return kodeBarang;
        }
        public double gethargaBarang() {
            return hargaBarang;
        }
        public int getJumlah() {
            return jumlahBeli;
        }
        public double getTotal() {
            return total;
        }
        public String getNamaKasir() {
            return namaKasir;
        }

        

        public double hitungTotal() {
            return hargaBarang * jumlahBeli;
        }

        public void tampilkanTransaksi(String kasir) {
            double total = hitungTotal();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            System.out.println("+----------------------------------------------------+");
            System.out.println("                   Selamat Datang                      ");
            System.out.println("                   -LandbouwMart-                      ");
            System.out.println("+----------------------------------------------------+");
            System.out.println("Tanggal dan Waktu : " + formatter.format(date));
            System.out.println("+----------------------------------------------------+");
            System.out.println("No. Faktur      : " + noFaktur);
            System.out.println("Kode Barang     : " + kodeBarang);
            System.out.println("Nama Barang     : " + namaBarang);
            System.out.println("Harga Barang    : " + hargaBarang);
            System.out.println("Jumlah Beli     : " + jumlahBeli);
            System.out.println("TOTAL           : " + total);
            System.out.println("+----------------------------------------------------+");
            System.out.println("Kasir : " + kasir);
            System.out.println("+----------------------------------------------------+");
        }


    }
}