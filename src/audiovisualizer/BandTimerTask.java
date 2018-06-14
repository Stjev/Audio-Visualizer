/**
 * Author: Stef De Visscher
 * 6/14/2018
 */
package audiovisualizer;

import java.util.TimerTask;

public class BandTimerTask extends TimerTask {
    private final Band[] bands;

    public BandTimerTask(Band[] bands) {
        this.bands = bands;
    }

    @Override
    public void run() {
        for (Band band : bands) {
            band.incrementHeight();
        }
    }
}
