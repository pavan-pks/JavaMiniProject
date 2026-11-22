package emergency;

// ICUAmbulance overrides responseTime()
public class ICUAmbulance extends Ambulance {

    public ICUAmbulance(String name, DispatchCenter center, EmergencyCall call) {
        super(name, center, call);
    }

    @Override
    public int responseTime() {
        return 5; // 5 minutes
    }
}
