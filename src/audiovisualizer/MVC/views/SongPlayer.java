/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.views;

import audiovisualizer.MVC.models.SongModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class SongPlayer implements InvalidationListener {

    private final static int BANDS = 128;
    private final static double REFRESH_RATE = 1000; //FPS
    private File song;

    private SimpleBooleanProperty playing;
    private SongModel songModel;

    private MediaPlayer player;
    private Boolean songLoaded = false;
    private Rectangle[] rectangles = new Rectangle[BANDS];

    /**
     * Checks if a song is selected and plays that song
     */
    private void playSong() {
        // Check if a song is selected
        if(song == null && playing.get()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to select a song first.");
            alert.setHeaderText("Select a song");
            alert.show();

            playing.set(false);
            return;
        }

        play();
    }

    /**
     * Plays a song
     */
    private void play() {
        // Check if the song is loaded yet.
        if(! songLoaded) {
            // Get the file to play
            Media media = new Media(song.toURI().toString());
            player = new MediaPlayer(media);

            songLoaded = true;

            player.setAudioSpectrumInterval(1.0 / REFRESH_RATE);
            player.setAudioSpectrumNumBands(BANDS);
            player.setAudioSpectrumListener(this::spectrumListener);
        }

        // Play the song
        player.play();
    }

    /**
     * Pauses the song
     */
    private void pauzeSong(){
        player.pause();
    }

    private void spectrumListener(double timestamp,
                                  double duration,
                                  float[] magnitudes,
                                  float[] phases) {

        for(int i = 0; i < BANDS; i += 1) {
            rectangles[i].yProperty().setValue(Math.abs(magnitudes[i]) * 10);
        }
    }

    /**
     * Invalidated method for listening to the models
     */
    @Override
    public void invalidated(Observable observable) {
        if(songModel.getSong() != null && ! songModel.getSong().equals(song)) {
            // The song changed
            song = songModel.getSong();
            songLoaded = false;
        } else {
            if(this.playing.get()) {
                playSong();
            } else {
                pauzeSong();
            }
        }
    }

    /**
     * Set the model for isPlaying
     */
    public void setPlaying(SimpleBooleanProperty playing) {
        this.playing = playing;
        this.playing.addListener(this);
    }

    /**
     * Set the song model
     */
    public void setSongModel(SongModel songModel) {
        this.songModel = songModel;
        this.songModel.addListener(this);
    }

    /**
     * Set the pane and generate rectangles
     */
    public void drawOnPane(Pane pane) {
        for(int i = 0; i < BANDS; i += 1) {
            rectangles[i] = new Rectangle(i*((pane.getPrefWidth() / BANDS + 0.3)), pane.getPrefHeight(),
                    (pane.getPrefWidth() / BANDS) - 0.3, pane.getPrefHeight());

            rectangles[i].setFill(Color.rgb(((i * 255) / BANDS - 255) * -1, 0, 255));

            pane.getChildren().add(rectangles[i]);
        }
    }
}
