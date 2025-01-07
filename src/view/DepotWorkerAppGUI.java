package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Customer;
import model.Manager;
import model.Parcel;
import view.CustomerQueueView;
import view.LogView;
import view.ParcelView;
import controller.LogController;


public class DepotWorkerAppGUI {
    private Manager manager;
    private CustomerController customerController;
    private ParcelController parcelController;
    private LogController logController;

    public DepotWorkerAppGUI() {
        manager = new Manager();
        customerController = new CustomerController(manager);
        parcelController = new ParcelController(manager);
        logController = new LogController();

        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Depot Worker Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(0, 1));

        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to add customer
            }
        });

        JButton viewCustomerQueueButton = new JButton("View Customer Queue");
        viewCustomerQueueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to view customer queue
            }
        });

        JButton addParcelButton = new JButton("Add Parcel");
        addParcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to add parcel
            }
        });

        JButton viewAllParcelsButton = new JButton("View All Parcels");
        viewAllParcelsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to view all parcels
            }
        });

        JButton viewLogsButton = new JButton("View Logs");
        viewLogsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Logic to view logs
            }
        });

        frame.add(addCustomerButton);
        frame.add(viewCustomerQueueButton);
        frame.add(addParcelButton);
        frame.add(viewAllParcelsButton);
        frame.add(viewLogsButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DepotWorkerAppGUI();
            }
        });
    }
}
