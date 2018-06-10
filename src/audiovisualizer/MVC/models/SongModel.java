/**
 * Stef De Visscher
 */
package audiovisualizer.MVC.models;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SongModel implements Observable{
    private File song;

    public File getSong() {
        return song;
    }

    /**
     * Sets the song and let's every listener know the song changed
     * @param song the chosen song
     */
    public void setSong(File song) {
        if(song != null && ! song.equals(this.song)) {
            this.song = song;
            fireInvalidationEvent();
        }
    }

    private Set<InvalidationListener> listeners = new HashSet<>();

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    // Fires the invalidaded method of every listener
    private void fireInvalidationEvent() {
        listeners.forEach(l -> l.invalidated(this));
    }
}
