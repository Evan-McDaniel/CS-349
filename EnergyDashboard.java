package energyDashboard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EnergyDashboard extends JFrame {
	private static String DB_URL = "jdbc:mysql:///EnergyDB";
	private static Connection conn;
	private Customer[] customerList = {};
	private JPanel customerPanel = new JPanel();
	private JPanel customerDetailPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JTable customerGrid;
	
	//creating constructor for main energy dashboard
	EnergyDashboard(){
		setTitle("Energy Dashboard");
		getDatabaseConnection();
		// Specify what happens when the close button is clicked.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//building each panel separately
		buildButtonPanel();
		buildCustomerDetailPanel();
		buildCustomerPanel();

		add(customerPanel, BorderLayout.CENTER);
		add(customerDetailPanel, BorderLayout.WEST);
		add(buttonPanel, BorderLayout.EAST);
		pack();
		// Display the window.
		setVisible(true);
	}
	
	public static void getDatabaseConnection() {
		try {
			// Create a connection to the database.
			conn = DriverManager.getConnection(DB_URL, "root", "12345");
		} catch (Exception ex) {
			//ex.printStackTrace();
			//System.exit(0);
			System.out.println("Could not obtain DB connection");
		}
	}
	
	private void buildButtonPanel() {
		//using box layout to align vertical from https://stackoverflow.com/questions/11357720/java-vertical-alignment-within-jpanel
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		//buttonPanel.setLayout(new GridLayout(3,1));
		JButton addCustomer = new JButton("Add Customer");
		JButton createInvoice = new JButton("Create Monthly Invoice");
		JButton recordPayment = new JButton("Record Payment");
		
		//add buttons to the button panel with center alignment and vertical spacing
		addCustomer.setAlignmentX(Component.CENTER_ALIGNMENT);
		addCustomer.addActionListener(new AddCustomerListener());
		createInvoice.setAlignmentX(Component.CENTER_ALIGNMENT);
		recordPayment.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(addCustomer);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(createInvoice);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(recordPayment);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
	}
	
	private void buildCustomerDetailPanel() {
		JTextArea details = new JTextArea("No customer selected");
		details.setEditable(false);
		customerDetailPanel.add(details);
	}
	
	//gets the customer names and ID from the database then adds to the customer panel in the form as table of names
	private void buildCustomerPanel() {
		if(conn != null) {
			String[][] customers = getCustomersFromDB();
			String[] colNames = {"ID", "Names"};
			customerGrid = new JTable(customers, colNames);
			// disable editing from https://stackoverflow.com/questions/1990817/how-to-make-a-jtable-non-editable
			customerGrid.setDefaultEditor(Object.class, null);
			//found information at https://stackoverflow.com/questions/15906001/jtable-selection-listener
			customerGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			ListSelectionModel selectionModel = customerGrid.getSelectionModel();
			selectionModel.addListSelectionListener(new TableListener());
			JScrollPane customerScrollList = new JScrollPane(customerGrid);
			customerPanel.add(customerScrollList);
		}
		else {
			customerPanel.setLayout(new GridLayout(2,1));
			customerPanel.add(new JLabel("No customers to display"));
			customerPanel.add(new JLabel("DB connection not found"));
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new EnergyDashboard();
	}
	
	private class AddCustomerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new AddCustomerWindow();
		}

	}
	
	private class TableListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			updateCustomerDetails(e);
		}
	}
	
	private String[][] getCustomersFromDB() {
		this.customerList = new Customer[3];
		String[][] customers = {};
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sqlStatement = "SELECT * FROM customer";
			ResultSet result = stmt.executeQuery(sqlStatement);
			ResultSetMetaData meta = result.getMetaData();
			customers = new String[3][meta.getColumnCount()];
			int i = 0;
			while (result.next())
			{
				Customer c = new Customer(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getDouble(5), result.getString(6), result.getDouble(7));
				customers[i][0] = result.getString(1);
				customers[i][1] = result.getString(2);
				this.customerList[i] = c;
				i++;
			}
			stmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return customers;
	}
	
	private void updateCustomerDetails(ListSelectionEvent e) {
		JButton invoice = new JButton("Create monthly invoice");
		invoice.addActionListener(new InvoiceListener());
		remove(customerDetailPanel);
		customerDetailPanel = new JPanel();
		//customerDetailPanel.setSize(500, 500);
		//customerDetailPanel.setLayout(new BoxLayout(customerDetailPanel, BoxLayout.Y_AXIS));
		customerDetailPanel.setLayout(new GridLayout(8, 2));
		Customer selected = search();
		JTextArea c_id = new JTextArea(Integer.toString(selected.getID()));
		JTextArea c_name = new JTextArea(selected.getName());
		JTextArea c_phone = new JTextArea(selected.getPhoneNumber());
		JTextArea c_address = new JTextArea(selected.getAddress());
		JTextArea c_tariff = new JTextArea(Double.toString(selected.getTariff()));
		JTextArea c_meter = new JTextArea((selected.getMeterType()));
		JTextArea c_usage= new JTextArea(Double.toString(selected.getUsage()));
		c_id.setAlignmentX(Component.CENTER_ALIGNMENT);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(150,10)));
		customerDetailPanel.add(new JLabel("Customer ID:"));
		customerDetailPanel.add(c_id);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,5)));
		customerDetailPanel.add(new JLabel("Customer Name:"));
		customerDetailPanel.add(c_name);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,5)));
		customerDetailPanel.add(new JLabel("Customer Phone Number:"));
		customerDetailPanel.add(c_phone);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,5)));
		customerDetailPanel.add(new JLabel("Customer Address:"));
		customerDetailPanel.add(c_address);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,5)));
		customerDetailPanel.add(new JLabel("Customer Tariff Rate:"));
		customerDetailPanel.add(c_tariff);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,5)));
		customerDetailPanel.add(new JLabel("Customer Meter Type:"));
		customerDetailPanel.add(c_meter);
		customerDetailPanel.add(Box.createRigidArea(new Dimension(0,10)));
		customerDetailPanel.add(new JLabel("Customer Usage Amount:"));
		customerDetailPanel.add(c_usage);
		add(customerDetailPanel, BorderLayout.WEST);
		pack();
		
	}
	
	private class InvoiceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new InvoiceWindow();
		}
	}
	
	
	private Customer search() {
		int rowNum = customerGrid.getSelectedRow();
		int customer_id = Integer.parseInt(customerGrid.getValueAt(rowNum, 0).toString());
		for (int i = 0; i < customerList.length;i++) {
			if(customerList[i].getID() == customer_id) {
				return customerList[i];
			}
		}
		return new Customer();
	}

}
