/**
 * Author: Stef De Visscher
 * 6/14/2018
 */
package audiovisualizer;

import javafx.scene.shape.Rectangle;

public class Band extends Rectangle {

    private final double maxHeight;

    public Band(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.maxHeight = y;
    }

    public void incrementHeight() {
        if(getY() < maxHeight) {
            setY(getY() + 3);
        }
    }

    public void setBandHeight(double y) {
        if(y < getY() && Math.abs(getY() - y) > 30) {
            setY(y);
        }
    }
}
