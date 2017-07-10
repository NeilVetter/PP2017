package pp2017.team20.client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pp2017.team20.server.engine.SqliteConnection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

/** 
 * Erstellt ein Fenster um einen Spieler neu zu Registrieren 
 * 
 * @author Hamid , Kirli , 6041663 
 * 
 * **/

public class RegistrationData extends JFrame {

	//Ruft ben�tigte swing Elemente auf.
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JButton btnNewButton;

	/**
	 * Test MainMethode
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationData frame = new RegistrationData();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});} */
	

	/**
	 * Erstellt das Fenster
	 */
	Connection connection = null;
	public RegistrationData() {
		connection= SqliteConnection.dbConnector();
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Setzt Feld zum eintragen vom Benutzernamen
		JLabel lblNewLabel = new JLabel("Username:");
		lblNewLabel.setForeground(Color.GRAY);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(40, 75, 79, 14);
		contentPane.add(lblNewLabel);
		
		// Setze Feld zum eintragen vom Passwort
		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setForeground(Color.GRAY);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(40, 118, 79, 19);
		contentPane.add(lblNewLabel_1);
		
		// Setze zweites Feld f�rs Passwort
		JLabel lblNewLabel_2 = new JLabel("Repeat PW:");
		lblNewLabel_2.setForeground(Color.GRAY);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(40, 160, 79, 19);
		contentPane.add(lblNewLabel_2);
		
		//Erstellt Feld zum eintragen des Benutzernamen
		textField = new JTextField();
		textField.setBounds(149, 74, 150, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		//Erstelle Feld f�r das Passwort
		passwordField = new JPasswordField();
		passwordField.setBounds(149, 119, 150, 20);
		contentPane.add(passwordField);
		//Erstelle 2.Feld f�r Passwort
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(149, 159, 150, 20);
		contentPane.add(passwordField_1);
		
		// Erstellt einen Knopf f�r die Registrierung
		btnNewButton = new JButton("Join the Game");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* ruft die Datenbank auf, testet ob die >Benutzername<
				*  und das >Passwort< schon vergeben ist. Wenn nicht speichere
				*  neuen >Benutzer< und >Passwort< in die Datenbank 
				*/
				try{
				String query="select * from User where username=? and userpw=?";
				 PreparedStatement pst= connection.prepareStatement(query);
				 pst.setString(1, textField.getText());
				 pst.setString(2, passwordField.getText());
				 
				 ResultSet rs=pst.executeQuery();
				 int i=0;
				 while(rs.next()){
					 i++;
				 }
				 if(i==1){
					 //Wenn Benutername unf Password schon vergeben 
					 //soll dem Spieler diese Nachricht ausgegeben werden.
					 JOptionPane.showMessageDialog(null, "Username and Password is take");
				 }
				 else{
					 // Wenn Benuztername noch nicht vergeben, soll der neue Spieler
					 // in der Datenbank angelegt werden.
					 String quere2="insert into User (username,userpw) values (?,?)";
					 PreparedStatement pst2=connection.prepareStatement(quere2);
					 
					 pst2.setString(1,textField.getText());
					 pst2.setString(2,passwordField.getText());
					
					 pst2.execute();
					 
					 JOptionPane.showMessageDialog(null, "Welcome new player");
					 
				 }
				 
				 pst.close();
			 }catch(Exception e1){
				 System.out.println( "Fehler " + e1);
			 }
			}
		});
		btnNewButton.setFont(new Font("Vani", Font.PLAIN, 15));
		btnNewButton.setBounds(149, 205, 150, 25);
		contentPane.add(btnNewButton);
		

	}
}
