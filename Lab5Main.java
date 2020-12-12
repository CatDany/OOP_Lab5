package OOP_Lab5;

import javax.swing.*;
import java.awt.*;

public class Lab5Main {

    public static JFrame frameServer;
    public static JFrame frameAlice;
    public static JFrame frameBob;

    public static TTTServer server;
    public static TTTClient clientAlice;
    public static TTTClient clientBob;

    public static void main(String[] args) {
        prepareFrames();

        server = new TTTServer();
        server.start(8881);

        clientAlice = new TTTClient(frameAlice);
        clientAlice.startConnection("localhost", 8881);

        clientBob = new TTTClient(frameBob);
        clientBob.startConnection("localhost", 8881);

        addButtons();
    }

    public static void prepareFrames()
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        frameAlice = new JFrame();
        frameAlice.setContentPane(new JPanel());
        frameAlice.setTitle("Клиент. Крестики-Нолики");
        frameAlice.setSize(360, 360);
        frameAlice.setResizable(false);
        frameAlice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameAlice.setLocation(dim.width/2-frameAlice.getSize().width/2 - 180, dim.height/2-frameAlice.getSize().height/2);
        frameAlice.getContentPane().setLayout(new GridLayout(3, 3));
        frameAlice.setVisible(true);

        frameBob = new JFrame();
        frameBob.setContentPane(new JPanel());
        frameBob.setTitle("Клиент. Крестики-Нолики");
        frameBob.setSize(360, 360);
        frameBob.setResizable(false);
        frameBob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameBob.setLocation(dim.width/2-frameBob.getSize().width/2 + 180, dim.height/2-frameBob.getSize().height/2);
        frameBob.getContentPane().setLayout(new GridLayout(3, 3));
        frameBob.setVisible(true);
    }

    public static void addButtons()
    {
        JFrame[] frames = new JFrame[] {frameAlice, frameBob};
        TTTClient[] clients = new TTTClient[] {clientAlice, clientBob};
        for (int f = 0; f < 2; f++) {
            JFrame frame = frames[f];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    {
                        JButton button = new JButton();
                        button.addActionListener(new ButtonHandler(clients[f], i, j));
                        frame.getContentPane().add(button);
                    }
                }
            }
            frame.repaint();
        }
    }
}
