package emergency;

// Custom Exception
public class NoAmbulanceAvailableException extends Exception {
    NoAmbulanceAvailableException(String msg) {
        super(msg);
    }
}
