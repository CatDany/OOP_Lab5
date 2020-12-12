package OOP_Lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TTTClientHandler implements Runnable {
    private PrintWriter out;
    private BufferedReader in;
    private TTTServer server;

    private int playerId;

    public TTTClientHandler(int playerId, TTTServer server) {
        this.playerId = playerId;
        this.server = server;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = server.serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println(Messages.YOUR_PLAYER_ID_IS + playerId);
            sendMessage(playerId == server.whoseTurn ? Messages.ITS_YOUR_TURN_NOW : Messages.ITS_YOUR_OPPONENTS_TURN);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith(Messages.IM_GOING)) {
                    int i = Integer.parseInt(inputLine.substring(Messages.IM_GOING.length()).substring(0, 1));
                    int j = Integer.parseInt(inputLine.substring(Messages.IM_GOING.length()).substring(3, 4));
                    server.tryPlay(playerId, i, j);
                }
                System.out.println((playerId == 0 ? "Alice" : "Bob") + " to Server: " + inputLine);
            }

        } catch (IOException t) {}
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}
