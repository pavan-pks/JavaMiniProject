package emergency;

// EmergencyCall class with constructor overloading
public class EmergencyCall {
    public String id;
    public String location;
    public String severity; // "Critical" or "Low"

    // Constructor 1: Default severity is "Low"
    public EmergencyCall(String id, String location) {
        this(id, location, "Low");
    }

    // Constructor 2: Explicit severity
    public EmergencyCall(String id, String location, String severity) {
        this.id = id;
        this.location = location;
        this.severity = severity;
    }

    public void display() {
        System.out.println("Call " + id + " | Location: " + location + " | Severity: " + severity);
    }
}
