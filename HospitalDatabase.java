// We need to import the java.sql package to use JDBC
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// for reading from the command line
import java.io.*;

// for the login window
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * This class implements a graphical login window and a simple text
 * interface for interacting with the HospitalDatabase table
 */
public class HospitalDatabase implements ActionListener {
    // command line reader 
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private Connection con;
    // user is allowed 3 login attempts
    private int loginAttempts = 0;
    // components of the login window
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JFrame mainFrame;

    /*
     * constructs login window and loads JDBC driver
     */
    public HospitalDatabase() {
        oracleLogin();

        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }
    }

	/**
	 * Run GUI for oracle login.
	 */
	private void oracleLogin() {
		mainFrame = new JFrame("User Login");

        JLabel usernameLabel = new JLabel("Enter username: ");
        JLabel passwordLabel = new JLabel("Enter password: ");

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        passwordField.setEchoChar('*');

        JButton loginButton = new JButton("Log In");

        JPanel contentPane = new JPanel();
        mainFrame.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(usernameLabel, c);
        contentPane.add(usernameLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        contentPane.add(usernameField);

        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        contentPane.add(passwordLabel);

        // place the password field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        contentPane.add(passwordField);

        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        contentPane.add(loginButton);

        // register password field and OK button with action event handler
        passwordField.addActionListener(this);
        loginButton.addActionListener(this);

        // anonymous inner class for closing the window
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        mainFrame.pack();

        // center the frame
        Dimension d = mainFrame.getToolkit().getScreenSize();
        Rectangle r = mainFrame.getBounds();
        mainFrame.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        mainFrame.setVisible(true);

        // place the cursor in the text field for the username
        usernameField.requestFocus();
	}

    /*
     * connects to Oracle database named ug using user supplied username and password
     */
    private boolean connect(String username, String password) {
        String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
        try {
            con = DriverManager.getConnection(connectURL, username, password);
            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            return false;
        }
    }

    /*
     * event handler for login window
     */
    public void actionPerformed(ActionEvent e) {
        if (connect(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
            // if the username and password are valid,
            // remove the login window and display a text menu
            mainFrame.dispose();
            showMenu();
        } else {
            loginAttempts++;
            if (loginAttempts >= 3) {
                mainFrame.dispose();
                System.exit(-1);
            } else {
                // clear the password
                passwordField.setText("");
            }
        }
    }

    /*
     * displays simple text interface
     */
    private void showMenu() {
        int choice = 0;
        boolean quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose an option: \n");
                System.out.print("1.  Output total costs owing for a patient.\n");
                System.out.print("2.  Find doctors with a given specialty.\n");
                System.out.print("3.  Find average cost of commonly prescribed medications.\n");
                System.out.print("4.  Find doctors who have done procedures in every operating room.\n");
                System.out.print("5.  Modify or view an existing table\n");
                System.out.print("6.  Run sql directly (e.g. create or drop table)\n");
                System.out.print("7.  Quit\n>> ");

                boolean cond = true;
                while (cond) {
                    try {
                        choice = Integer.parseInt(in.readLine());
                        if (choice >= 1 && choice <= 7) {
                            cond = false;
                        } else {
                            System.out.println("Please enter an available option!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Your input must contain valid integers only! Try again: ");
                    }
                }

                switch (choice) {
                    case 1:
                        genBill();
                        break;
                    case 2:
                        findSpec();
                        break;
                    case 3:
                        findAveMedCost();
                        break;
                    case 4:
                        findAllORDocs();
                        break;
                    case 5:
                        chooseTable();
                        break;
                    case 6:
                        runSql();
                        break;
                    case 7:
                        quit = true;
                }
            }
            con.close();
            in.close();
            System.out.println("\nGood Bye!\n\n");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("IOException!");
            try {
                con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    /*
     * generate a patient's bill
     */
    private void genBill() {
        try {
            System.out.print("\nEnter patient's ID number: ");
            int pt_id=0;
            boolean cond = true;
            while (cond) {
                try {
                    pt_id = Integer.parseInt(in.readLine());
                    cond = false;
                } catch (NumberFormatException e) {
                    System.out.println("Your input must contain valid integers only! Try again: ");
                }
            }
        
            String query1 = "SELECT Sum(P.cost) FROM Procedures P, Performs F WHERE pt_id='" + pt_id + "' AND P.proc_id=F.proc_id";
            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query1);
            
            int pCost=0;
            while(rs1.next()){
                pCost = rs1.getInt(1);
            }
            System.out.println("Patient "+pt_id+" owes $"+pCost+" for procedures.");
            
        
            String query2 = "SELECT Sum(M.cost) FROM Medications M, Prescribes P WHERE pt_id='" + pt_id + "' AND M.med_id=P.med_id";
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(query2);
            
            int mCost=0;
            while(rs2.next()){
                mCost = rs2.getInt(1);
            }
            System.out.println("Patient "+pt_id+" owes $"+mCost+" for medications."); 

            System.out.println("Detailed costs: ");

            String query3 = "SELECT M.med_name, P.prescribes_date, M.cost FROM Medications M, Prescribes P WHERE M.med_id=P.med_id AND P.pt_id='" + pt_id +"'";
            Statement stmt3 = con.createStatement();
            ResultSet rs3 = stmt3.executeQuery(query3);

            ResultSetMetaData rs3MetaData = rs3.getMetaData();

            // get number of columns
            int numCols3 = rs3MetaData.getColumnCount();

            ArrayList<String> availableColumns3 = new ArrayList<String>();
            ArrayList<Integer> columnWidths3 = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i <= numCols3; i++) {
                int displaySize3 = rs3MetaData.getColumnDisplaySize(i) + 20;
                String columnName3 = rs3MetaData.getColumnName(i);
                availableColumns3.add(columnName3);
                columnWidths3.add(displaySize3);
                System.out.printf("%-" + displaySize3 + "." + displaySize3 + "s", columnName3);
            }
            System.out.println("\n");

            // display rows
            while (rs3.next()) {
                for (int i = 0; i < numCols3; i++) {
                    String columnName3 = availableColumns3.get(i);
                    Integer columnSize3 = columnWidths3.get(i);
                    String rowData3 = rs3.getString(columnName3);
                    System.out.printf("%-" + columnSize3 + "." + columnSize3 + "s", rowData3);
                }
                System.out.println("\n");
            }

            String query4 = "SELECT P.proc_name, F.performs_date, P.cost FROM Procedures P, Performs F WHERE P.proc_id=F.proc_id AND F.pt_id='" + pt_id +"'";
            Statement stmt4 = con.createStatement();
            ResultSet rs4 = stmt4.executeQuery(query4);

            ResultSetMetaData rs4MetaData = rs4.getMetaData();

            // get number of columns
            int numCols4 = rs4MetaData.getColumnCount();

            ArrayList<String> availableColumns4 = new ArrayList<String>();
            ArrayList<Integer> columnWidths4 = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i <= numCols4; i++) {
                int displaySize4 = rs4MetaData.getColumnDisplaySize(i) + 20;
                String columnName4 = rs4MetaData.getColumnName(i);
                availableColumns4.add(columnName4);
                columnWidths4.add(displaySize4);
                System.out.printf("%-" + displaySize4 + "." + displaySize4 + "s", columnName4);
            }
            System.out.println("\n");

            // display rows
            while (rs4.next()) {
                for (int i = 0; i < numCols4; i++) {
                    String columnName4 = availableColumns4.get(i);
                    Integer columnSize4 = columnWidths4.get(i);
                    String rowData4 = rs4.getString(columnName4);
                    System.out.printf("%-" + columnSize4 + "." + columnSize4 + "s", rowData4);
                }
                System.out.println("\n");
            }

            stmt1.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
        } catch(IOException e){
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }   
    }
    
    
    /*
     * find doctors with a given specialty
     */
    private void findSpec() {
        try {
            System.out.print("\nEnter a specialty: ");
            String spec = in.readLine();
        
            String query = "SELECT do_name FROM Doctors WHERE specialization='" + spec + "'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            // get number of columns
            int numCols = rsMetaData.getColumnCount();

            ArrayList<String> availableColumns = new ArrayList<String>();
            ArrayList<Integer> columnWidths = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i <= numCols; i++) {
                int displaySize = rsMetaData.getColumnDisplaySize(i) + 1;
                String columnName = rsMetaData.getColumnName(i);
                availableColumns.add(columnName);
                columnWidths.add(displaySize);
                System.out.printf("%-" + displaySize + "." + displaySize + "s", columnName);
            }
            System.out.println("\n");

            // display rows
            while (rs.next()) {
                for (int i = 0; i < numCols; i++) {
                    String columnName = availableColumns.get(i);
                    Integer columnSize = columnWidths.get(i);
                    String rowData = rs.getString(columnName);
                    System.out.printf("%-" + columnSize + "." + columnSize + "s", rowData);
                }
                System.out.println("\n");
            }
            stmt.close();
        } catch(IOException e){
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }   
    }
    
    
    /*
     * find ave cost of medications prescribed to at least 5 different patients
     */
    private void findAveMedCost() {
        try {
            String query = "SELECT AVG(M.cost) FROM Medications M, Prescribes P WHERE P.med_id=M.med_id GROUP BY P.med_id HAVING 4<COUNT(DISTINCT P.pt_id)";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            int cost=0;
            int counter=0;
            while(rs.next()){
                cost += rs.getInt(1);
                counter++;
            }
            int avg = cost/counter;
            System.out.println("Average cost of medications prescribed to at least five patients is $" + avg);
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }   
    }
    
    
    /*
     * find all doctors who have done procedures in all operating rooms
     */
    private void findAllORDocs() {
        try {
            System.out.println("Doctors who have done procedures in all operating rooms:");
            String query = "SELECT do_name FROM Doctors D WHERE NOT EXISTS "
                    + "(SELECT O.oproom_id FROM OperatingRooms O WHERE NOT EXISTS "
                    + "(SELECT O.oproom_id FROM Performs P WHERE P.do_id=D.do_id AND P.oproom_id=O.oproom_id))";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsMetaData = rs.getMetaData();

            // get number of columns
            int numCols = rsMetaData.getColumnCount();

            ArrayList<String> availableColumns = new ArrayList<String>();
            ArrayList<Integer> columnWidths = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i <= numCols; i++) {
                int displaySize = rsMetaData.getColumnDisplaySize(i) + 1;
                String columnName = rsMetaData.getColumnName(i);
                availableColumns.add(columnName);
                columnWidths.add(displaySize);
                System.out.printf("%-" + displaySize + "." + displaySize + "s", columnName);
            }
            System.out.println("\n");

            // display rows
            while (rs.next()) {
                for (int i = 0; i < numCols; i++) {
                    String columnName = availableColumns.get(i);
                    Integer columnSize = columnWidths.get(i);
                    String rowData = rs.getString(columnName);
                    System.out.printf("%-" + columnSize + "." + columnSize + "s", rowData);
                }
                System.out.println("\n");
            }
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }   
    }

    /*
     * run sql directly
     */
    private void runSql() {
        try {
            System.out.print("\nEnter sql: ");
            String sql = in.readLine();
            PreparedStatement ps = con.prepareCall(sql);
            ps.executeUpdate();
            // commit work
            con.commit();
            ps.close();
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                // undo the action
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    /*
     * choose a table to modify
     */
    private void chooseTable() {
        int choice;
        boolean quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose a table: \n");
                System.out.print("1.  Patients\n");
                System.out.print("2.  Doctors\n");
                System.out.print("3.  Bookings\n");
                System.out.print("4.  OperatingRooms\n");
                System.out.print("5.  BookingReservesOperatingRooms\n");
                System.out.print("6.  RecoveryRooms\n");
                System.out.print("7.  RecoveryRoomBeds\n");
                System.out.print("8.  BookingReservesRecoveryRooms\n");
                System.out.print("9.  Procedures\n");
                System.out.print("10.  BookingForProcedures\n");
                System.out.print("11.  AdmittedTo\n");
                System.out.print("12.  Medications\n");
                System.out.print("13.  Prescribes\n");
                System.out.print("14.  Performs\n");
                System.out.print("15.  Offices\n");
                System.out.print("16.  HasAOffice\n");
                System.out.print("17.  StaffUsers\n");
                System.out.print("18.  PatientUsers\n");
                System.out.print("19.  SUserHasA\n");
                System.out.print("20.  PUserHasA\n");
                System.out.print("21.  Back\n>> ");

                try {
                    choice = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Your input must contain valid integers only! Try again: ");
                    return;
                }

                System.out.println(" ");

                switch (choice) {
                    case 1:
                        modifyTable("Patients");
                        break;
                    case 2:
                        modifyTable("Doctors");
                        break;
                    case 3:
                        modifyTable("Bookings");
                        break;
                    case 4:
                        modifyTable("OperatingRooms");
                        break;
                    case 5:
                        modifyTable("BookingReservesOperatingRooms");
                        break;
                    case 6:
                        modifyTable("RecoveryRooms");
                        break;
                    case 7:
                        modifyTable("RecoveryRoomBeds");
                        break;
                    case 8:
                        modifyTable("BookingReservesRecoveryRooms");
                        break;
                    case 9:
                        modifyTable("Procedures");
                        break;
                    case 10:
                        modifyTable("BookingForProcedures");
                        break;
                    case 11:
                        modifyTable("AdmittedTo");
                        break;
                    case 12:
                        modifyTable("Medications");
                        break;
                    case 13:
                        modifyTable("Prescribes");
                        break;
                    case 14:
                        modifyTable("Performs");
                        break;
                    case 15:
                        modifyTable("Offices");
                        break;
                    case 16:
                        modifyTable("HasAOffice");
                        break;
                    case 17:
                        modifyTable("StaffUsers");
                        break;
                    case 18:
                        modifyTable("PatientUsers");
                        break;
                    case 19:
                        modifyTable("SUserHasA");
                        break;
                    case 20:
                        modifyTable("PUserHasA");
                        break;
                    case 21:
                        quit = true;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
            try {
                con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    /*
     * choose table modifications
     */
    private void modifyTable(String tableName) {
        int choice = 0;
        boolean quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose an option: \n");
                System.out.print("1.  Insert " + tableName + "\n");
                System.out.print("2.  Delete " + tableName + "\n");
                System.out.print("3.  Update " + tableName + "\n");
                System.out.print("4.  Show " + tableName + "\n");
                System.out.print("5.  Back\n>> ");

                boolean cond = true;
                while (cond) {
                    try {
                        choice = Integer.parseInt(in.readLine());
                        cond = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Your input must contain valid integers only! Try again: ");
                    }
                }

                String tblUpperCase = tableName.toUpperCase();
                switch (choice) {
                    case 1:
                        insertIntoTable(tblUpperCase);
                        break;
                    case 2:
                        deleteFromTable(tblUpperCase);
                        break;
                    case 3:
                        updateTableValue(tblUpperCase);
                        break;
                    case 4:
                        showTableData(tblUpperCase);
                        break;
                    case 5:
                        quit = true;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
            try {
                con.close();
                System.exit(-1);
            } catch (SQLException ex) {
                System.out.println("Message: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    /*
     * insert a record into a table
     */
    private void insertIntoTable(String tableName) {
        try {
            // get table columns
            ArrayList<String> availableColumns = getTableColumns(tableName);
            if (availableColumns.size() == 0) {
                System.out.println("No columns found!");
                return;
            }

            // construct the insert sql statement
            String query = "INSERT INTO " + tableName + " VALUES (";
            for (int i = 0; i < availableColumns.size(); i++) {
                query = query.concat("?,");
            }
            query = query.substring(0,query.length()-1).concat(")");

            PreparedStatement preparedStatement = con.prepareStatement(query);
            // get insert values from user
            for (int i = 1; i <= availableColumns.size(); i++) {
                String columnName = availableColumns.get(i-1);
                String columnType = getColumnType(tableName, columnName);

                System.out.print("\n" + columnName + ": ");
                if (columnType.equals("NUMBER")) {
                    boolean cond = true;
                    while (cond) {
                        try {
                            int value = Integer.parseInt(in.readLine());
                            preparedStatement.setInt(i, value);
                            cond = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Your input must contain valid integers only! Try again: ");
                        }
                    }
                } else if (columnType.equals("DATE")) {
                    boolean cond = true;                                       
                    while (cond) {
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm-dd-MM-yyyy");
                        java.util.Date parsed;
                        try {
                            parsed = format.parse(in.readLine());
                            Date sqldate = new Date(parsed.getTime());
                            Time sqltime = new Time(parsed.getTime());
                            preparedStatement.setDate(i, sqldate);
                            preparedStatement.setTime(i, sqltime);
                            cond = false;
                        } catch (ParseException e) {
                            System.out.println("Please enter a valid date!");
                        }
                    }
                } else {
                    String value = in.readLine();
                    preparedStatement.setString(i, value);
                }
            }
            preparedStatement.executeUpdate();
            con.commit();
            preparedStatement.close();
            System.out.println("\nInsert successful!");
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                // undo the insert
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    /*
     * delete a record from a table
     */
    private void deleteFromTable(String tableName) {
        try {

            // find the primary key of table
            String primaryKey = getPrimaryKey(tableName);
            if (primaryKey.equals("")) {
                System.out.println("No primary key found!");
                return;
            }

            // get data type of primary key
            String columnType = getColumnType(tableName, primaryKey);

            // get value of primary key
            PreparedStatement ps = con.prepareStatement("DELETE FROM " + tableName + " WHERE " + primaryKey + " = ?");
            System.out.print("\n" + primaryKey + ": ");

            if (columnType.equals("NUMBER")) {
                boolean cond = true;
                while (cond) {
                    try {
                        int id = Integer.parseInt(in.readLine());
                        ps.setInt(1, id);
                        cond = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Your input must contain valid integers only! Try again: ");
                    }
                }
            } else {
                String id = in.readLine();
                ps.setString(1, id);
            }

            // update row count
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\n" + primaryKey + " does not exist!");
            }
            con.commit();
            ps.close();
            if (rowCount > 0) {
                System.out.println("\nDelete successful!");
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    /*
     * update a value of a given table and field
     */
    private void updateTableValue(String tableName) {
        try {
            // find the primary key of table
            String primaryKey = getPrimaryKey(tableName);
            if (primaryKey.equals("")) {
                System.out.println("No primary key found!");
                return;
            }

            // get data type of primary key
            String pkColumnType = getColumnType(tableName, primaryKey);
            // get table columns
            ArrayList<String> availableColumns = getTableColumns(tableName);
            // remove the primary key, not allowed to update that field
            availableColumns.remove(primaryKey);

            int columnNum = availableColumns.size();
            if (columnNum == 0) {
                System.out.println("No columns found!");
                return;
            }
            // get user input for which column to update
            System.out.print("\n\nPlease choose an option: \n");
            for (int i = 1; i <= columnNum; i++) {
                System.out.print(i + ".  Update field " + availableColumns.get(i-1) + "\n");
            }
            System.out.print(columnNum + 1 + ".  Back\n>> ");

            int choice = 0;
            boolean cond = true;
            while (cond) {
                try {
                    choice = Integer.parseInt(in.readLine());
                    if (choice >= 1 && choice <= columnNum + 1) {
                        cond = false;
                    } else {
                        System.out.println("Please enter an available option!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Your input must contain valid integers only! Try again: ");
                }
            }
            // Back option
            if (choice == columnNum + 1) {
                return;
            }

            // construct the update sql query
            String fieldName = availableColumns.get(choice - 1);
            // get data type of field
            String fColumnType = getColumnType(tableName, fieldName);
            PreparedStatement ps = con.prepareStatement("UPDATE " + tableName + " SET " + fieldName + " = ? WHERE " + primaryKey + " = ?");

            System.out.print("\nSET " + fieldName + ": ");
            if (fColumnType.equals("NUMBER")) {
                cond = true;
                while (cond) {
                    try {
                        int id = Integer.parseInt(in.readLine());
                        ps.setInt(1, id);
                        cond = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Your input must contain valid integers only! Try again: ");
                    }
                }
            } else {
                ps.setString(1, in.readLine());
            }

            System.out.print("\nWHERE " + primaryKey + ": ");
            if (pkColumnType.equals("NUMBER")) {
                cond = true;
                while (cond) {
                    try {
                        ps.setInt(2, Integer.parseInt(in.readLine()));
                        cond = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Your input must contain valid integers only! Try again: ");
                    }
                }
            } else {
                ps.setString(2, in.readLine());
            }

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\nRow does not exist!");
            }
            con.commit();
            ps.close();
            if (rowCount > 0) {
                System.out.println("\nUpdate successful!");
            }
        }catch(IOException e){
            System.out.println("IOException!");
        }catch(SQLException ex){
            System.out.println("Message: " + ex.getMessage());

            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
    }

    /*
     * display all table data with column width automatically sized to match
     */
    private void showTableData(String tableName) {
        try {
            System.out.print("\n\nPlease choose an option: \n");
            System.out.print("1.  Enter Projection and/or Selection criteria\n");
            System.out.print("2.  View entire table\n");
            System.out.print("3.  Back\n>> ");

            int choice = 0;
            boolean cond = true;
            while (cond) {
                try {
                    choice = Integer.parseInt(in.readLine());
                    if (choice >= 1 && choice <= 3) {
                        cond = false;
                    } else {
                        System.out.println("Please enter an available option!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Your input must contain valid integers only! Try again: ");
                }
            }
            // Back option
            if (choice == 3) {
                return;
            }

            // default query for returning all table data
            String query = "SELECT * FROM " + tableName;
            // get user input for projection and selection
            if (choice == 1) {
                System.out.print("\n\nPlease enter values to perform projection (\"a, b, c\" or * to project all fields):\n");
                String projection = in.readLine();

                System.out.print("\n\nPlease enter values to perform selection (\"a = '?'\" or * to skip selection):\n");
                String selection = in.readLine();

                if (selection.equals("*")) {
                    query = "SELECT " + projection + " FROM " + tableName;
                } else {
                    query = "SELECT " + projection + " FROM " + tableName + " WHERE " + selection;
                }
            }

            // execute query
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();

            // get number of columns
            int numCols = rsMetaData.getColumnCount();

            ArrayList<String> availableColumns = new ArrayList<String>();
            ArrayList<Integer> columnWidths = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i <= numCols; i++) {
                int displaySize = rsMetaData.getColumnDisplaySize(i) + 20;
                String columnName = rsMetaData.getColumnName(i);
                availableColumns.add(columnName);
                columnWidths.add(displaySize);
                System.out.printf("%-" + displaySize + "." + displaySize + "s", columnName);
            }
            System.out.println("\n");

            // display rows
            while (resultSet.next()) {
                for (int i = 0; i < numCols; i++) {
                    String columnName = availableColumns.get(i);
                    Integer columnSize = columnWidths.get(i);
                    String rowData = resultSet.getString(columnName);
                    System.out.printf("%-" + columnSize + "." + columnSize + "s", rowData);
                }
                System.out.println("\n");
            }
            statement.close();
        }catch(IOException e){
            System.out.println("IOException!");
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    /*
     * find the primary key of given table
     */
    private String getPrimaryKey(String tableName) {
        String primaryKey = "";
        try {
            DatabaseMetaData dbMetaData = con.getMetaData();
            ResultSet resultSet = dbMetaData.getPrimaryKeys(null, null, tableName);
            ArrayList<String> primaryKeys = new ArrayList<String>();
            while (resultSet.next()) {
                primaryKeys.add(resultSet.getString("COLUMN_NAME"));
            }
            if (primaryKeys.size() > 0) {
                primaryKey = primaryKeys.get(0);
            }
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
        return primaryKey;
    }

    /*
     * find the data type for this column
     */
    private String getColumnType(String tableName, String columnName) {
        String columnType = "";
        try {
            int index = 0;
            Statement statement = con.createStatement();
            ResultSetMetaData rsMetaData = statement.executeQuery("SELECT * FROM " + tableName).getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if (rsMetaData.getColumnName(i).equals(columnName)) {
                    index = i;
                    break;
                }
            }
            columnType = rsMetaData.getColumnTypeName(index);
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
        return columnType;
    }

    /*
     * find the available columns for given table
     */
    private ArrayList<String> getTableColumns(String tableName) {
        ArrayList<String> availableColumns = new ArrayList<String>();
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsMetaData = resultSet.getMetaData();

            // get number of columns
            int numCols = rsMetaData.getColumnCount();

            // get column names
            for (int i = 1; i <= numCols; i++) {
                availableColumns.add(rsMetaData.getColumnName(i));
            }
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex2) {
                System.out.println("Message: " + ex2.getMessage());
                System.exit(-1);
            }
        }
        return availableColumns;
    }

    public static void main(String args[]) {
        HospitalDatabase hospitalDatabase = new HospitalDatabase();
    }
}

