/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.views;

import audiovisualizer.MVC.models.SongModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;

/**
 * Label used for displaying the name of a song
 */
public class SongLabel extends Label implements InvalidationListener{
    private SongModel model;

    public void setModel(SongModel model) {
        this.model = model;
        setStyle("-fx-text-fill: #ffffff");
        model.addListener(this);
    }

    @Override
    public void invalidated(Observable observable) {
        setText(model.getSong().getName());
    }
}
