package emergency;

// Dispatchable interface in package emergency
public interface Dispatchable {
    void dispatch(EmergencyCall call) throws NoAmbulanceAvailableException;
}
