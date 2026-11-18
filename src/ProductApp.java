import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ProductApp extends JFrame {

    // Text field used to capture the customer's name
    private JTextField customerNameField;

    // Text field used to capture the product price
    private JTextField productPriceField;

    // Combo box used to select the mattress product
    private JComboBox<String> productComboBox;

    public ProductApp() {
        // Set the main window title
        setTitle("Product App");

        // Set the size of the application window
        setSize(420, 260);

        // Close the application when the window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Display the window in the center of the screen
        setLocationRelativeTo(null);

        // Create the menu system for the application
        createMenuBar();

        // Create a panel using GridLayout to arrange labels and input fields neatly
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField();

        JLabel productNameLabel = new JLabel("Product Name:");

        // Add the available mattress products to the combo box
        productComboBox = new JComboBox<>(new String[]{
                "Cozy Nights Monarch",
                "Restonic Barbados",
                "Sealy Phuket"
        });

        JLabel productPriceLabel = new JLabel("Product Price:");
        productPriceField = new JTextField();

        // Button used to process the sale details
        JButton processButton = new JButton("Process");

        // Add spacing around the panel
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add customer name controls to the form
        panel.add(customerNameLabel);
        panel.add(customerNameField);

        // Add product selection controls to the form
        panel.add(productNameLabel);
        panel.add(productComboBox);

        // Add product price controls to the form
        panel.add(productPriceLabel);
        panel.add(productPriceField);

        // Add the process button to the form
        panel.add(new JLabel());
        panel.add(processButton);

        // Add the panel to the window
        add(panel);

        // When the Process button is clicked, process the sale
        processButton.addActionListener(e -> processSale());
    }

    private void createMenuBar() {
        // Create the main menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create File menu and Exit menu item
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        // Create Tools menu with Process and Clear menu items
        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processMenuItem = new JMenuItem("Process");
        JMenuItem clearMenuItem = new JMenuItem("Clear");

        // Close the application when Exit is clicked
        exitMenuItem.addActionListener(e -> System.exit(0));

        // Process the sale when Process is clicked from the menu
        processMenuItem.addActionListener(e -> processSale());

        // Clear all input fields when Clear is clicked
        clearMenuItem.addActionListener(e -> clearForm());

        // Add Exit item to File menu
        fileMenu.add(exitMenuItem);

        // Add Process and Clear items to Tools menu
        toolsMenu.add(processMenuItem);
        toolsMenu.add(clearMenuItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);

        // Attach the menu bar to the window
        setJMenuBar(menuBar);
    }

    private void clearForm() {
        // Clear the customer name textbox
        customerNameField.setText("");

        // Clear the product price textbox
        productPriceField.setText("");

        // Reset the combo box to the first product
        productComboBox.setSelectedIndex(0);
    }

    private void processSale() {
        // Capture the customer name entered by the user
        String customerName = customerNameField.getText();

        // Capture the selected product from the combo box
        String productName = productComboBox.getSelectedItem().toString();

        // Validate that required fields are not empty
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
            // Convert the product price from text to a number
            productPrice = Double.parseDouble(productPriceField.getText());
        } catch (NumberFormatException ex) {
            // Show an error message if the price is not a valid number
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid product price.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Create a ProductCalculations object to calculate VAT, commission, and total
        ProductCalculations calculations = new ProductCalculations(productPrice);

        // Calculate VAT, commission, and total sale amount
        double vatAmount = calculations.GetVat();
        double commissionAmount = calculations.GetCommission();
        double totalSaleAmount = calculations.GetTotal();

        // Prepare the message to display in the message box
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

        // Display the sale information to the user
        JOptionPane.showMessageDialog(
                this,
                message,
                "Product App",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Save the sale information to data.txt
        saveToFile(customerName, productName, vatAmount, commissionAmount, totalSaleAmount);
    }

    private void saveToFile(String customerName, String productName, double vatAmount,
                            double commissionAmount, double totalSaleAmount) {
        try {
            // Create or overwrite the data.txt file
            FileWriter writer = new FileWriter("data.txt");

            // Write the sale details to the file in the required format
            writer.write("DATA LOG\n");
            writer.write("*********************\n");
            writer.write("Customer Name: " + customerName + "\n");
            writer.write("Product: " + productName + "\n");
            writer.write(String.format("VAT: R%.2f\n", vatAmount));
            writer.write(String.format("COMMISSION: R%.2f\n", commissionAmount));
            writer.write(String.format("TOTAL: R%.2f\n", totalSaleAmount));

            // Close the file after writing
            writer.close();

            // Inform the user that the information was saved successfully
            JOptionPane.showMessageDialog(
                    this,
                    "Sale information saved to data.txt",
                    "File Saved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            // Show an error message if the file could not be saved
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred while saving to the file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        // Start the Swing application safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ProductApp app = new ProductApp();
            app.setVisible(true);
        });
    }
}
