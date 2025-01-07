import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    private String customerID;
    private String name;

    public CustomerModel(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // CRUD Operations
    private static List<CustomerModel> customers = new ArrayList<>();

    public static void addCustomer(CustomerModel customer) {
        customers.add(customer);
    }

    public static CustomerModel getCustomer(String customerID) {
        for (CustomerModel customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    public static void updateCustomer(String customerID, String name) {
        CustomerModel customer = getCustomer(customerID);
        if (customer != null) {
            customer.setName(name);
        }
    }

    public static void deleteCustomer(String customerID) {
        customers.removeIf(customer -> customer.getCustomerID().equals(customerID));
    }
}
