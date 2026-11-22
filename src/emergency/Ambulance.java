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

        // Critical calls get higher thread priority (10)
        if ("Critical".equalsIgnoreCase(call.severity)) {
            this.setPriority(Thread.MAX_PRIORITY); // 10
        } else {
            this.setPriority(Thread.NORM_PRIORITY); // 5
        }
    }

    public abstract int responseTime();

    @Override
    public void dispatch(EmergencyCall call) {
        this.call = call;
        this.start();
    }

    @Override
    public void run() {
        System.out.println("  [TRANSIT] " + name + " (Thread Priority: " + getPriority() +
                ") heading to " + call.id + " at " + call.location +
                " [ETA: " + responseTime() + "m]");

        try {
            Thread.sleep(responseTime() * 70); // Simulated response time
        } catch (InterruptedException ignored) {}

        System.out.println("  [ARRIVED] " + name + " completed " + call.id + " at " + call.location + ".");

        // Alert dispatch center that ambulance is back
        if (center != null) {
            center.onReturn(this);
        }
    }
}
