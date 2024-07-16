import java.sql.*;
import java.sql.Connection; //veritabanı bağlantısını temsil eder.
import java.sql.DriverManager; //veritabanı bağlantılarını yönetir.
import java.sql.Statement; //SQL sorgularını çalıştırmak için kullanılır.
import java.sql.SQLException;//SQL işlemleri sırasında oluşabilecek hataları temsil eder.
import java.util.Scanner; // Kullanıcıdan veri almak için kullanılır.
import java.sql.PreparedStatement; //SQL sorgularını verimli bir şekilde çalıştırmak için kullanılır.
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {
    private String kullanici_adi = "root"; //Veritabanına bağlanmak için kullanılan MySQL kullanıcı adı. Varsayılan olarak root kullanıcısıdır çünkü MySQL'in varsayılan yönetici hesabıdır.
    private String parola = "";
    private String db_ismi = "siparis_uygulaması"; // Kullanılacak veritabanının adı.
    private String host =  "localhost"; //Veritabanının barındırıldığı sunucu adresi. localhost yerel bilgisayarı temsil eder.
    private int port = 3306; //MySQL veritabanı sunucusunun dinlediği port numarası.

    private Connection con = null; //Veritabanı bağlantısını temsil eden Connection nesnesi.
    private Statement statement = null;  //SQL sorgularını çalıştırmak için kullanılan Statement nesnesi.
    private PreparedStatement preparedStatement = null;


    public void siparis() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ne işlem yapmak istersiniz ? ");
        String cevap = scanner.nextLine();
        System.out.println("Siparişinizi girin : ");
        String urun = scanner.nextLine();

        String sorgu = "INSERT INTO siparisler (urun) VALUES (?)"; // Kullanıcıdan alınan ürünü siparişler tablosuna ekler.

        try {
            con.setAutoCommit(false); //otomotik olarak veritabanına ekleme yapmaz manuel olarak yapar.
            preparedStatement = con.prepareStatement(sorgu); //con (veritabanı bağlantısı) kullanılarak preparedStatement nesnesi oluşturuluyor ve SQL sorgusu (sorgu) bu nesneye atanıyor.
            preparedStatement.setString(1, urun); // PreparedStatement nesnesine urun adında bir String değer atanmasını sağlar.
            preparedStatement.executeUpdate(); //preparedStatement içindeki sorgu çalıştırılıyor.

            System.out.println("Değişiklikleri kaydetmek istiyor musunuz? (yes/no)");
            String siparis1 = scanner.nextLine();

            if (siparis1.equalsIgnoreCase("yes")) {
                con.commit(); //sorguları kaydet.
                System.out.println("Ürün Veritabanına Eklendi...");
            } else {
                con.rollback(); // sorguları iptal et.
                System.out.println("Ürün Veritabanına Eklenmedi...");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
}
    public void siparisleriGetir() {

        String sorgu = "Select * From siparisler"; //Tüm siparişleri seçmek için bir SQL sorgusu oluşturur.

        try {
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sorgu);// SQL sorgusunu çalıştırır ve sonuç kümesini ResultSet olarak döner. Bu metod genellikle SELECT sorguları için kullanılır.

            while (rs.next()) { //ResultSet içindeki tüm satırları tek tek işler. rs.next() metodu true dönerse, bir sonraki satırın mevcut olduğunu gösterir ve döngü devam eder. Aksi takdirde döngü sona erer.
                //Her bir çalışanın bilgilerini alır.
                int siparis_no= rs.getInt("siparis_no");
                String urun = rs.getString("urun");
                System.out.println("Id: " + siparis_no + " Siparis Adı: " + urun);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);//SQL işlemleri sırasında oluşabilecek hataları yakalar.

        }
    }

    public main() {
        // Veritabanı bağlantı URL'sini oluşturuyoruz.
        // "jbdc:mysql://localhost:3306/demo"
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db_ismi+ "?useUnicode=true&characterEncoding=utf8";


        try {
            // MySQL JDBC sürücüsünü yüklüyoruz.
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException ex) { //JDBC sürücüsü yüklenemezse oluşacak hatayı yakalar ve mesaj yazdırır.
            System.out.println("Driver Bulunamadı....");
        }

        // Veritabanı bağlantısını kuruyoruz.
        try {
            con = DriverManager.getConnection(url, kullanici_adi, parola);//Veritabanı bağlantısını kurar.
            System.out.println("Bağlantı Başarılı...");


        } catch (SQLException ex) { //Bağlantı hatası oluşursa hatayı yakalar ve mesaj yazdırır.
            System.out.println("Bağlantı Başarısız...");

        }

    }


    public static void main(String[] args) {
        main baglanti = new main();
        baglanti.siparis();
        System.out.println("***************************");
        baglanti.siparisleriGetir();
    }

    }
