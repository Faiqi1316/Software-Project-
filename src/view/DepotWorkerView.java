import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DepotWorkerView {
    private JFrame frame;
    private JTextArea textArea;
    private JButton addCustomerButton;
    private JButton addParcelButton;

    private DepotWorkerController controller;

    public DepotWorkerView(DepotWorkerController controller) {
        this.controller = controller;
        frame = new JFrame("Depot Worker Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addCustomerButton = new JButton("Add Customer");
        addParcelButton = new JButton("Add Parcel");
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(addParcelButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void setTextArea(String text) {
        textArea.setText(text);
    }

    public void addCustomerButtonListener(ActionListener listener) {
        addCustomerButton.addActionListener(listener);
    }

    public void addParcelButtonListener(ActionListener listener) {
        addParcelButton.addActionListener(listener);
    }
}
