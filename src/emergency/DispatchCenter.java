package emergency;

import java.util.ArrayList;

/**
 * Dispatch Center:
 * 1. Takes call details (id, location, severity).
 * 2. If Critical: dispatches ICU (or Basic if no ICU).
 *    If Low: dispatches ONLY Basic (keeps ICU free for critical emergencies).
 * 3. If no ambulance available: throws exception & adds call to queue.
 * 4. When an ambulance returns: checks queue and dispatches Critical calls first!
 */
public class DispatchCenter implements Dispatchable {
    public int freeICU;
    public int freeBasic;
    public ArrayList<EmergencyCall> queue = new ArrayList<>();

    public DispatchCenter(int icuCount, int basicCount) {
        this.freeICU = icuCount;
        this.freeBasic = basicCount;
    }

    // 1. Dispatcher receives a call
    public synchronized void receiveCall(EmergencyCall call) {
        System.out.println("\n[Dispatcher] Incoming Call: " + call.id + " at " + call.location + " [Severity: " + call.severity + "]");
        try {
            dispatch(call);
        } catch (NoAmbulanceAvailableException e) {
            queue.add(call);
            System.out.println("  [EXCEPTION] " + e.getMessage() + " -> Added to queue. (Queue: " + queue.size() + ")");
        }
    }

    // 2. Dispatch check based on severity
    @Override
    public synchronized void dispatch(EmergencyCall call) throws NoAmbulanceAvailableException {
        if ("Critical".equalsIgnoreCase(call.severity)) {
            if (freeICU > 0) {
                freeICU--;
                System.out.println("  -> Dispatched ICU Ambulance for Call " + call.id);
                new ICUAmbulance("ICU-Ambulance", this, call).dispatch(call);
                return;
            } else if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  -> Dispatched Basic Ambulance for Call " + call.id + " (ICU busy)");
                new BasicAmbulance("Basic-Ambulance", this, call).dispatch(call);
                return;
            }
        } else {
            // Low severity -> ONLY Basic!
            if (freeBasic > 0) {
                freeBasic--;
                System.out.println("  -> Dispatched Basic Ambulance for Call " + call.id);
                new BasicAmbulance("Basic-Ambulance", this, call).dispatch(call);
                return;
            }
        }

        // If no ambulance free, throw custom exception
        throw new NoAmbulanceAvailableException("No suitable ambulance free for Call " + call.id + " [" + call.severity + "]!");
    }

    // 3. When an ambulance returns: check queue (Critical first, then Low)
    public synchronized void onReturn(Ambulance amb) {
        if (amb instanceof ICUAmbulance) freeICU++;
        else freeBasic++;

        System.out.println("<< Ambulance returned to base. Free: [ICU=" + freeICU + ", Basic=" + freeBasic + "]");

        // Check queue: Critical first, then Low
        for (String priority : new String[]{"Critical", "Low"}) {
            for (int i = 0; i < queue.size(); i++) {
                EmergencyCall c = queue.get(i);
                if (priority.equalsIgnoreCase(c.severity)) {
                    try {
                        dispatch(c);
                        queue.remove(i);
                        System.out.println("  -> Dispatched waiting " + priority + " Call " + c.id + " from queue!");
                        return;
                    } catch (NoAmbulanceAvailableException ignored) {}
                }
            }
        }
    }
}
