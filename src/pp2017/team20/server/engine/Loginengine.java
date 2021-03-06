package pp2017.team20.server.engine;

import java.sql.*;

import javax.swing.*;

import pp2017.team20.client.gui.Registration;

/** 
 * Diese Klasse �berpr�ft mithilfe der SqLite Datenbank, den Benutzernamen und das Passwort und gibt
 * bei einer korrekten Eingabe einen Boolean zur�ck.
 * 
 *  @author Hamid, Kirli 6041663*/
public class Loginengine {
	
	
	public static boolean logIn( String username , String password)  {
		boolean succses = true;
		Connection connection = null;
		//Setzt die "Connection"
				connection=SqliteConnection.dbConnector();
		try { 
		String query="select * from User where username=? and userpw=?";
		 PreparedStatement pst= connection.prepareStatement(query);
		 //Textfeld wird mit Username verbunden
		 pst.setString(1, username);
		 //Passwortfeld mit Userpw
		 pst.setString(2, password); 
		 ResultSet rs=pst.executeQuery();
		 int i=0;
		 while(rs.next()){
			 i++;}
			 
			
		 
		 if(i==1){
			 //Wenn Benutzername und Passwort korrekt und sich in der db. befinden
			
			 
			 succses = true;
			//Schliesst das Registrationsfenster
			 try{
			 Registration.window.dispose();
			 }catch(Exception e){
				 
			 }
		 }
		 else{
			 //Wenn Benutzername oder das Passwort falsch sind
			 // oder Spieler nicht registriert ist
		
			 JOptionPane.showMessageDialog(null, "Wrong Username or Password");
		succses= false;
		
		 } 
		 rs.close();
		pst.close();
		 }catch (Exception e){
			 e.printStackTrace();
		 }
		
		return succses;
	

		}
}

	

