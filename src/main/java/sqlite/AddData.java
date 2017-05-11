package sqlite;
import pi4led.controller.BluetoothListener;
import pi4led.controller.SensorController;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by Gebruiker on 02/05/2017.
 */
public class AddData extends SensorController {

  //  public class InsertApp {

        /**
         * Connect to the test.db database
         *
         * @return the Connection object
         */
        private Connection connect() {
            // SQLite connection string
            String url = "jdbc:sqlite:C://sqlite/db/test.db";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return conn;
        }


        public void insert(String name, String age, String time, double bpm) {
            String sql = "INSERT INTO warehouses(NAME,AGE,DATA,BPM) VALUES(?,?,?,?)";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, age);
                pstmt.setString(3, time);
                pstmt.setDouble(4, bpm);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        public void main(String args[]) {


            String name = null;
            String age = null;
            String date = null;
        /*
        name,age,data
        should be got from  phone app
        or somewhere

         */


            SensorController S = new SensorController();
            BluetoothListener bl= new BluetoothListener();
            double bpm = S.value();



            AddData app = new AddData();
            // insert three new rows
            app.insert(name, age, date, bpm);












            /*Connection c = null;
    Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:test.db");
                c.setAutoCommit(false);
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                String sql = "INSERT INTO COMPANY (ID,NAME,AGE,DATA,BPM) " +
                        "VALUES (1, 'Paul', 32, 'California', v);";
                stmt.executeUpdate(sql);


                stmt.executeUpdate(sql);

                stmt.close();
                c.commit();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Records created successfully");
        }*/
        }
    }


