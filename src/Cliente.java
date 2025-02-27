import java.io.*;
import java.net.*;
import java.util.Scanner;

class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println(reader.readLine());
        String mensajeServidor;

        while (true) {
            mensajeServidor = reader.readLine();
            if (mensajeServidor == null || mensajeServidor.equals("FIN")) {
                System.out.println("El juego ha terminado.");
                break;
            }
            System.out.println(mensajeServidor);
            if (mensajeServidor.contains("Ingresa un número")) {
                writer.println(scanner.nextLine());
            }
        }
        socket.close();
        scanner.close();
    }
}