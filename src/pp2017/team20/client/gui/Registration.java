package pp2017.team20.client.gui;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import pp2017.team20.server.engine.SqliteConnection;
import pp2017.team20.shared.LogInMessage;

import java.sql.*;

public class Registration {
 /**
  * Die Klasse erstellt ein "Einlog"-Fenster und 
  * ruft die Verbindung zur Datenbank auf
  * 
  *  @author Hamid, Kirli, 6041663 **/
	
	// Ben�tigte swing Elemente
	public JFrame window;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnButton;
	private JLabel lblLabel;
	boolean succses = false;
	public String username;
	public String password;
	GamingArea ga;
	/**
	 * Teste MainMethode
	 * 
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Registration windowTest = new Registration();
					windowTest.window.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	//Ruft die Datenbank auf im SqliteConnection
//	Connection connection = null;
	
	public Registration(GamingArea ga) {
		this.ga = ga;
		initialize();
		//Setzt die "Connection"
		//connection=SqliteConnection.dbConnector();
		
	}

	//Initialisiere das Fenster 
	 
	public void initialize() {
		window = new JFrame();
		//Setze gr��e des Fenster fest
		window.setBounds(100, 100, 542, 409);
		//Beim verlassen schliessen
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		
		//erstellt die Beschriftung "Username" + positionieren
		//setze Farbe, Schriftgr��e, Knopfgr��e
		JLabel lblLogin = new JLabel("Username:");
		lblLogin.setForeground(Color.GRAY);
		lblLogin.setFont(new Font("Euphemia", Font.PLAIN, 15));
		lblLogin.setBounds(211, 136, 90, 32);
		window.getContentPane().add(lblLogin);
		
		//erstellt die Beschriftung "Password" + positionieren 
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
		
		//erstellt das Feld zum eintragen des Passwortes
		passwordField = new JPasswordField();
		passwordField.setBounds(308, 190, 171, 32);
		window.getContentPane().add(passwordField);
		
		//erstellt einen Knopf f�rs "Login"
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnNewButton){
					 username = textField.getText();
					 password = passwordField.getText();
					 ga.login(username,password);
					
				}
					
				
				
				/*Sobald der Benutzer auf den Knopf dr�ckt, 
			  * wird hier der >Benutzername< und das >Passwort<
			  * mit der Datenbank vergliechen. */
				/*try{
					
				 String query="select * from User where username=? and userpw=?";
				 PreparedStatement pst= connection.prepareStatement(query);
				 //Textfeld wird mit Username verbunden
				 pst.setString(1, textField.getText());
				 //Passwortfeld mit Userpw
				 pst.setString(2, passwordField.getText()); 
				 ResultSet rs=pst.executeQuery();
				 int i=0;
				 while(rs.next()){
					 i++;
				 }
				 if(i==1){
					 //Wenn Benutzername und Passwort korrekt und sich in der db. befinden
					 JOptionPane.showMessageDialog(null, "Username and Password is correct");
					 new GamingArea("Mario's Adventure");
					 window.dispose();
					 succses =true;
				 }
				 else{
					 //Wenn Benutzername oder das Passwort falsch sind.
					 // oder Spieler nicht registriert
					 JOptionPane.showMessageDialog(null, "Wrong Username or Password");
				 }
				 rs.close();
				 pst.close();
			 }catch(Exception e){
				 System.out.println( "Fehler" + e);
			 }*/
			}

			public void regclient(){
				String nickname = textField.getText();
				String pwt = passwordField.getText();
				
				try {
					//engine.sendLogInMessage(1,nickname,pwt);
					} catch ( Exception e2){
						e2.printStackTrace();
					}
					
			}
				
			
		
		});
		
		btnNewButton.setFont(new Font("Vani", Font.PLAIN, 15));
		btnNewButton.setBounds(224, 271, 103, 32);
		window.getContentPane().add(btnNewButton);
		
		//Erstellt Knopf um sich neu zu Registrieren.
		btnButton = new JButton("Register");
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// �ffnet ein zweiten Fenster zum Regestrieren
				RegistrationData regdata = new RegistrationData();
				regdata.setVisible(true);
				
			}
		});
		btnButton.setFont(new Font("Vani", Font.PLAIN, 15));
		btnButton.setBounds(376, 271, 103, 32);
		window.getContentPane().add(btnButton);
		
		//Lade ein Hintergrundsbild hoch
	/*	lblLabel = new JLabel("");
		Image img= new ImageIcon(this.getClass().getResource("/menu.png")).getImage();
		lblLabel.setIcon(new ImageIcon(img));
		lblLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabel.setBounds(27, 15, 266, 110);
		window.getContentPane().add(lblLabel);*/
	}
}
