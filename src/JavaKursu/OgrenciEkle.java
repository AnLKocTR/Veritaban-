package JavaKursu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class OgrenciEkle {
    private static final String URL = "jdbc:mysql://localhost:3306/java_kursu";
    private static final String KULLANICI = "root"; // MySQL kullanıcı adınız farklıysa değiştirin.

    public static void main(String[] args) {
        String parola = parolaSor();
        if (parola == null) {
            System.out.println("Parola girişi iptal edildi.");
            return;
        }
        Scanner klavye = new Scanner(System.in);
        String sql = "INSERT INTO ogrenciler (ad, soyad, kayit_tarihi) "
                   + "VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection baglanti = DriverManager.getConnection(URL, KULLANICI, parola);
             PreparedStatement ifade = baglanti.prepareStatement(sql)) {
            System.out.print("Öğrencinin adı: ");
            String ad = klavye.nextLine();
            System.out.print("Öğrencinin soyadı: ");
            String soyad = klavye.nextLine();

            if (ad.isBlank() || soyad.isBlank()) {
                System.out.println("Ad ve soyad boş bırakılamaz.");
                return;
            }

            ifade.setString(1, ad);
            ifade.setString(2, soyad);
            int eklenenSatir = ifade.executeUpdate();
            System.out.println(eklenenSatir + " öğrenci kaydı eklendi.");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1049) {
                System.out.println("'java_kursu' veritabanı bu MySQL sunucusunda bulunamadı.");
                System.out.println("Kod şu adrese bağlanmayı denedi: " + URL);
                System.out.println("Workbench'te aynı sunucuda SHOW DATABASES; komutunu çalıştırın.");
                System.out.println("Listede farklı yazılıyorsa URL'deki veritabanı adını birebir eşleştirin.");
            } else {
                System.out.println("MySQL işlemi başarısız: " + e.getMessage());
                System.out.println("Kullanıcı adını, şifreyi, tablo ve sütun adlarını kontrol edin.");
            }
        }
    }

    private static String parolaSor() {
        JPasswordField parolaAlani = new JPasswordField(20);
        parolaAlani.setEchoChar('\u2022');
        int secim = JOptionPane.showConfirmDialog(
                null,
                parolaAlani,
                "MySQL şifresi",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (secim != JOptionPane.OK_OPTION) {
            return null;
        }
        return new String(parolaAlani.getPassword());
    }
}
