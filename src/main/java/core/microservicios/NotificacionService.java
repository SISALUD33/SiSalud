package core.microservicios;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NotificacionService {

    public static boolean enviarSMS(String numero, String mensaje) {
        try {
            if (numero == null || numero.trim().isEmpty()) {
                System.out.println("Número vacío o nulo");
                return false;
            }

            // Limpia el número y deja solo dígitos
            String soloDigitos = numero.replaceAll("\\D", ""); 


            // Validamos que queden exactamente 10 dígitos
            if (soloDigitos.length() != 10) {
                System.out.println("Número inválido: debe tener 10 dígitos. Recibido: " + soloDigitos);
                return false;
            }

            String numeroFinal = "+57" + soloDigitos;

            String url = "https://api.callmebot.com/sms.php?phone="
                    + numeroFinal
                    + "&text="
                    + URLEncoder.encode(mensaje, "UTF-8");

            URL endpoint = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
            conn.setRequestMethod("GET");

            int response = conn.getResponseCode();
            System.out.println("Notificación SMS a " + numeroFinal + " → HTTP " + response);

            return response >= 200 && response < 300;

        } catch (Exception e) {
            System.out.println("Error enviando SMS: " + e.getMessage());
            return false;
        }
    }
}
