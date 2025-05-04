import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.OpFeedbackParam;

import java.util.Random;

public class VlArtifact extends Artifact {

    public static final int BOUND = 2;
    private int estimation;
    private Random random;

    @OPERATION
    public void init(double initial) {
        this.estimation = (int) initial;
        this.random = new Random();
    }

    @OPERATION
    void generateVl() {
        int value;
        log("...generate value");
        try {
            Thread.sleep(random.nextInt(5000));
            value = random.nextInt(estimation - BOUND, estimation + BOUND);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log("...done: " + value);

        if(hasObsProperty("value")) {
            ObsProperty valueObsProp = getObsProperty("value");
            valueObsProp.updateValue(0, value);
        } else {
            defineObsProperty("value", value);
        }
    }

    @OPERATION
    void updateEstimation(int estimation) {
        log("update estimation: " + estimation);
        this.estimation = estimation;
    }

    @OPERATION
    void getEstimation(OpFeedbackParam<Integer> estimation) {
        estimation.set(this.estimation);
    }
}