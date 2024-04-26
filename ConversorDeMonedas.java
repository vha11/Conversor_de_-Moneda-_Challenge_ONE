import java.util.Scanner;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

public class Main {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/1c982f75eb58662e366c742b/latest/USD";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        mostrarMenu(); //
    }

    public static void mostrarMenu() {
        System.out.println("Bienvenido(a) al Conversor de Moneda");
        System.out.println("                         MENU");
        System.out.println("1. Dólar                 -> Peso argentino");
        System.out.println("2. Dólar                 -> Real brasileño");
        System.out.println("3. Dólar                 -> Peso Colombiano");
        System.out.println("4. Peso Argentino        -> Dólar");
        System.out.println("5. Real brasileño        -> Dólar");
        System.out.println("6. Peso Colombiano       -> Dólar");
        System.out.println("7. Salir");
        System.out.println("\nIntroduzca el número de la opción:");

        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Introduzca la cantidad a converter: ");
        double cantidad = scanner.nextInt();


        switch (opcion) {
            case 1:
                convertirMoneda(cantidad,"USD", "ARS");
                break;
            case 2:
                convertirMoneda(cantidad,"USD", "BRL");
                break;
            case 3:
                convertirMoneda(cantidad,"USD", "COP");
                break;
            case 4:
                convertirMoneda(cantidad,"ARS", "USD");
                break;
            case 5:
                convertirMoneda(cantidad,"BRL", "USD");
                break;
            case 6:
                convertirMoneda(cantidad,"COP", "USD");
                break;
            case 7:
                System.out.println("Saliendo del Conversor de Moneda. ¡Hasta luego!");
                System.exit(0);
                break;
            default:
                System.out.println("Opción inválida. Por favor, seleccione una opción válida.");
                mostrarMenu();
        }
    }

    public static void convertirMoneda(double cantidad, String monedaOrigen, String monedaDestino) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

            double tasaCambioDestino = jsonObject.getAsJsonObject("conversion_rates").get(monedaDestino).getAsDouble();
            double tasaCambioOrigen = jsonObject.getAsJsonObject("conversion_rates").get(monedaOrigen).getAsDouble();

            double resultado = (cantidad * tasaCambioDestino)/ tasaCambioOrigen;
            String resultadoFormateado = String.format("%.3f", resultado);

            System.out.println(cantidad + " " + monedaOrigen + " equivale a " + resultadoFormateado + " " + monedaDestino);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
