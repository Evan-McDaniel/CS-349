package energyDashboard;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class AddCustomerWindow extends JFrame {
	private JPanel panel = new JPanel();
	private JTextField c_name = new JTextField(20);
	private JTextField c_phone = new JTextField(10);
	private JTextField c_address = new JTextField(20);
	private JTextField c_tariff = new JTextField(10);
	private JTextField c_meter = new JTextField(10);
	private JButton submit = new JButton("Submit");
	private Connection conn;
	
	public AddCustomerWindow() {
		setTitle("Create New Customer");
		// Specify what happens when the close button is clicked.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		EnergyDashboard.getDatabaseConnection();
		//building each panel separately
		buildPanel();

		add(panel, BorderLayout.CENTER);
		pack();
		// Display the window.
		setVisible(true);
	}
	
	private void buildPanel() {
		panel.setLayout(new GridLayout(6, 1));
		panel.add(new JLabel("Enter the customer name:"));
		panel.add(c_name);
		panel.add(new JLabel("Enter the customer phone number:"));
		panel.add(c_phone);
		panel.add(new JLabel("Enter the customer address:"));
		panel.add(c_address);
		panel.add(new JLabel("Enter the customer tariff:"));
		panel.add(c_tariff);
		panel.add(new JLabel("Enter the customer meter:"));
		panel.add(c_meter);
		submit.addActionListener(new AddCustomerListener());
		panel.add(submit);
	}
	
	private class AddCustomerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String sqlStatement = String.format("INSERT INTO Customer (Name, PhoneNumber, Address, Tariff, MeterType)  VALUES %s, %s, %s, %f, %s",
						c_name.getText(), c_phone.getText(), c_address.getText(), c_tariff.getText(), c_meter.getText()) ;
				int rows = stmt.executeUpdate(sqlStatement);
				if(rows == 1) {
					JOptionPane.showMessageDialog(null, "Customer succesfully added");
				} else {
					JOptionPane.showMessageDialog(null, "Customer could not be added. check input again.");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
