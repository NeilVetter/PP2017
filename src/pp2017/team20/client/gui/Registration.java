package pp2017.team20.client.gui;



import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;


public class Registration {
	/**
	 * Die Klasse erstellt ein "Einlog"-Fenster und ruft die Verbindung zur
	 * Datenbank auf
	 * 
	 * @author Hamid, Kirli, 6041663
	 **/

	// Ben�tigte swing Elemente
	public static JFrame window;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnButton;
	private JLabel lblLabel;
	boolean succses = false;
	public String username;
	public String password;
	GamingArea ga;

	public Registration(GamingArea ga) {
		this.ga = ga;
		initialize();

	}

	// Initialisiere das Fenster

	public void initialize() {
		window = new JFrame();
		// Setze groesse des Fenster fest
		window.setBounds(100, 100, 542, 409);
		// Beim verlassen schliessen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);

		// erstellt die Beschriftung "Username" + positionieren
		// setze Farbe, Schriftgroessee, Knopfgroessee
		JLabel lblLogin = new JLabel("Username:");
		lblLogin.setForeground(Color.GRAY);
		lblLogin.setFont(new Font("Euphemia", Font.PLAIN, 15));
		lblLogin.setBounds(211, 136, 90, 32);
		window.getContentPane().add(lblLogin);

		// erstellt die Beschriftung "Password" + positionieren
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Euphemia", Font.PLAIN, 15));
		lblPassword.setForeground(Color.GRAY);
		lblPassword.setBounds(209, 190, 92, 32);
		window.getContentPane().add(lblPassword);

		// erstellt das Feld zum eintragen des Benutzernames
		textField = new JTextField();
		textField.setBounds(308, 136, 171, 31);
		window.getContentPane().add(textField);
		textField.setColumns(10);

		// erstellt das Feld zum eintragen des Passwortes
		passwordField = new JPasswordField();
		passwordField.setBounds(308, 190, 171, 32);
		window.getContentPane().add(passwordField);

		// erstellt einen Knopf f�rs "Login"
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getSource() == btnNewButton) {
					username = textField.getText();
					password = passwordField.getText();
					// speichert den eingegebenen Username und Password.
					// startet die Client Methode "Login"
					ga.login(username, password);

				}

			}

		});

		btnNewButton.setFont(new Font("Vani", Font.PLAIN, 15));
		btnNewButton.setBounds(224, 271, 103, 32);
		window.getContentPane().add(btnNewButton);

		// Erstellt Knopf um sich neu zu Registrieren.
		btnButton = new JButton("Register");
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// �ffnffnet ein zweiten Fenster zum Regestrieren
				RegistrationData regdata = new RegistrationData();
				regdata.setVisible(true);

			}
		});
		btnButton.setFont(new Font("Vani", Font.PLAIN, 15));
		btnButton.setBounds(376, 271, 103, 32);
		window.getContentPane().add(btnButton);

		// Lade ein Hintergrundsbild hoch
		lblLabel = new JLabel("");
		Image img = null;
		try {
			img = ImageIO.read(new File("img//login.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lblLabel.setIcon(new ImageIcon(img));
		lblLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabel.setBounds(27, 15, 266, 110);
		window.getContentPane().add(lblLabel);
	}
}
