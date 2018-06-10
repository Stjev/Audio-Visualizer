/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.views;

import audiovisualizer.MVC.models.SongModel;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.File;

public class AudioPlayer implements InvalidationListener, PitchDetectionHandler {
    private File song;

    private SimpleBooleanProperty playing;
    private SongModel songModel;
    private SimpleDoubleProperty freqModel;

    public void setPlaying(SimpleBooleanProperty playing) {
        this.playing = playing;
        this.playing.addListener(this);
    }

    public void setSongModel(SongModel songModel) {
        this.songModel = songModel;
        this.songModel.addListener(this);
    }

    public void setFreqModel(SimpleDoubleProperty freqModel) {
        this.freqModel = freqModel;
    }

    @Override
    public void invalidated(Observable observable) {
        if(songModel.getSong() != null && ! songModel.getSong().equals(song)) {
            // The song changed
            song = songModel.getSong();
        } else {
            playSong();
        }
    }

    private void playSong() {
        // Check if a song is selected
        if(song == null && playing.get()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to select a song first.");
            alert.setHeaderText("Select a song");
            alert.show();
            return;
        }

        float sampleRate = 44100;
        int bufferSize = 1024;
        int overlap = 0;

        final AudioFormat format = new AudioFormat(sampleRate, 16, 1, true,
                true);
        final DataLine.Info dataLineInfo = new DataLine.Info(
                TargetDataLine.class, format);
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {

    }
}
