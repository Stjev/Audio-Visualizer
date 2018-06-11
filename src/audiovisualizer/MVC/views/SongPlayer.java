/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.views;

import audiovisualizer.MVC.models.SongModel;
import be.tarsos.dsp.*;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SongPlayer implements InvalidationListener, PitchDetectionHandler {
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

    private AudioFormat format;
    private GainProcessor gainProcessor;
    private AudioPlayer audioPlayer;
    private AudioDispatcher dispatcher;
    private WaveformSimilarityBasedOverlapAdd wsola;

    private double pauzedAt;

    private void playSong() {
        // Check if a song is selected
        if(song == null && playing.get()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You need to select a song first.");
            alert.setHeaderText("Select a song");
            alert.show();

            playing.set(false);
            return;
        }

        load();
        play();
    }

    private void load() {
        AudioFileFormat fileFormat;
        try {
            fileFormat = AudioSystem.getAudioFileFormat(song);
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new Error(e);
        }
        format = fileFormat.getFormat();
        pauzedAt = 0;
    }

    private void play(){
        try {
            play(pauzedAt);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private void play(double startTime) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        gainProcessor = new GainProcessor(1.0);

        wsola = new WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.slowdownDefaults(1.0, format.getSampleRate()));

        dispatcher = AudioDispatcherFactory.fromFile(song, wsola.getInputBufferSize(), wsola.getOverlap());

        wsola.setDispatcher(dispatcher);
        dispatcher.skip(startTime);

        dispatcher.addAudioProcessor(wsola);
        dispatcher.addAudioProcessor(gainProcessor);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.YIN, format.getSampleRate(), 1024, this));

        new Thread(dispatcher,"Audio Player Thread").start();
        Media sound = new Media(new File("C:\\Users\\Stef Desktop\\Music\\ADD feat. Emilia Ali (Cudos Remix).mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if(pitchDetectionResult.getPitch() != -1){
            freqModel.setValue(pitchDetectionResult.getPitch() - 23000);
        }
    }
}
