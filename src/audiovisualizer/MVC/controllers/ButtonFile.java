/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.controllers;

import audiovisualizer.MVC.models.SongModel;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class ButtonFile extends Button implements EventHandler<MouseEvent> {
    private SongModel songModel;

    public ButtonFile() {
        setOnMouseClicked(this);
    }

    public SongModel getSongModel() {
        return songModel;
    }

    public void setSongModel(SongModel songModel) {
        this.songModel = songModel;
    }

    @Override
    public void handle(MouseEvent event) {
        FileChooser chooser = new FileChooser();
        // Only able to select .mp3 & .wav files
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("mp3 audio files (*.mp3)", "*.mp3");
        chooser.getExtensionFilters().add(filter);
        chooser.titleProperty().setValue("Search a song");
        // Open a window to search for a song
        File song = chooser.showOpenDialog(this.getContextMenu());

        if(song != null) {
            songModel.setSong(song);
        }
    }
}
