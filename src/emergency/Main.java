package emergency;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // City fleet: 1 ICU Ambulance, 1 Basic Ambulance
        DispatchCenter center = new DispatchCenter(1, 1);

        System.out.println("=========================================");
        System.out.println("     CITY AMBULANCE DISPATCH SYSTEM      ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n1. Enter Emergency Call");
            System.out.println("2. Run Demo");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("Location: ");
                String loc = sc.nextLine().trim();
                if (loc.isEmpty()) loc = "Main Street";

                System.out.print("Severity (Critical / Low, press Enter for Low): ");
                String sev = sc.nextLine().trim();

                EmergencyCall call = sev.isEmpty()
                        ? new EmergencyCall(loc)
                        : new EmergencyCall(loc, sev);

                center.receiveCall(call);
                center.waitForIdle();

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
