
package logikk;

/**
 *
 * Enum-class for describing the different statuses an order can have.
 */
public enum Status {

    PENDING(1, "The order is waiting to be made"),
    UNDER_PREPARATION(2, "The order is being made"),
    PENDING_DELIVERY(3, "The food is waiting for a driver"),
    ON_THE_ROAD(4, "The food is on it's way"),
    FINISHED(5, "The order has been successfully delivered"),
    MISSING(6, "The order is delayed"),
    NEEDS_APPROVAL(7, "Pending"),
    NULL(0, "There is no registered order");
    private int code;
    private String description;
    /**
     * Sets value to the given attributes.
     * @param code Code of status
     * @param description Description of status
     */
    private Status(int code, String description) {
        this.code = code;
        this.description = description;
    }
    /**
     * Finds the name of status given the code.
     * @param code The code of the status.
     * @return Name of status
     */
    public static String getStatusName(int code){
        switch (code) {
            case 1:
                return Status.PENDING.toString();
            case 2:
                return Status.UNDER_PREPARATION.toString();
            case 3:
                return Status.PENDING_DELIVERY.toString();
            case 4:
                return Status.ON_THE_ROAD.toString();
            case 5:
                return Status.FINISHED.toString();
            case 6:
                return Status.MISSING.toString();
            case 7:
                return Status.NEEDS_APPROVAL.toString();
        }
        return null; 
    }
    /**
     * Finds the description of the status given the code.
     * @param code The code of the status
     * @return The description of the status
     */
    public static String getDescription(int code) {
        switch (code) {
            case 1:
                return Status.PENDING.description;
            case 2:
                return Status.UNDER_PREPARATION.description;
            case 3:
                return Status.PENDING_DELIVERY.description;
            case 4:
                return Status.ON_THE_ROAD.description;
            case 5:
                return Status.FINISHED.description;
            case 6:
                return Status.MISSING.description;
            case 7:
                return Status.NEEDS_APPROVAL.description;
        }
        return null; 
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return this.toString();
    }
}