package emergency;

public class BasicAmbulance extends Ambulance {
    public BasicAmbulance(DispatchCenter center, EmergencyCall call) {
        super("Basic Ambulance", center, call);
    }

    @Override
    public int responseTime() {
        return 12; // 12 mins
    }
}
