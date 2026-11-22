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
        System.out.println("  Running: " + name + " (Priority " + getPriority() + ") -> " +
                call.id + " at " + call.location + " (ETA: " + responseTime() + " mins)");

        try {
            Thread.sleep(responseTime() * 50); // Simulated travel time
        } catch (InterruptedException ignored) {}

        System.out.println("  Finished: " + name + " completed " + call.id);

        if (center != null) {
            center.onReturn(this);
        }
    }
}
