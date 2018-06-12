/**
 * Stef De Visscher
 */
package audiovisualizer;

import audiovisualizer.MVC.controllers.ButtonFile;
import audiovisualizer.MVC.controllers.ButtonPlay;
import audiovisualizer.MVC.models.SongModel;
import audiovisualizer.MVC.views.SongPlayer;
import audiovisualizer.MVC.views.SongLabel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;


public class AVCompanion {

    public Pane pane;
    public ButtonFile btnFile;
    public ButtonPlay btnPlay;
    public SongLabel lblSong;

    // Models
    private SimpleBooleanProperty playing;
    private SongModel songModel;

    // SongPlayer
    private SongPlayer player;

    public void initialize(){
        // Initialize the models
        playing = new SimpleBooleanProperty(false);
        songModel = new SongModel();

        // Initialize an audioplayer
        player = new SongPlayer();
        player.setPlaying(playing);
        player.setSongModel(songModel);
        player.drawOnPane(pane);

        /*// Generates a rectangle
        Rectangle visualBlock = new Rectangle(140, 0, 120, 10);
        visualBlock.heightProperty().bind(freqModel);

        // Adds the rectangle to the pane
        pane.getChildren().add(visualBlock);*/

        btnPlay.setPlayingProperty(playing);
        btnFile.setSongModel(songModel);
        lblSong.setModel(songModel);
    }
}
