package OOP_Lab5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonHandler implements ActionListener {

    TTTClient client;
    int i;
    int j;

    public ButtonHandler(TTTClient client, int i, int j) {
        this.client = client;
        this.i = i;
        this.j = j;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (client != null) {
            client.sendMessage(Messages.IM_GOING + i + "; " + j);
        }
    }
}
