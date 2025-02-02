package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.sql.*;

public class GenerateBill extends JFrame implements ActionListener, Printable {

    String meter;
    JButton bill, printButton; 
    Choice cmonth;
    JTextArea area;
    GenerateBill(String meter) {
        this.meter = meter;
        
        setSize(600, 760); 
        setLocation(550, 30);
        
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        
        JLabel heading = new JLabel("Generate Bill");
        JLabel meternumber = new JLabel(meter);
        
        cmonth = new Choice();
        
        cmonth.add("January");
        cmonth.add("February");
        cmonth.add("March");
        cmonth.add("April");
        cmonth.add("May");
        cmonth.add("June");
        cmonth.add("July");
        cmonth.add("August");
        cmonth.add("September");
        cmonth.add("October");
        cmonth.add("November");
        cmonth.add("December");
        
        area = new JTextArea(50, 15);
        area.setText("\n\n\t--------Click on the---------\n\t Generate Bill Button to get\n\tthe bill of the Selected Month");
        area.setFont(new Font("Senserif", Font.ITALIC, 18));
        
        JScrollPane pane = new JScrollPane(area);
        
        bill = new JButton("Generate Bill");
        bill.addActionListener(this);

        // Create printButton
        printButton = new JButton("Print");
        printButton.addActionListener(this);
        
        panel.add(heading);
        panel.add(meternumber);
        panel.add(cmonth);
        add(panel, "North");
        
        add(pane, "Center");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(bill);
        buttonPanel.add(printButton); // Add printButton to the layout
        add(buttonPanel, "South");
        
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == bill) {
            generateBill();
        } else if (ae.getSource() == printButton) {
            printBill();
        }
    }

    private void generateBill() {
        try {
            Conn c = new Conn();
            
            String month = cmonth.getSelectedItem();
            
            area.setText("\tELECTRICITY BILL GENERATED FOR THE MONTH\n\tOF "+month+", 2022\n\n\n");
            
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no = '"+meter+"'");
        
            if(rs.next()) {
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
            
            rs = c.s.executeQuery("select * from meter_info where meter_no = '"+meter+"'");
        
            if(rs.next()) {
                area.append("\n    Meter Location: " + rs.getString("meter_location"));
                area.append("\n    Meter Type:     " + rs.getString("meter_type"));
                area.append("\n    Phase Code:        " + rs.getString("phase_code"));
                area.append("\n    Bill Type:          " + rs.getString("bill_type"));
                area.append("\n    Days:                " + rs.getString("days"));
                area.append("\n---------------------------------------------------");
                area.append("\n");
            }
            
            rs = c.s.executeQuery("select * from tax");
        
            if(rs.next()) {
                area.append("\n");
                area.append("\n    Cost Per Unit: " + rs.getString("cost_per_unit"));
                area.append("\n    Meter Rent:     " + rs.getString("cost_per_unit"));
                area.append("\n    Service Charge:        " + rs.getString("service_charge"));
                area.append("\n    Service Tax:          " + rs.getString("service_charge"));
                area.append("\n    Swacch Bharat Cess:                " + rs.getString("swacch_bharat_cess"));
                area.append("\n    Fixed Tax: " + rs.getString("fixed_tax"));
                area.append("\n");
            }
            
            rs = c.s.executeQuery("select * from bill where meter_no = '"+meter+"' and month='"+month+"'");
        
            if(rs.next()) {
                area.append("\n");
                area.append("\n    Current Month: " + rs.getString("month"));
                area.append("\n    Units Consumed:     " + rs.getString("units"));
                area.append("\n    Total Charges:        " + rs.getString("totalbill"));
                area.append("\n-------------------------------------------------------");
                area.append("\n    Total Payable: " + rs.getString("totalbill"));
                area.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBill() {
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

    public static void main(String[] args) {
        new GenerateBill("");
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
}
