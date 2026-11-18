import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ProductApp extends JFrame {

    private JTextField customerNameField;
    private JTextField productPriceField;
    private JComboBox<String> productComboBox;

    public ProductApp() {
        setTitle("Product App");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuBar();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField();

        JLabel productNameLabel = new JLabel("Product Name:");
        productComboBox = new JComboBox<>(new String[]{
                "Cozy Nights Monarch",
                "Restonic Barbados",
                "Sealy Phuket"
        });

        JLabel productPriceLabel = new JLabel("Product Price:");
        productPriceField = new JTextField();

        JButton processButton = new JButton("Process");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(customerNameLabel);
        panel.add(customerNameField);

        panel.add(productNameLabel);
        panel.add(productComboBox);

        panel.add(productPriceLabel);
        panel.add(productPriceField);

        panel.add(new JLabel());
        panel.add(processButton);

        add(panel);

        processButton.addActionListener(e -> processSale());
    }

    private void processSale() {
        String customerName = customerNameField.getText();
        String productName = productComboBox.getSelectedItem().toString();

        if (customerName.isEmpty() || productPriceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the customer name and product price.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        double productPrice;

        try {
            productPrice = Double.parseDouble(productPriceField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid product price.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        ProductCalculations calculations = new ProductCalculations(productPrice);

        double vatAmount = calculations.GetVat();
        double commissionAmount = calculations.GetCommission();
        double totalSaleAmount = calculations.GetTotal();

        String message = String.format(
                "Customer Name: %s\n" +
                "Product: %s\n" +
                "VAT: R%.2f\n" +
                "COMMISSION: R%.2f\n" +
                "TOTAL: R%.2f",
                customerName,
                productName,
                vatAmount,
                commissionAmount,
                totalSaleAmount
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Product App",
                JOptionPane.INFORMATION_MESSAGE
        );

        saveToFile(customerName, productName, vatAmount, commissionAmount, totalSaleAmount);
    }

    private void saveToFile(String customerName, String productName, double vatAmount,
                            double commissionAmount, double totalSaleAmount) {
        try {
            FileWriter writer = new FileWriter("data.txt");

            writer.write("DATA LOG\n");
            writer.write("*********************\n");
            writer.write("Customer Name: " + customerName + "\n");
            writer.write("Product: " + productName + "\n");
            writer.write(String.format("VAT: R%.2f\n", vatAmount));
            writer.write(String.format("COMMISSION: R%.2f\n", commissionAmount));
            writer.write(String.format("TOTAL: R%.2f\n", totalSaleAmount));

            writer.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Sale information saved to data.txt",
                    "File Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while saving to the file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processMenuItem = new JMenuItem("Process");
        JMenuItem clearMenuItem = new JMenuItem("Clear");

        exitMenuItem.addActionListener(e -> System.exit(0));
        processMenuItem.addActionListener(e -> processSale());
        clearMenuItem.addActionListener(e -> clearForm());

        fileMenu.add(exitMenuItem);

        toolsMenu.add(processMenuItem);
        toolsMenu.add(clearMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);

        setJMenuBar(menuBar);
    }

    private void clearForm() {
        customerNameField.setText("");
        productPriceField.setText("");
        productComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductApp app = new ProductApp();
            app.setVisible(true);
        });
    }
}
