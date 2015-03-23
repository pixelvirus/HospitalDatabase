// We need to import the java.sql package to use JDBC
import java.sql.*;
// for reading from the command line
import java.io.*;
// for the login window
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * This class implements a graphical login window and a simple text
 * interface for interacting with the hospital table
 */
public class hospital implements ActionListener {
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
    public hospital() {
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

        try {
            // Load the Oracle JDBC driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
            System.exit(-1);
        }
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
        int choice;
        boolean quit;
        quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose one of the following: \n");
                System.out.print("1.  Run sql directly (create table)\n");
                System.out.print("2.  Modify or view an existing table\n");
                System.out.print("3.  Quit\n>> ");

                choice = Integer.parseInt(in.readLine());
                System.out.println(" ");

                switch (choice) {
                    case 1:
                        runSql();
                        break;
                    case 2:
                        chooseTable();
                        break;
                    case 3:
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
     * run sql directly
     */
    private void runSql() {
        PreparedStatement ps;
        try {
            System.out.print("\nEnter sql: ");
            String sql = in.readLine();
            ps = con.prepareCall(sql);
            ps.executeUpdate();
            // commit work
            con.commit();
            ps.close();
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
     * choose a table to modify
     */
    private void chooseTable() {
        int choice;
        boolean quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose a table to modify: \n");
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

                choice = Integer.parseInt(in.readLine());
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
        int choice;
        boolean quit;
        quit = false;

        try {
            // disable auto commit mode
            con.setAutoCommit(false);

            while (!quit) {
                System.out.print("\n\nPlease choose one of the following: \n");
                System.out.print("1.  Insert " + tableName + "\n");
                System.out.print("2.  Delete " + tableName + "\n");
                System.out.print("3.  Update " + tableName + "\n");
                System.out.print("4.  Show " + tableName + "\n");
                System.out.print("5.  Test get column names");
                System.out.print("6.  Back\n>> ");

                choice = Integer.parseInt(in.readLine());
                System.out.println(" ");

                switch (choice) {
                    case 1:
                        insertIntoTable(tableName);
                        break;
                    case 2:
                        deleteFromTable(tableName);
                        break;
                    case 3:
                        updateTable(tableName);
                        break;
                    case 4:
                        showTable(tableName);
                        break;
                    case 5:
                        testGetColumns(tableName);
                        break;
                    case 6:
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
     * inserts a hospital
     */
    private void insertIntoTable(String tableName) {
        int bid;
        String bname;
        String baddr;
        String bcity;
        int bphone;
        PreparedStatement ps;

        try {
//            ps = con.prepareStatement("INSERT INTO hospital VALUES (?,?,?,?,?)");

//            System.out.print("\nBranch ID: ");
//            bid = Integer.parseInt(in.readLine());
//            ps.setInt(1, bid);
//
//            System.out.print("\nBranch Name: ");
//            bname = in.readLine();
//            ps.setString(2, bname);
//
//            System.out.print("\nBranch Address: ");
//            baddr = in.readLine();
//
//            if (baddr.length() == 0) {
//                ps.setString(3, null);
//            } else {
//                ps.setString(3, baddr);
//            }
//
//            System.out.print("\nBranch City: ");
//            bcity = in.readLine();
//            ps.setString(4, bcity);
//
//            System.out.print("\nBranch Phone: ");
//            String phoneTemp = in.readLine();
//            if (phoneTemp.length() == 0) {
//                ps.setNull(5, java.sql.Types.INTEGER);
//            } else {
//                bphone = Integer.parseInt(phoneTemp);
//                ps.setInt(5, bphone);
//            }
            System.out.print("\nInsert " + tableName + " sql: ");
            String sql = in.readLine();
            ps = con.prepareCall(sql);
            ps.executeUpdate();

            // commit work
            con.commit();

            ps.close();
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
     * deletes a hospital
     */
    private void deleteFromTable(String tableName) {
        int bid;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("DELETE FROM hospital WHERE branch_id = ?");

            System.out.print("\nBranch ID: ");
            bid = Integer.parseInt(in.readLine());
            ps.setInt(1, bid);

            int rowCount = ps.executeUpdate();

            if (rowCount == 0) {
                System.out.println("\nBranch " + bid + " does not exist!");
            }

            con.commit();

            ps.close();
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
     * updates the name of a hospital
     */
    private void updateTable(String tableName) {
        int bid;
        String bname;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("UPDATE hospital SET branch_name = ? WHERE branch_id = ?");

            System.out.print("\nBranch ID: ");
            bid = Integer.parseInt(in.readLine());
            ps.setInt(2, bid);

            System.out.print("\nBranch Name: ");
            bname = in.readLine();
            ps.setString(1, bname);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println("\nBranch " + bid + " does not exist!");
            }

            con.commit();
            ps.close();
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
     * test
     */
    private void testGetColumns(String tableName) {
        PreparedStatement ps;
        try {
            ps = con.prepareCall("SELECT table_name, column_name, data_type, data_length FROM USER_TAB_COLUMNS WHERE table_name = '" + tableName + "'");
            ps.executeUpdate();
            // commit work
            con.commit();
            ps.close();
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
     * display table rows
     */
    private void showTable(String tableName) {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();

            // get number of columns
            int numCols = metaData.getColumnCount();

            ArrayList<String> availableColumns = new ArrayList<String>();
            ArrayList<Integer> columnWidths = new ArrayList<Integer>();

            // display column names
            for (int i = 1; i < numCols; i++) {
                int displaySize = metaData.getColumnDisplaySize(i) + 1;
                String columnName = metaData.getColumnName(i);
                availableColumns.add(columnName);
                columnWidths.add(displaySize);
                System.out.printf("%-" + displaySize + "." + displaySize + "s", columnName);
            }
            System.out.println("\n");

            while (resultSet.next()) {
                for (int i = 0; i < availableColumns.size(); i++) {
                    String columnName = availableColumns.get(i);
                    Integer columnSize = columnWidths.get(i);
                    String rowData = resultSet.getString(columnName);
                    System.out.printf("%-" + columnSize + "." + columnSize + "s", rowData);
                }
                System.out.println("\n");
            }
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Message: " + ex.getMessage());
        }
    }

    public static void main(String args[]) {
        hospital b = new hospital();
    }
}

