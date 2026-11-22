package emergency;

// Class with constructor overloading & auto ID
public class EmergencyCall {
    public static int count = 1;
    public String id, location, severity;

    // Overload 1: Default "Low" severity
    public EmergencyCall(String location) {
        this(location, "Low");
    }

    // Overload 2: Custom severity
    public EmergencyCall(String location, String severity) {
        this.id = "Call-" + count++;
        this.location = location;
        this.severity = (severity == null || severity.trim().isEmpty()) ? "Low" : severity.trim();
    }
}
