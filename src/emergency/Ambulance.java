package emergency;

// Abstract Ambulance class extending Thread and implementing Dispatchable
public abstract class Ambulance extends Thread implements Dispatchable {
    public String name;
    public EmergencyCall call;
    public DispatchCenter center;

    public Ambulance(String name, DispatchCenter center, EmergencyCall call) {
        this.name = name;
        this.center = center;
        this.call = call;
        setPriority("Critical".equalsIgnoreCase(call.severity) ? Thread.MAX_PRIORITY : Thread.NORM_PRIORITY);
    }

    public abstract int responseTime();

    @Override
    public void dispatch(EmergencyCall call) {
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(responseTime() * 40); // Simulate travel time
        } catch (InterruptedException ignored) {}

        System.out.println("  [Arrived] " + name + " reached " + call.location + " (" + call.id + ")");

        if (center != null) {
            center.onReturn(this);
        }
    }
}
