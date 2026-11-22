package emergency;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // City fleet: 1 ICU Ambulance, 1 Basic Ambulance
        DispatchCenter center = new DispatchCenter(1, 1);

        System.out.println("=================================================");
        System.out.println("        CITY AMBULANCE DISPATCH SYSTEM (P15)     ");
        System.out.println("=================================================");
        System.out.println("Fleet Status: 1 ICU Unit, 1 Basic Unit");

        while (true) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("1. Enter Emergency Call");
            System.out.println("2. Run Flow Demonstration");
            System.out.println("3. Exit");
            System.out.print("Select choice (1-3): ");

            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.println("\n--- New Emergency Call ---");
                System.out.print("Location: ");
                String loc = sc.nextLine().trim();
                if (loc.isEmpty()) loc = "Central Square";

                System.out.print("Severity (Type 'Critical' or press Enter for 'Low'): ");
                String sev = sc.nextLine().trim();

                // Constructor Overloading with Auto-Generated Call ID:
                EmergencyCall call = sev.isEmpty()
                        ? new EmergencyCall(loc)        // Overload 1: Auto ID + Default Low
                        : new EmergencyCall(loc, sev);  // Overload 2: Auto ID + Specific Severity

                System.out.println();
                center.receiveCall(call);

                // Wait for the dispatched ambulance to finish before re-prompting menu
                center.waitForIdle();

            } else if (choice.equals("2")) {
                runDemo();

            } else if (choice.equals("3")) {
                System.out.println("\nExiting Ambulance Dispatch System. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        sc.close();
    }

    static void runDemo() {
        System.out.println("\n=======================================================");
        System.out.println("             STEP-BY-STEP FLOW DEMONSTRATION           ");
        System.out.println("=======================================================");

        EmergencyCall.resetCounter();
        DispatchCenter demo = new DispatchCenter(1, 1);

        // Step 1
        System.out.println("\n--- Step 1: Low-severity call arrives ---");
        demo.receiveCall(new EmergencyCall("Market Road", "Low"));

        // Step 2
        System.out.println("\n--- Step 2: Second Low-severity call arrives (Basic busy, ICU free) ---");
        System.out.println("  [RULE] Low severity dispatches ONLY Basic. ICU remains reserved!");
        demo.receiveCall(new EmergencyCall("Park Lane", "Low"));

        // Step 3
        System.out.println("\n--- Step 3: Critical call arrives ---");
        demo.receiveCall(new EmergencyCall("Metro Station", "Critical"));

        // Step 4
        System.out.println("\n--- Step 4: Another Critical call arrives while all units are busy ---");
        demo.receiveCall(new EmergencyCall("Highway Cross", "Critical"));

        // Step 5
        System.out.println("\n--- Step 5: Units return & process queue (Critical served FIRST) ---");
        System.out.println("  Waiting queue contains: CALL-2 (Low) and CALL-4 (Critical)");
        demo.waitForIdle();

        System.out.println("\n=======================================================");
        System.out.println("               DEMONSTRATION COMPLETED                 ");
        System.out.println("=======================================================");
    }
}
