package emergency;

import java.util.ArrayList;

/**
 * Dispatch Center:
 * - Critical: Dispatches ICU (or Basic if ICU busy).
 * - Low: Dispatches ONLY Basic (keeps ICU free for critical emergencies).
 * - No ambulance free: Throws exception and queues the call.
 * - Ambulance returns: Queue is checked, Critical calls served FIRST.
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

    // 1. Dispatcher receives an incoming call
    public synchronized void receiveCall(EmergencyCall call) {
        System.out.println("  [CALL]    " + call.id + " | Location: " + call.location + " | Severity: " + call.severity.toUpperCase());
        try {
            dispatch(call);
        } catch (NoAmbulanceAvailableException e) {
            queue.add(call);
            System.out.println("  [BLOCKED] " + e.getMessage());
            System.out.println("  [QUEUED]  " + call.id + " placed in waiting queue (Queue size: " + queue.size() + ")");
        }
    }

    // 2. Dispatch logic based on severity
    @Override
    public synchronized void dispatch(EmergencyCall call) throws NoAmbulanceAvailableException {
        if ("Critical".equalsIgnoreCase(call.severity)) {
            if (freeICU > 0) {
                freeICU--;
                System.out.println("  [ASSIGN]  Dispatched ICU Ambulance for " + call.id);
                new ICUAmbulance("ICU-Unit", this, call).dispatch(call);
                return;
            } else if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  [ASSIGN]  ICU busy -> Dispatched Basic Ambulance for " + call.id);
                new BasicAmbulance("Basic-Unit", this, call).dispatch(call);
                return;
            }
        } else {
            // Low severity -> ONLY Basic!
            if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  [ASSIGN]  Dispatched Basic Ambulance for " + call.id);
                new BasicAmbulance("Basic-Unit", this, call).dispatch(call);
                return;
            }
        }

        throw new NoAmbulanceAvailableException("No suitable ambulance free for " + call.id + " [" + call.severity.toUpperCase() + "]");
    }

    // 3. When an ambulance returns: Critical calls served first!
    public synchronized void onReturn(Ambulance amb) {
        if (amb instanceof ICUAmbulance) freeICU++;
        else freeBasic++;

        System.out.println("  [BASE]    " + amb.name + " returned. Free -> ICU: " + freeICU + ", Basic: " + freeBasic);

        // Check queue: Critical first, then Low
        for (String priority : new String[]{"Critical", "Low"}) {
            for (int i = 0; i < queue.size(); i++) {
                EmergencyCall c = queue.get(i);
                if (priority.equalsIgnoreCase(c.severity)) {
                    try {
                        dispatch(c);
                        queue.remove(i);
                        System.out.println("  [RESUMED] Queued " + priority.toUpperCase() + " call " + c.id + " dispatched!");
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
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }
}
