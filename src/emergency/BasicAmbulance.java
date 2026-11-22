package emergency;

// BasicAmbulance overrides responseTime()
public class BasicAmbulance extends Ambulance {

    public BasicAmbulance(String name, DispatchCenter center, EmergencyCall call) {
        super(name, center, call);
    }

    @Override
    public int responseTime() {
        return 12; // 12 minutes
    }
}
