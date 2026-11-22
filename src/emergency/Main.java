package emergency;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // City fleet: 1 ICU Ambulance, 1 Basic Ambulance
        DispatchCenter center = new DispatchCenter(1, 1);

        System.out.println("=========================================");
        System.out.println("     CITY AMBULANCE DISPATCH SYSTEM      ");
        System.out.println("=========================================");
        System.out.println("Fleet: 1 ICU Ambulance, 1 Basic Ambulance");

        while (true) {
            System.out.println("\n-----------------------------------------");
            System.out.println("1. Test Call(s)");
            System.out.println("2. Run Demo (Shows Full Flow)");
            System.out.println("3. Exit");
            System.out.print("Choice (1-3): ");

            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("\nHow many calls do you want to enter? ");
                int count = 1;
                try {
                    count = Integer.parseInt(sc.nextLine().trim());
                } catch (Exception e) {
                    count = 1;
                }

                // Collect all calls first — no threads interrupting input
                ArrayList<EmergencyCall> calls = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    System.out.println("\n--- Call " + i + " of " + count + " ---");
                    System.out.print("Location: ");
                    String loc = sc.nextLine().trim();
                    if (loc.isEmpty()) loc = "Street " + i;

                    System.out.print("Severity (Type 'Critical' or press Enter for 'Low'): ");
                    String sev = sc.nextLine().trim();

                    // Constructor Overloading (Auto ID):
                    EmergencyCall call = sev.isEmpty()
                            ? new EmergencyCall(loc)
                            : new EmergencyCall(loc, sev);
                    calls.add(call);
                }

                // Process all collected calls
                System.out.println("\n================ PROCESSING ================");
                for (EmergencyCall c : calls) {
                    center.receiveCall(c);
                }

                // Wait for ambulances to finish before showing menu again
                center.waitForIdle();
                System.out.println("\n================ ALL DONE ==================");

            } else if (choice.equals("2")) {
                runDemo();

            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Please enter 1, 2, or 3.");
            }
        }
        sc.close();
    }

    static void runDemo() {
        System.out.println("\n---------------- DEMO START ----------------");

        EmergencyCall.resetCounter();
        DispatchCenter demo = new DispatchCenter(1, 1);

        System.out.println("\n[1] Low call arrives:");
        demo.receiveCall(new EmergencyCall("Market Road", "Low"));

        System.out.println("\n[2] Another Low call arrives (Basic is busy, ICU is kept for critical):");
        demo.receiveCall(new EmergencyCall("Park Lane", "Low"));

        System.out.println("\n[3] Critical call arrives (ICU is free):");
        demo.receiveCall(new EmergencyCall("Metro Station", "Critical"));

        System.out.println("\n[4] Another Critical call arrives (all ambulances busy):");
        demo.receiveCall(new EmergencyCall("Highway Cross", "Critical"));

        System.out.println("\n[5] Waiting for ambulances to return (Critical served first):");
        demo.waitForIdle();

        System.out.println("\n----------------- DEMO END -----------------");
    }
}
