package pp2017.team20.server.engine;
import java.sql.*;
import javax.swing.*;

/** 
 * Erstellt die Verbindung zwischen Java und SQLite(Datenbank) auf 
 * 
 * @author Hamid, Kirli, 6041663 **/

public class SqliteConnection {
	Connection conn=null;
	public static Connection dbConnector(){
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conn=DriverManager.getConnection("jdbc:sqlite:Userdate.db");
			System.out.println("Connection Succsesful");
			return conn;
			
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
	}
}
