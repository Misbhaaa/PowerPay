package electricity.billing.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class Paytm extends JFrame implements ActionListener {

    String meter;
    JButton back;

    public Paytm(String meter) {
        super("Payment");
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        this.meter = meter;

        setLayout(new GridLayout(0, 1));

        JButton paytmButton = createStyledButton("Pay via Paytm");
        paytmButton.addActionListener(this);
        add(paytmButton);

        JButton gpayButton = createStyledButton("Pay via Amazon Pay");
        gpayButton.addActionListener(this);
        add(gpayButton);

        JButton phonepeButton = createStyledButton("Pay via PhonePe");
        phonepeButton.addActionListener(this);
        add(phonepeButton);


        back = createStyledButton("Back");
        back.addActionListener(this);
        add(back);

        setTitle("Payment Options");
        setSize(300, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 255, 255)); // Set background color to bright red
        button.setForeground(Color.BLACK); // Set text color
        button.setFocusPainted(false); // Remove focus border
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Set padding
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            setVisible(false);
            new PayBill(meter);
        } else {
            JButton clickedButton = (JButton) ae.getSource();
            String buttonText = clickedButton.getText();
            if (buttonText.startsWith("Pay via")) {
                try {
                    String paymentMethod = buttonText.substring(8); // Extract payment method from button text
                    String url = getPaymentURL(paymentMethod);
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error opening payment page: " + e.getMessage());
                }
            }
        }
    }

    private String getPaymentURL(String paymentMethod) {
        // Add logic to get the payment URL based on the selected payment method
        // For simplicity, we're using static URLs here
        switch (paymentMethod) {
            case "Paytm":
                return "https://paytm.com/online-payments";
            case "Amazon Pay":
                return "https://www.amazon.in/amazonpay/home";
            case "PhonePe":
                return "https://www.phonepe.com/";
            // Add more cases for other payment methods
            default:
                return ""; // Return empty string if payment method not recognized
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Paytm("");
    }
}
