package OOP_Lab5;

import java.io.*;
import java.net.ServerSocket;

public class TTTServer {
    public ServerSocket serverSocket;
    private TTTClientHandler[] clients = new TTTClientHandler[2];

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients[0] = new TTTClientHandler(0, this);
            clients[1] = new TTTClientHandler(1, this);
        } catch (IOException t) {}
    }

    /**
     * -1 = пусто
     * 0 = "O" = playerId
     * 1 = "X" = playerId
     */
    public int[][] grid = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
    public int whoseTurn = 0;

    public void tryPlay(int playerId, int i, int j) {
        if (grid[i][j] != -1) {
            clients[playerId].sendMessage(Messages.NUH_UH_ALREADY_TAKEN);
        }
        else if (playerId != whoseTurn) {
            clients[playerId].sendMessage(Messages.NOPE_NOT_YOUR_TURN);
        }
        else {
            grid[i][j] = playerId;

            if (checkGameWon(playerId, i, j)) {
                clients[playerId].sendMessage(Messages.YOU_WON);
                clients[(playerId + 1) % 2].sendMessage(Messages.YOU_LOST);
            }
            else if (checkDraw(playerId, i, j)) {
                clients[0].sendMessage(Messages.DRAW);
                clients[1].sendMessage(Messages.DRAW);
            }
            else {
                whoseTurn = (whoseTurn == 0 ? 1 : 0);
            }
            updateBothPlayers();
        }
    }

    public boolean checkGameWon(int playerId, int x, int y) {
        // check columns
        for (int i = 0; i < 3; i++) {
            if (grid[x][i] != playerId)
                break;
            if (i == 2) {
                return true;
            }
        }

        // check rows
        for (int i = 0; i < 3; i++) {
            if (grid[i][y] != playerId)
                break;
            if (i == 2) {
                return true;
            }
        }

        // check diagonal
        if (x == y) {
            for(int i = 0; i < 3; i++){
                if (grid[i][i] != playerId)
                    break;
                if (i == 2) {
                    return true;
                }
            }
        }

        // check anti-diagonal
        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (grid[i][2-i] != playerId)
                    break;
                if(i == 2){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkDraw(int playerId, int x, int y) {
        i: for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == -1)
                    break i;
            }
            if (i == 2) {
                return true;
            }
        }
        return false;
    }

    public void updateBothPlayers() {
        for (int p = 0; p < 2; p++) {
            String gridSerialized = "";
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    gridSerialized += grid[i][j] + ";";
                }
            }
            clients[p].sendMessage("Here's what the board looks like: " + gridSerialized);
            boolean theirTurn = (p == whoseTurn);
            clients[p].sendMessage(theirTurn ? "It's your turn now" : "It's your opponent's turn");
        }
    }
}