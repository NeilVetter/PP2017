package pp2017.team20.client.gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pp2017.team20.client.gui.GamingArea;

/**
 * Klasse in der der Chat des Spielfensters mit einem Eingabe- und Ausgabefenster erstellt wird
 * 
 * @author Heck, Liz, 5991099
 * 
 */

public class Chat extends JPanel {

	private static final long serialVersionUID = 1L;

	private GamingArea window;
	public JButton send;
	private JTextField chatInput;
	public JTextArea chatOutput;

	public int clientID;

	/**
	 * Hier werden das Eingabefenster, das Ausgabefenster und der Button zum
	 * Versenden erstellt, mithilfe von GridBagLayout
	 * 
	 * @author Heck, Liz, 5991099
	 */
	public Chat(GamingArea window) {
		this.window = window;
		this.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.GRAY);
		southPanel.setForeground(Color.WHITE);
		southPanel.setLayout(new GridBagLayout());

		// Eingabefenster wird erstellt
		chatInput = new JTextField(10);
		chatInput.setBackground(Color.WHITE);
		chatInput.setForeground(Color.BLACK);
		chatInput.requestFocusInWindow();

		// 'Sende Nachricht' Button wird erstellt
		send = new JButton("Send Message");
		send.setBackground(Color.WHITE);
		send.setForeground(Color.BLACK);
		send.addActionListener(new sendMessageListener());

		// Ausgabefenster wird erstellt
		chatOutput = new JTextArea();
		chatOutput.setEditable(false);
		chatOutput.setSize(100, 30);
		chatOutput.setFont(new Font("Arial", Font.BOLD, 15));
		chatOutput.setBackground(Color.GRAY);
		chatOutput.setForeground(Color.WHITE);
		chatOutput.setLineWrap(true);

		// Systemnachricht dass der Spieler angemeldet ist wird im Chat
		// angezeigt
		chatOutput.setText("     ---Team20 ist angemeldet---" + "\n" + "\n");

		// scrollbares Ausgabefenster wird in die Mitte hinzugefuegt
		this.add(new JScrollPane(chatOutput), BorderLayout.CENTER);

		// Layout des Eingabefensters wird designt
		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 300.0D;
		left.weighty = 1.0D;

		// Layout des 'Send Message' Button wird designt
		GridBagConstraints below = new GridBagConstraints();
		below.insets = new Insets(0, 10, 0, 0);
		below.anchor = GridBagConstraints.LINE_START;
		below.fill = GridBagConstraints.NONE;
		below.weightx = 1.0D;
		below.weighty = 1.0D;

		southPanel.add(chatInput, left);
		southPanel.add(send, below);

		// Eingabefenster und Senden Button werden unten in das Fenster
		// eingefuegt
		this.add(BorderLayout.SOUTH, southPanel);
	}

	/**
	 * Hier wird die Nachricht aus dem Eingabefenster gesendet und im
	 * Ausgabefenster angezeigt
	 * 
	 * @author Heck, Liz, 5991099
	 *
	 */
	public class sendMessageListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			window.engine.sendChatMessage(clientID, chatInput.getText());
		}
	}

}
