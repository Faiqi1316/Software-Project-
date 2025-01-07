import java.util.ArrayList;
import java.util.List;

public class ParcelModel {
    private String parcelID;
    private float length;
    private float width;
    private float height;
    private float weight;
    private int daysInDepot;
    private String status;

    public ParcelModel(String parcelID, float length, float width, float height, float weight, int daysInDepot, String status) {
        this.parcelID = parcelID;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = status;
    }

    // Getters and Setters
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

    // CRUD Operations
    private static List<ParcelModel> parcels = new ArrayList<>();

    public static void addParcel(ParcelModel parcel) {
        parcels.add(parcel);
    }

    public static ParcelModel getParcel(String parcelID) {
        for (ParcelModel parcel : parcels) {
            if (parcel.getParcelID().equals(parcelID)) {
                return parcel;
            }
        }
        return null;
    }

    public static void updateParcel(String parcelID, float length, float width, float height, float weight, int daysInDepot, String status) {
        ParcelModel parcel = getParcel(parcelID);
        if (parcel != null) {
            parcel.length = length;
            parcel.width = width;
            parcel.height = height;
            parcel.weight = weight;
            parcel.daysInDepot = daysInDepot;
            parcel.status = status;
        }
    }

    public static void deleteParcel(String parcelID) {
        parcels.removeIf(parcel -> parcel.getParcelID().equals(parcelID));
    }
}
