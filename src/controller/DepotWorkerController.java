import java.util.*;

public class DepotWorkerController {
    private Queue<CustomerModel> customerQueue;
    private Map<String, ParcelModel> parcelMap;
    private LogModel log;

    public DepotWorkerController() {
        customerQueue = new LinkedList<>();
        parcelMap = new HashMap<>();
        log = new LogModel(); // Initialize log
    }

    public void addCustomer(CustomerModel customer) {
        customerQueue.add(customer);
        log.addLog("Customer added: " + customer.getCustomerID());
    }

    public void addParcel(ParcelModel parcel) {
        parcelMap.put(parcel.getParcelID(), parcel);
        log.addLog("Parcel added: " + parcel.getParcelID());
    }

    public CustomerModel getNextCustomer() {
        return customerQueue.poll(); // Retrieve and remove the next customer
    }

    public ParcelModel getParcel(String parcelID) {
        return parcelMap.get(parcelID); // Retrieve parcel by ID
    }

    public void updateParcel(ParcelModel parcel) {
        parcelMap.put(parcel.getParcelID(), parcel); // Update parcel information
        log.addLog("Parcel updated: " + parcel.getParcelID());
    }

    public void deleteParcel(String parcelID) {
        parcelMap.remove(parcelID); // Remove parcel by ID
        log.addLog("Parcel deleted: " + parcelID);
    }
}
