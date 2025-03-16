package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteCustomer extends JFrame implements ActionListener {

    JTextField meterNumberField;
    JButton deleteButton, cancelButton;

    DeleteCustomer() {
        setBounds(500, 220, 500, 300);
        setLayout(null);

        JLabel heading = new JLabel("Delete Customer");
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        heading.setBounds(150, 20, 200, 30);
        add(heading);

        JLabel meterNumberLabel = new JLabel("Meter Number");
        meterNumberLabel.setBounds(60, 80, 100, 30);
        add(meterNumberLabel);

        meterNumberField = new JTextField();
        meterNumberField.setBounds(200, 80, 200, 30);
        add(meterNumberField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(120, 150, 100, 30);
        deleteButton.setBackground(Color.BLACK);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(this);
        add(deleteButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(240, 150, 100, 30);
        cancelButton.setBackground(Color.BLACK);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(this);
        add(cancelButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == deleteButton) {
            String meter = meterNumberField.getText();
            try {
                Conn c = new Conn();
                String query = "delete from customer where meter_no = '" + meter + "'";
                String query1 = "delete from login where meter_no = '" + meter + "'";
                c.s.executeUpdate(query);
                c.s.executeUpdate(query1);
                JOptionPane.showMessageDialog(null, "Customer Deleted Successfully");
                setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new DeleteCustomer();
    }
}
