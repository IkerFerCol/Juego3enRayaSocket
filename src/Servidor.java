import java.io.*;
import java.net.*;
import java.util.*;

class Servidor {
    private static char[][] tablero = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
    };
    private static Socket jugador1, jugador2;
    private static BufferedReader reader1, reader2;
    private static PrintWriter writer1, writer2;
    private static char jugadorActual = 'X';

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Esperando jugadores...");

        jugador1 = serverSocket.accept();
        System.out.println("Jugador 1 conectado");

        jugador2 = serverSocket.accept();
        System.out.println("Jugador 2 conectado");

        writer1 = new PrintWriter(jugador1.getOutputStream(), true);
        reader1 = new BufferedReader(new InputStreamReader(jugador1.getInputStream()));
        writer2 = new PrintWriter(jugador2.getOutputStream(), true);
        reader2 = new BufferedReader(new InputStreamReader(jugador2.getInputStream()));

        writer1.println("Eres el jugador X");
        writer2.println("Eres el jugador O");

        while (true) {
            enviarTablero();
            if (!jugarTurno()) break;
        }

        writer1.println("FIN");
        writer2.println("FIN");
        serverSocket.close();
        jugador1.close();
        jugador2.close();
    }

    private static void enviarTablero() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : tablero) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        writer1.println(sb.toString());
        writer2.println(sb.toString());
    }

    private static boolean jugarTurno() throws IOException {
        PrintWriter writerAct = (jugadorActual == 'X') ? writer1 : writer2;
        BufferedReader readerAct = (jugadorActual == 'X') ? reader1 : reader2;

        int movimiento;
        while (true) {
            writerAct.println("Tu turno. Ingresa un número (1-9):");
            try {
                String input = readerAct.readLine();
                if (input == null) return false;
                movimiento = Integer.parseInt(input) - 1;
                if (movimiento < 0 || movimiento >= 9 || tablero[movimiento / 3][movimiento % 3] == 'X' || tablero[movimiento / 3][movimiento % 3] == 'O') {
                    writerAct.println("Movimiento inválido, intenta de nuevo.");
                    continue;
                }
                break;
            } catch (Exception e) {
                writerAct.println("Entrada inválida, intenta de nuevo.");
            }
        }

        tablero[movimiento / 3][movimiento % 3] = jugadorActual;

        if (saberVictoria()) {
            enviarTablero();
            writer1.println("Jugador " + jugadorActual + " gana!");
            writer2.println("Jugador " + jugadorActual + " gana!");
            return false;
        }

        if (tablaLlena()) {
            enviarTablero();
            writer1.println("Empate!");
            writer2.println("Empate!");
            return false;
        }

        jugadorActual = (jugadorActual == 'X') ? 'O' : 'X';
        return true;
    }

    private static boolean saberVictoria() {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == jugadorActual && tablero[i][1] == jugadorActual && tablero[i][2] == jugadorActual) return true;
            if (tablero[0][i] == jugadorActual && tablero[1][i] == jugadorActual && tablero[2][i] == jugadorActual) return true;
        }
        if (tablero[0][0] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][2] == jugadorActual) return true;
        if (tablero[0][2] == jugadorActual && tablero[1][1] == jugadorActual && tablero[2][0] == jugadorActual) return true;
        return false;
    }

    private static boolean tablaLlena() {
        for (char[] fila : tablero) {
            for (char celda : fila) {
                if (celda != 'X' && celda != 'O') return false;
            }
        }
        return true;
    }
}