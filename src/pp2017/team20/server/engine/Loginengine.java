package pp2017.team20.server.engine;

import java.sql.*;

import javax.swing.*;

public class Loginengine {
	
	public static boolean logIn( String username , String password) throws Exception {
		boolean succses = false;
		Connection connection = null;
		 String query="select * from User where username=? and userpw=?";
		 PreparedStatement pst= connection.prepareStatement(query);
		 //Textfeld wird mit Username verbunden
		 pst.setString(1, username);
		 //Passwortfeld mit Userpw
		 pst.setString(2, password); 
		 ResultSet rs=pst.executeQuery();
		 int i=0;
		 while(rs.next()){
			 i++;
		 }
		 if(i==1){
			 //Wenn Benutzername und Passwort korrekt und sich in der db. befinden
			 JOptionPane.showMessageDialog(null, "Username and Password is correct");
			 succses = true;
			return succses;
			
		 }
		 else{
			 //Wenn Benutzername oder das Passwort falsch sind.
			 // oder Spieler nicht registriert
		
			 JOptionPane.showMessageDialog(null, "Wrong Username or Password");
		succses= false;
		 return succses;
		 }
		// rs.close();
		// pst.close();
	
//	return false;
	}
}

	

