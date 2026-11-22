package emergency;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Dispatch center with 1 ICU and 1 Basic ambulance
        DispatchCenter center = new DispatchCenter(1, 1);

        System.out.println("=========================================");
        System.out.println("      AMBULANCE DISPATCH SYSTEM (P15)    ");
        System.out.println("=========================================");
        System.out.println("Fleet: 1 ICU Ambulance, 1 Basic Ambulance");

        while (true) {
            System.out.println("\n1. Enter Emergency Call");
            System.out.println("2. Run Demo (Shows Full Flow)");
            System.out.println("3. Exit");
            System.out.print("Choice (1-3): ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Call ID: ");
                String id = sc.nextLine().trim();
                System.out.print("Location: ");
                String loc = sc.nextLine().trim();
                System.out.print("Severity (Critical / Low - press Enter for default Low): ");
                String sev = sc.nextLine().trim();

                // Constructor Overloading:
                EmergencyCall call = sev.isEmpty()
                        ? new EmergencyCall(id, loc)        // Overload 1 (Default "Low")
                        : new EmergencyCall(id, loc, sev);   // Overload 2 (Custom severity)

                center.receiveCall(call);

            } else if (choice.equals("2")) {
                runDemo();

            } else if (choice.equals("3")) {
                System.out.println("Exiting. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Enter 1, 2, or 3.");
            }
        }
        sc.close();
    }

    static void runDemo() {
        System.out.println("\n=======================================================");
        System.out.println("              FLOW DEMONSTRATION                       ");
        System.out.println("=======================================================");
        // Fresh demo center: 1 ICU, 1 Basic
        DispatchCenter demo = new DispatchCenter(1, 1);

        // Step 1: Low call arrives -> Takes Basic
        System.out.println("\n[Step 1] Call 1 (Low) arrives:");
        demo.receiveCall(new EmergencyCall("CALL-1", "MG Road", "Low"));

        // Step 2: Call 2 (Low) arrives while Basic is busy (only ICU is free)
        // Rule: Low dispatches ONLY Basic! Throws exception and adds to queue.
        System.out.println("\n[Step 2] Call 2 (Low) arrives (Basic is busy, ICU is free):");
        demo.receiveCall(new EmergencyCall("CALL-2", "Park Lane", "Low"));

        // Step 3: Call 3 (Critical) arrives -> Free ICU dispatches immediately!
        System.out.println("\n[Step 3] Call 3 (Critical) arrives -> Dispatches to free ICU:");
        demo.receiveCall(new EmergencyCall("CALL-3", "Metro Station", "Critical"));

        // Step 4: Call 4 (Critical) arrives -> Both busy! Throws exception and adds to queue.
        System.out.println("\n[Step 4] Call 4 (Critical) arrives while all ambulances are busy:");
        demo.receiveCall(new EmergencyCall("CALL-4", "Highway Cross", "Critical"));

        // Step 5: Wait for threads to return and observe queue processing (Critical first!)
        System.out.println("\n[Step 5] Waiting for ambulances to return and process queue (Critical first)...");
        try {
            Thread.sleep(3500); // Allow all threads to finish
        } catch (InterruptedException ignored) {}

        System.out.println("\n=======================================================");
        System.out.println("            FLOW DEMONSTRATION COMPLETE                ");
        System.out.println("=======================================================");
    }
}
