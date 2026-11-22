package emergency;

import java.util.ArrayList;

/**
 * Dispatch Center:
 * - Critical: Dispatches ICU (or Basic if ICU is busy).
 * - Low: Dispatches ONLY Basic (keeps ICU free for critical emergencies).
 * - No ambulance free: Throws exception and adds call to queue.
 * - When an ambulance returns: Checks queue and serves Critical calls first.
 */
public class DispatchCenter implements Dispatchable {
    public int totalICU;
    public int totalBasic;
    public int freeICU;
    public int freeBasic;
    public ArrayList<EmergencyCall> queue = new ArrayList<>();

    public DispatchCenter(int icuCount, int basicCount) {
        this.totalICU = icuCount;
        this.totalBasic = basicCount;
        this.freeICU = icuCount;
        this.freeBasic = basicCount;
    }

    // 1. Receive a call -> dispatch or add to queue
    public synchronized void receiveCall(EmergencyCall call) {
        System.out.println("\nIncoming Call " + call.id + " | Location: " + call.location + " | Severity: " + call.severity);
        try {
            dispatch(call);
        } catch (NoAmbulanceAvailableException e) {
            queue.add(call);
            System.out.println("  Error: " + e.getMessage());
            System.out.println("  " + call.id + " added to queue. (Waiting in queue: " + queue.size() + ")");
        }
    }

    // 2. Dispatch check based on severity
    @Override
    public synchronized void dispatch(EmergencyCall call) throws NoAmbulanceAvailableException {
        if ("Critical".equalsIgnoreCase(call.severity)) {
            if (freeICU > 0) {
                freeICU--;
                System.out.println("  Dispatched: ICU Ambulance for " + call.id);
                new ICUAmbulance("ICU-Ambulance", this, call).dispatch(call);
                return;
            } else if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  Dispatched: Basic Ambulance for " + call.id + " (ICU was busy)");
                new BasicAmbulance("Basic-Ambulance", this, call).dispatch(call);
                return;
            }
        } else {
            // Low severity -> ONLY Basic!
            if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  Dispatched: Basic Ambulance for " + call.id);
                new BasicAmbulance("Basic-Ambulance", this, call).dispatch(call);
                return;
            }
        }

        throw new NoAmbulanceAvailableException("No ambulance free for Call " + call.id + " (" + call.severity + ")");
    }

    // 3. When an ambulance returns: Critical calls served first!
    public synchronized void onReturn(Ambulance amb) {
        if (amb instanceof ICUAmbulance) freeICU++;
        else freeBasic++;

        System.out.println("  Back at base: " + amb.name + " (Free: " + freeICU + " ICU, " + freeBasic + " Basic)");

        // Check queue: Critical first, then Low
        for (String priority : new String[]{"Critical", "Low"}) {
            for (int i = 0; i < queue.size(); i++) {
                EmergencyCall c = queue.get(i);
                if (priority.equalsIgnoreCase(c.severity)) {
                    try {
                        System.out.println("  Picking waiting " + priority + " Call " + c.id + " from queue:");
                        dispatch(c);
                        queue.remove(i);
                        return;
                    } catch (NoAmbulanceAvailableException ignored) {}
                }
            }
        }
    }

    // Waits until all ambulances return and queue is empty
    public void waitForIdle() {
        while (freeICU < totalICU || freeBasic < totalBasic || !queue.isEmpty()) {
            try {
                Thread.sleep(80);
            } catch (InterruptedException ignored) {}
        }
    }
}
