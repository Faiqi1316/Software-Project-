import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

// Parcel Class
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

    public String getStatus() {
        return status;
    }

    public float calculateFee() {
        return weight * 10 + daysInDepot * 2;
    }

    public String getDimensions() {
        return "Length: " + length + ", Width: " + width + ", Height: " + height;
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

// Worker Class
class Worker {
    private String name;

    public Worker(String name) {
        this.name = name;
    }

    public void processCustomer(Customer customer) {
        if (!customer.getParcels().isEmpty()) {
            Parcel parcel = customer.getParcels().poll();
            Log.getInstance().addLog("Processed Parcel " + parcel.getParcelID() + " for Customer " + customer.getName());
        } else {
            Log.getInstance().addLog("No parcels to process for Customer " + customer.getName());
        }
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
        log = Log.getInstance();
    }

    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public void addParcel(Parcel parcel) {
        parcelMap.addParcel(parcel);
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
}

// GUI Application
public class DepotWorkerApp {
    private Manager manager;

    public DepotWorkerApp() {
        manager = new Manager();
        setupSampleData();
        createAndShowGUI();
    }

    private void setupSampleData() {
        Customer customer1 = new Customer("C001", "Alice");
        Customer customer2 = new Customer("C002", "Bob");

        Parcel parcel1 = new Parcel("P001", 10, 5, 4, 15, 2, "Pending");
        Parcel parcel2 = new Parcel("P002", 12, 6, 5, 20, 3, "Pending");

        customer1.addParcel(parcel1);
        customer2.addParcel(parcel2);

        manager.addCustomer(customer1);
        manager.addCustomer(customer2);
        manager.addParcel(parcel1);
        manager.addParcel(parcel2);
    }

    private void createAndShowGUI() {
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

    private void showCustomerQueue() {
        JFrame frame = new JFrame("Customer Queue");
        frame.setSize(400, 300);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Customer ID", "Name"}, 0);
        JTable table = new JTable(model);

        for (Customer customer : manager.getCustomerQueue()) {
            model.addRow(new Object[]{customer.getCustomerID(), customer.getName()});
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    private void showParcels() {
        JFrame frame = new JFrame("Parcels");
        frame.setSize(400, 300);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Parcel ID", "Weight", "Status"}, 0);
        JTable table = new JTable(model);

        for (Parcel parcel : manager.getParcelMap().getParcels()) {
            model.addRow(new Object[]{parcel.getParcelID(), parcel.getWeight(), parcel.getStatus()});
        }

        frame.add(new JScrollPane(table));
        frame.setVisible(true);
    }

    private void showLogs() {
        JFrame frame = new JFrame("Logs");
        frame.setSize(400, 300);

        JTextArea textArea = new JTextArea(Log.getInstance().getLogs());
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DepotWorkerApp::new);
    }
}
