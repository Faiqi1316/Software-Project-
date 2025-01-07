import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

// Parcel Class (with daysInDepot)
class Parcel {
    private String parcelID;
    private float length;
    private float width;
    private float height;
    private float weight;
    private int daysInDepot;
    private String status;

    public Parcel(String parcelID, float length, float width, float height, float weight, int daysInDepot, String status) {
        this.parcelID = parcelID;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = status;
    }

    public String getParcelID() {
        return parcelID;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public void setDaysInDepot(int daysInDepot) {
        this.daysInDepot = daysInDepot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float calculateFee() {
        float baseFee = weight * 10 + daysInDepot * 2;
        if (parcelID.startsWith("D")) {
            baseFee *= 0.9; // 10% discount for parcels with ID starting with 'D'
        }
        return baseFee;
    }

    public String getDimensions() {
        return "Length: " + length + ", Width: " + width + ", Height: " + height;
    }

    public static boolean isValidParcelID(String id) {
        return id.matches("[A-Z][0-9]{3}");
    }
}

// Customer Class
class Customer {
    private String customerID;
    private String name;
    private Queue<Parcel> parcels;

    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
        this.parcels = new LinkedList<>();
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public Queue<Parcel> getParcels() {
        return parcels;
    }

    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    public static boolean isValidCustomerID(String id) {
        return id.matches("C[0-9]{3}");
    }
}

// Log Class (Singleton)
class Log {
    private static Log instance;
    private StringBuffer logData;

    private Log() {
        logData = new StringBuffer();
    }

    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addLog(String event) {
        logData.append(event).append("\n");
    }

    public String getLogs() {
        return logData.toString();
    }

    public void writeLogsToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(logData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// ParcelMap Class
class ParcelMap {
    private Map<String, Parcel> parcels;

    public ParcelMap() {
        parcels = new HashMap<>();
    }

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelID(), parcel);
    }

    public Parcel getParcel(String parcelID) {
        return parcels.get(parcelID);
    }

    public void removeParcel(String parcelID) {
        parcels.remove(parcelID);
    }

    public Collection<Parcel> getParcels() {
        return parcels.values();
    }
}

// Manager Class
class Manager {
    private Queue<Customer> customerQueue;
    private ParcelMap parcelMap;
    private Log log;

    public Manager() {
        customerQueue = new LinkedList<>();
        parcelMap = new ParcelMap();
        log = Log.getInstance(); // Ensure log is initialized
    }

    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public void addParcel(Parcel parcel) {
        parcelMap.addParcel(parcel);
        log.addLog("Parcel added: " + parcel.getParcelID());
    }

    public Customer processNextCustomer() {
        return customerQueue.poll();
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public ParcelMap getParcelMap() {
        return parcelMap;
    }

    public Log getLog() {
        return log;
    }

    public void initializeFromFiles(String customerFile, String parcelFile) {
        try (BufferedReader customerReader = new BufferedReader(new FileReader(customerFile));
             BufferedReader parcelReader = new BufferedReader(new FileReader(parcelFile))) {

            String line;
            while ((line = customerReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && Customer.isValidCustomerID(parts[0])) {
                    addCustomer(new Customer(parts[0], parts[1]));
                }
            }

            while ((line = parcelReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7 && Parcel.isValidParcelID(parts[0])) {
                    Parcel parcel = new Parcel(parts[0], Float.parseFloat(parts[1]), Float.parseFloat(parts[2]),
                            Float.parseFloat(parts[3]), Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), parts[6]);
                    addParcel(parcel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float calculateFee(String parcelID) {
        Parcel parcel = parcelMap.getParcel(parcelID);
        return parcel != null ? parcel.calculateFee() : 0;
    }

    public void saveCustomersToFile(String customerFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerFile))) {
            for (Customer customer : customerQueue) {
                writer.write(customer.getCustomerID() + "," + customer.getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveParcelsToFile(String parcelFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(parcelFile))) {
            for (Parcel parcel : parcelMap.getParcels()) {
                writer.write(parcel.getParcelID() + "," + parcel.getLength() + "," + parcel.getWidth() + "," +
                        parcel.getHeight() + "," + parcel.getWeight() + "," + parcel.getDaysInDepot() + "," + parcel.getStatus() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// GUI Application with Days in Depot feature
public class DepotWorkerApp {
    private Manager manager;

    private DepotWorkerController controller;

    public DepotWorkerApp() {
        controller = new DepotWorkerController(); // Instantiate the controller
        manager = new Manager();
        manager.initializeFromFiles("customers.txt", "parcels.txt");
        createAndShowGUI(controller); // Pass the controller to the view

        // Save customers and parcels when the application closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            manager.saveCustomersToFile("customers.txt");
            manager.saveParcelsToFile("parcels.txt");
            manager.getLog().writeLogsToFile("logs.txt"); // Save logs as well
        }));
    }

    private void createAndShowGUI(DepotWorkerController controller) {
        JFrame frame = new JFrame("Depot Worker Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Depot Worker Application", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton customerQueueButton = new JButton("Customer Queue");
        JButton parcelsButton = new JButton("Parcels");
        JButton logButton = new JButton("Logs");

        buttonPanel.add(customerQueueButton);
        buttonPanel.add(parcelsButton);
        buttonPanel.add(logButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        customerQueueButton.addActionListener(e -> showCustomerQueue());
        parcelsButton.addActionListener(e -> showParcels());
        logButton.addActionListener(e -> showLogs());

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Method to show the customer queue in a table
    private void showCustomerQueue() {
        JFrame frame = new JFrame("Customer Queue");
        frame.setSize(600, 400);

        // Table Model
        DefaultTableModel model = new DefaultTableModel(new String[]{"Customer ID", "Name"}, 0);
        JTable table = new JTable(model);

        // Add customers to the table
        for (Customer customer : manager.getCustomerQueue()) {
            model.addRow(new Object[]{customer.getCustomerID(), customer.getName()});
        }

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Buttons to add/remove customers
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Customer");
        JButton removeButton = new JButton("Remove Customer");

        addButton.addActionListener(e -> {
            String customerID = JOptionPane.showInputDialog(frame, "Enter Customer ID:");
            String customerName = JOptionPane.showInputDialog(frame, "Enter Customer Name:");
            if (Customer.isValidCustomerID(customerID)) {
                Customer customer = new Customer(customerID, customerName);
                manager.addCustomer(customer);
                model.addRow(new Object[]{customer.getCustomerID(), customer.getName()});
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Customer ID format.");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String customerID = (String) model.getValueAt(selectedRow, 0);
                model.removeRow(selectedRow);
                manager.getCustomerQueue().removeIf(customer -> customer.getCustomerID().equals(customerID));
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Method to show the parcels in a table
    private void showParcels() {
        JFrame frame = new JFrame("Parcels");
        frame.setSize(600, 400);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Parcel ID", "Dimensions", "Weight", "Days in Depot", "Fee"}, 0);
        JTable table = new JTable(model);

        // Add parcels to the table
        for (Parcel parcel : manager.getParcelMap().getParcels()) {
            model.addRow(new Object[]{
                    parcel.getParcelID(),
                    parcel.getDimensions(),
                    parcel.getWeight(),
                    parcel.getDaysInDepot(),
                    parcel.calculateFee()
            });
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons for adding and removing parcels
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Parcel");
        JButton removeButton = new JButton("Remove Parcel");

        addButton.addActionListener(e -> {
            String parcelID = JOptionPane.showInputDialog(frame, "Enter Parcel ID:");
            String lengthStr = JOptionPane.showInputDialog(frame, "Enter Length:");
            String widthStr = JOptionPane.showInputDialog(frame, "Enter Width:");
            String heightStr = JOptionPane.showInputDialog(frame, "Enter Height:");
            String weightStr = JOptionPane.showInputDialog(frame, "Enter Weight:");
            String daysInDepotStr = JOptionPane.showInputDialog(frame, "Enter Days in Depot:");
            String status = JOptionPane.showInputDialog(frame, "Enter Parcel Status (e.g., Uncollected or Collected)");

            if (Parcel.isValidParcelID(parcelID)) {
                try {
                    float length = Float.parseFloat(lengthStr);
                    float width = Float.parseFloat(widthStr);
                    float height = Float.parseFloat(heightStr);
                    float weight = Float.parseFloat(weightStr);
                    int daysInDepot = Integer.parseInt(daysInDepotStr);
                    Parcel parcel = new Parcel(parcelID, length, width, height, weight, daysInDepot, status);
                    manager.addParcel(parcel);
                    model.addRow(new Object[]{parcelID, parcel.getDimensions(), weight, daysInDepot, parcel.calculateFee()});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Parcel ID format.");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String parcelID = (String) model.getValueAt(selectedRow, 0);
                manager.getParcelMap().removeParcel(parcelID);
                model.removeRow(selectedRow);
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
    }

    // Show Logs
    private void showLogs() {
        JFrame frame = new JFrame("Logs");
        frame.setSize(600, 400);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(manager.getLog().getLogs());

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DepotWorkerApp::new);
    }
}
