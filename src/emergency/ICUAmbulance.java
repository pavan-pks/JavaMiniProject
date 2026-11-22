package emergency;

public class ICUAmbulance extends Ambulance {
    public ICUAmbulance(DispatchCenter center, EmergencyCall call) {
        super("ICU Ambulance", center, call);
    }

    @Override
    public int responseTime() {
        return 5; // 5 mins
    }
}
