import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

// Parcel Class
class Parcel {
    private String parcelId;
    private double weight;
    private String destination;

    public Parcel(String parcelId, double weight, String destination) {
        this.parcelId = parcelId;
        this.weight = weight;
        this.destination = destination;
    }

    public String getParcelId() {
        return parcelId;
    }

    public double getWeight() {
        return weight;
    }

    public String getDestination() {
        return destination;
    }
}

// Customer Class
class Customer {
    private String customerId;
    private String name;
    private String email;

    public Customer(String customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

// QueueOfCustomers Class
class QueueOfCustomers {
    private Queue<Customer> customerQueue;

    public QueueOfCustomers() {
        customerQueue = new LinkedList<>();
    }

    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public Customer processNextCustomer() {
        return customerQueue.poll();
    }

    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    public Queue<Customer> getQueue() {
        return customerQueue;
    }
}

// ParcelMap Class
class ParcelMap {
    private Map<String, Parcel> parcels;

    public ParcelMap() {
        parcels = new HashMap<>();
    }

    public void addParcel(Parcel parcel) {
        parcels.put(parcel.getParcelId(), parcel);
    }

    public Parcel getParcel(String parcelId) {
        return parcels.get(parcelId);
    }

    public Map<String, Parcel> getParcels() {
        return parcels;
    }
}

// Log Class (Singleton)
class Log {
    private static Log instance;
    private StringBuilder logData;

    private Log() {
        logData = new StringBuilder();
    }

    public static synchronized Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addLog(String message) {
        logData.append(message).append("\n");
    }

    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(logData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLogs() {
        return logData.toString();
    }
}

// Worker Class
class Worker {
    public double calculateFee(Parcel parcel) {
        return parcel.getWeight() * 10; // Example fee calculation
    }

    public void processParcel(Customer customer, Parcel parcel) {
        double fee = calculateFee(parcel);
        Log log = Log.getInstance();
        log.addLog("Processing Parcel for Customer: " + customer.getName());
        log.addLog("Parcel ID: " + parcel.getParcelId() + ", Destination: " + parcel.getDestination());
        log.addLog("Fee Calculated: $" + fee);
    }
}

// Manager Class
public class Manager {

    private static QueueOfCustomers queue = new QueueOfCustomers();
    private static ParcelMap parcelMap = new ParcelMap();
    private static Worker worker = new Worker();

    public static void main(String[] args) {
        // Add sample data
        addSampleData();

        // Initialize GUI
        SwingUtilities.invokeLater(() -> {
            new Manager().createAndShowGUI();
        });
    }

    private static void addSampleData() {
        queue.addCustomer(new Customer("C001", "Alice", "alice@example.com"));
        queue.addCustomer(new Customer("C002", "Bob", "bob@example.com"));

        parcelMap.addParcel(new Parcel("P001", 5.0, "New York"));
        parcelMap.addParcel(new Parcel("P002", 10.0, "Los Angeles"));
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Depot Worker Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Table to display customer queue
        DefaultTableModel customerTableModel = new DefaultTableModel(new String[]{"Customer ID", "Name", "Email"}, 0);
        JTable customerTable = new JTable(customerTableModel);
        populateCustomerTable(customerTableModel);

        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBorder(BorderFactory.createTitledBorder("Customer Queue"));

        // Table to display parcel details
        DefaultTableModel parcelTableModel = new DefaultTableModel(new String[]{"Parcel ID", "Weight", "Destination"}, 0);
        JTable parcelTable = new JTable(parcelTableModel);
        populateParcelTable(parcelTableModel);

        JScrollPane parcelScrollPane = new JScrollPane(parcelTable);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Parcel Details"));

        // Log display area
        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        // Buttons
        JButton processButton = new JButton("Process Next Customer");
        processButton.addActionListener(e -> {
            if (!queue.isEmpty()) {
                Customer customer = queue.processNextCustomer();
                Parcel parcel = parcelMap.getParcel("P00" + customer.getCustomerId().charAt(1)); // Match parcel to customer
                if (parcel != null) {
                    worker.processParcel(customer, parcel);
                } else {
                    Log.getInstance().addLog("No Parcel Found for Customer: " + customer.getName());
                }
                logArea.setText(Log.getInstance().getLogs());
                populateCustomerTable(customerTableModel);
            } else {
                JOptionPane.showMessageDialog(frame, "No more customers in the queue!");
            }
        });

        JButton saveLogButton = new JButton("Save Log");
        saveLogButton.addActionListener(e -> {
            Log.getInstance().saveToFile("log.txt");
            JOptionPane.showMessageDialog(frame, "Logs saved to log.txt");
        });

        // Layout setup
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(customerScrollPane);
        centerPanel.add(parcelScrollPane);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(processButton);
        bottomPanel.add(saveLogButton);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(logScrollPane, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void populateCustomerTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows
        for (Customer customer : queue.getQueue()) {
            model.addRow(new Object[]{customer.getCustomerId(), customer.getName(), customer.getEmail()});
        }
    }

    private void populateParcelTable(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows
        for (Parcel parcel : parcelMap.getParcels().values()) {
            model.addRow(new Object[]{parcel.getParcelId(), parcel.getWeight(), parcel.getDestination()});
        }
    }
}
