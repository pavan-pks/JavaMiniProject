package emergency;

// EmergencyCall class with constructor overloading & auto-generated IDs
public class EmergencyCall {
    private static int counter = 1;

    public String id;
    public String location;
    public String severity; // "Critical" or "Low"

    // Overload 1: Location only -> Auto ID + Default "Low" severity
    public EmergencyCall(String location) {
        this("CALL-" + counter++, location, "Low");
    }

    // Overload 2: Location + Severity -> Auto ID + specified severity
    public EmergencyCall(String location, String severity) {
        this("CALL-" + counter++, location, severity);
    }

    // Overload 3: Full parameters (explicit ID, location, severity)
    public EmergencyCall(String id, String location, String severity) {
        this.id = id;
        this.location = location;
        this.severity = (severity == null || severity.trim().isEmpty()) ? "Low" : severity.trim();
    }

    public static void resetCounter() {
        counter = 1;
    }
}
