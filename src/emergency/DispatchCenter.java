package emergency;

import java.util.ArrayList;

/**
 * Dispatch Center:
 * - Critical: Dispatches ICU (or Basic if ICU busy).
 * - Low: Dispatches ONLY Basic (keeps ICU free for critical cases).
 * - No ambulance free: Throws exception and queues call.
 * - When an ambulance returns: Critical calls served first.
 */
public class DispatchCenter implements Dispatchable {
    public int freeICU = 1;
    public int freeBasic = 1;
    public ArrayList<EmergencyCall> queue = new ArrayList<>();

    public synchronized void receiveCall(EmergencyCall call) {
        System.out.println("\n[" + call.id + "] " + call.location + " (" + call.severity + " Severity)");
        try {
            dispatch(call);
        } catch (NoAmbulanceAvailableException e) {
            queue.add(call);
            System.out.println("  → " + e.getMessage());
            System.out.println("  → " + call.id + " placed in waiting queue.");
        }
    }

    @Override
    public synchronized void dispatch(EmergencyCall call) throws NoAmbulanceAvailableException {
        if ("Critical".equalsIgnoreCase(call.severity)) {
            if (freeICU > 0) {
                freeICU--;
                System.out.println("  → Sent: ICU Ambulance (Priority 10, ETA: 5m)");
                new ICUAmbulance(this, call).dispatch(call);
                return;
            } else if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  → Sent: Basic Ambulance (Priority 10, ETA: 12m) [ICU busy]");
                new BasicAmbulance(this, call).dispatch(call);
                return;
            }
        } else {
            // Low severity -> ONLY Basic!
            if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  → Sent: Basic Ambulance (Priority 5, ETA: 12m)");
                new BasicAmbulance(this, call).dispatch(call);
                return;
            }
        }

        if (!"Critical".equalsIgnoreCase(call.severity) && freeICU > 0) {
            throw new NoAmbulanceAvailableException("Basic busy (ICU kept for Critical)");
        }
        throw new NoAmbulanceAvailableException("All ambulances currently busy");
    }

    public synchronized void onReturn(Ambulance amb) {
        if (amb instanceof ICUAmbulance) freeICU++;
        else freeBasic++;

        // Process queue: Critical first, then Low
        for (String prio : new String[]{"Critical", "Low"}) {
            for (int i = 0; i < queue.size(); i++) {
                EmergencyCall c = queue.get(i);
                if (prio.equalsIgnoreCase(c.severity)) {
                    try {
                        dispatch(c);
                        queue.remove(i);
                        System.out.println("  ✓ Rescued " + prio + " " + c.id + " from queue!");
                        return;
                    } catch (NoAmbulanceAvailableException ignored) {}
                }
            }
        }
    }

    public void waitForIdle() {
        while (freeICU < 1 || freeBasic < 1 || !queue.isEmpty()) {
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
    }
}
