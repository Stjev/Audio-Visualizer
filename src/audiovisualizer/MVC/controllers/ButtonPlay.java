/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.controllers;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * Button to control the song if it's played or not.
 */
public class ButtonPlay extends Button implements EventHandler<MouseEvent>, InvalidationListener{
    private SimpleBooleanProperty playing;

    public ButtonPlay() {
        setOnMouseClicked(this);
    }

    /**
     * When this button is pressed it pauses / plays the song
     */
    @Override
    public void handle(MouseEvent event) {
        playing.set(! playing.get());
    }

    public boolean isPlaying() {
        return playing.get();
    }

    public SimpleBooleanProperty playingProperty() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing.set(playing);
    }

    public void setPlayingProperty(SimpleBooleanProperty playing) {
        this.playing = playing;
        this.playing.addListener(this);
    }

    /**
     * If the song is paused / played the text is set
     */
    @Override
    public void invalidated(Observable observable) {
        if(playing.get()) setText("Pause");
        else setText("Play");
    }


}
