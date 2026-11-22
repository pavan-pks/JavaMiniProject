package emergency;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DispatchCenter center = new DispatchCenter();

        System.out.println("=========================================");
        System.out.println("     CITY AMBULANCE DISPATCH SYSTEM      ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n1. Enter Call(s)");
            System.out.println("2. Run Demo");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("How many calls to enter? ");
                int count = 1;
                try { count = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) {}

                ArrayList<EmergencyCall> calls = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    System.out.println("\n--- Call " + i + " of " + count + " ---");
                    System.out.print("Location: ");
                    String loc = sc.nextLine().trim();
                    if (loc.isEmpty()) loc = "Street " + i;

                    System.out.print("Severity (Type 'Critical' or press Enter for 'Low'): ");
                    String sev = sc.nextLine().trim();

                    // Constructor overloading:
                    calls.add(sev.isEmpty() ? new EmergencyCall(loc) : new EmergencyCall(loc, sev));
                }

                System.out.println("\n============ PROCESSING CALLS ============");
                for (EmergencyCall c : calls) {
                    center.receiveCall(c);
                }
                center.waitForIdle();
                System.out.println("\n============ ALL CALLS COMPLETED ============");

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
        System.out.println("\n================ DEMO START ================");
        EmergencyCall.count = 1;
        DispatchCenter demo = new DispatchCenter();

        System.out.println("1. Low call arrives:");
        demo.receiveCall(new EmergencyCall("Market Road", "Low"));

        System.out.println("\n2. Low call arrives (Basic busy, ICU free):");
        demo.receiveCall(new EmergencyCall("Park Lane", "Low"));

        System.out.println("\n3. Critical call arrives (ICU free):");
        demo.receiveCall(new EmergencyCall("Metro Station", "Critical"));

        System.out.println("\n4. Critical call arrives (all busy):");
        demo.receiveCall(new EmergencyCall("Highway Cross", "Critical"));

        System.out.println("\n--- Processing in Background (Critical Served First) ---");
        demo.waitForIdle();

        System.out.println("\n================= DEMO END =================");
    }
}
