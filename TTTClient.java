package OOP_Lab5;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TTTClient implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private Thread thread;
    private String ip;
    private int port;

    public JFrame frame;
    /**
     * 0 = "O"
     * 1 = "X"
     */
    public int playerId;
    public boolean isMyTurn = false;
    /**
     * "-1" = пусто
     * "0" = "O" = playerId
     * "1" = "X" = playerId
     */
    public String[] grid;

    private static final Color CLICKABLE_COLOR = Color.white;
    private static final Color X_COLOR = new Color(	238, 238, 144);
    private static final Color O_COLOR = new Color(144,144,238);
    private static final Color NOT_YOUR_TURN_COLOR = Color.lightGray;

    public TTTClient(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(Messages.WHICH_NUMBER_AM_I);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith(Messages.YOUR_PLAYER_ID_IS)) {
                    playerId = Integer.parseInt(inputLine.substring(Messages.YOUR_PLAYER_ID_IS.length()));
                    updateTitle();
                }
                System.out.println("Server to " + (playerId == 0 ? "Alice" : "Bob") + ": " + inputLine);
                if (inputLine.equals(Messages.ITS_YOUR_TURN_NOW)) {
                    isMyTurn = true;
                    updateWhoseTurn();
                }
                else if (inputLine.equals(Messages.ITS_YOUR_OPPONENTS_TURN)) {
                    isMyTurn = false;
                    updateWhoseTurn();
                }
                else if (inputLine.startsWith(Messages.HERES_WHAT_THE_BOARD_LOOKS_LIKE)) {
                    grid = inputLine.substring(Messages.HERES_WHAT_THE_BOARD_LOOKS_LIKE.length()).split(";");
                    updateGrid();
                }
                else if (inputLine.equals(Messages.YOU_WON)) {
                    showVictory();
                }
                else if (inputLine.equals(Messages.YOU_LOST)) {
                    showDefeat();
                }
                else if (inputLine.equals(Messages.DRAW)) {
                    showDraw();
                }
            }
        } catch (IOException t) {}
    }

    public void startConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        thread = new Thread(this);
        thread.start();
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    private void updateTitle() {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle((playerId == 0 ? "O" : "X") + " - Крестики-Нолики");
        });
    }

    private void updateWhoseTurn() {
        SwingUtilities.invokeLater(() -> {
            for (Component component : frame.getContentPane().getComponents()) {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    if (!isMyTurn) {
                        button.setBackground(NOT_YOUR_TURN_COLOR);
                    } else if (button.getText().isEmpty()) {
                        button.setBackground(CLICKABLE_COLOR);
                    } else if (button.getText().equals("O")) {
                        button.setBackground(O_COLOR);
                    } else if (button.getText().equals("X")) {
                        button.setBackground(X_COLOR);
                    }
                }
            }
            frame.requestFocus();
        });
    }

    private void updateGrid() {
        SwingUtilities.invokeLater(() -> {
            int gridIndex = 0;
            for (Component component : frame.getContentPane().getComponents())
            {
                if (component instanceof JButton) {
                    JButton button = (JButton) component;
                    switch (grid[gridIndex++]) {
                        case "-1":
                            button.setText("");
                            break;
                        case "0":
                            button.setText("O");
                            break;
                        case "1":
                            button.setText("X");
                            break;
                    }
                }
            }
        });
    }

    private void showVictory() {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("ПОБЕДА! " + frame.getTitle());
        });
    }

    private void showDefeat() {
        SwingUtilities.invokeLater(() -> {
            frame.hide();
        });
    }

    private void showDraw() {
        SwingUtilities.invokeLater(() -> {
            frame.setTitle("НИЧЬЯ! " + frame.getTitle());
        });
    }
}