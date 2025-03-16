package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;

public class Receipt extends JFrame implements ActionListener, Printable {

    private String meter;
    private JButton generateButton, printButton, cancelButton;
    private Choice monthChoice;
    private JTextArea area;

    Receipt(String meter) {
        this.meter = meter;

        setSize(600, 760);
        setLocation(550, 30);

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();

        JLabel heading = new JLabel("Generate Receipt");
        JLabel meterLabel = new JLabel("Meter Number: " + meter);

        monthChoice = new Choice();
        monthChoice.add("January");
        monthChoice.add("February");
        monthChoice.add("March");
        monthChoice.add("April");
        monthChoice.add("May");
        monthChoice.add("June");
        monthChoice.add("July");
        monthChoice.add("August");
        monthChoice.add("September");
        monthChoice.add("October");
        monthChoice.add("November");
        monthChoice.add("December");

        area = new JTextArea(50, 15);
        area.setText("\n\n\t--------Click on the---------\n\t Generate Receipt Button to get\n\tthe receipt of the Selected Month");
        area.setFont(new Font("Senserif", Font.ITALIC, 18));

        JScrollPane scrollPane = new JScrollPane(area);

        generateButton = new JButton("Generate Receipt");
        generateButton.addActionListener(this);

        printButton = new JButton("Print");
        printButton.addActionListener(this);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        panel.add(heading);
        panel.add(meterLabel);
        panel.add(monthChoice);
        add(panel, BorderLayout.NORTH);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(generateButton);
        buttonPanel.add(printButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == generateButton) {
            generateReceipt();
        } else if (ae.getSource() == printButton) {
            printReceipt();
        } else if (ae.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    private void generateReceipt() {
        try {
            Conn c = new Conn();
            String month = monthChoice.getSelectedItem();

            ResultSet rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' and month='" + month + "'");
            
            if (rs.next()) {
                String status = rs.getString("status");
                if (!"paid".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(null, "Not Paid");
                    return;
                }
            }

            area.setText("\tELECTRICITY RECEIPT GENERATED FOR THE MONTH\n\tOF " + month + ", 2024\n\n\n");

            rs = c.s.executeQuery("select * from customer where meter_no = '" + meter + "'");

            if (rs.next()) {
                area.append("\n    Customer Name: " + rs.getString("name"));
                area.append("\n    Meter Number   : " + rs.getString("meter_no"));
                area.append("\n    Address             : " + rs.getString("address"));
                area.append("\n    City                 : " + rs.getString("city"));
                area.append("\n    State                : " + rs.getString("state"));
                area.append("\n    Email                : " + rs.getString("email"));
                area.append("\n    Phone                 : " + rs.getString("phone"));
                area.append("\n---------------------------------------------------");
                area.append("\n");
            }

            rs = c.s.executeQuery("select * from meter_info where meter_no = '" + meter + "'");

            if (rs.next()) {
                area.append("\n    Meter Location: " + rs.getString("meter_location"));
                area.append("\n    Meter Type:     " + rs.getString("meter_type"));
                area.append("\n    Phase Code:        " + rs.getString("phase_code"));
                area.append("\n    Bill Type:          " + rs.getString("bill_type"));
                area.append("\n    Days:                " + rs.getString("days"));
                area.append("\n---------------------------------------------------");
                area.append("\n");
            }

            rs = c.s.executeQuery("select * from tax");

            rs = c.s.executeQuery("select * from bill where meter_no = '" + meter + "' and month='" + month + "'");

            if (rs.next()) {
                area.append("\n");
                area.append("\n    Current Month: " + rs.getString("month"));
                area.append("\n    Units Consumed:     " + rs.getString("units"));
                area.append("\n    Total Charges:        " + rs.getString("totalbill"));
                area.append("\n-------------------------------------------------------");
                area.append("\n    Total Paid: " + rs.getString("totalbill"));
                area.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printReceipt() {
        try {
            boolean complete = area.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing Completed!", "Printing", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing Cancelled!", "Printing", JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Printing Failed: " + pe.getMessage(), "Printing", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Disable double buffering
        disableDoubleBuffering(area);

        // Call the JTextArea's print method
        area.printAll(g);

        // Re-enable double buffering
        enableDoubleBuffering(area);

        return PAGE_EXISTS;
    }

    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }

    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    public static void main(String[] args) {
        new Receipt("");
    }
}
