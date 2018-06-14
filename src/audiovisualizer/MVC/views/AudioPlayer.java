/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.views;

import audiovisualizer.Band;
import audiovisualizer.BandTimerTask;
import audiovisualizer.MVC.models.SongModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayer implements InvalidationListener {

    //TODO: Slow down rectangle movement

    private final static int BANDS = 128;
    private final static double REFRESH_RATE = 10; //FPS
    private final static int FREQUENCY_CAP = 18000;
    private final static int CAP = (FREQUENCY_CAP * BANDS) / 20000;
    private File song;

    private SimpleBooleanProperty playing;
    private SongModel songModel;

    private MediaPlayer player;
    private Boolean songLoaded = false;
    private Band[] bands = new Band[CAP];

    private Timer timer;

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

        timer = new Timer();
        timer.scheduleAtFixedRate(new BandTimerTask(bands), 0, 20);
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
        timer.cancel();
    }

    private void spectrumListener(double timestamp,
                                  double duration,
                                  float[] magnitudes,
                                  float[] phases) {
        for(int i = 0; i < CAP; i += 1) {
            /*if(magnitudes[i] > -60) {
                bands[i].setBandHeight(600 - Math.log(60 + magnitudes[i]) * 50);
            }*/

            bands[i].setBandHeight(Math.max(0, 600 - Math.abs(60 + magnitudes[i]) * 13));
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

    private Pane pane;

    /**
     * Set the pane and generate bands
     */
    public void drawOnPane(Pane pane) {
        this.pane = pane;
        for(int i = 0; i < CAP; i += 1) {
            bands[i] = new Band(i*((pane.getPrefWidth() / CAP)), pane.getPrefHeight(),
                    (pane.getPrefWidth() / CAP) - 0.1, pane.getPrefHeight());

            bands[i].setFill(Color.rgb(((i * 255) / CAP - 255) * -1, 0, 255));

            pane.getChildren().add(bands[i]);
        }
    }
}
