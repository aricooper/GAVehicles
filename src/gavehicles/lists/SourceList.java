package gavehicles.lists;

import gavehicles.abstracts.AbstractSource;
import java.awt.Graphics;
import java.util.ArrayList;

public class SourceList extends ArrayList<AbstractSource> {

    public void paint(Graphics g) {
        for (AbstractSource s : this) {
            s.paint(g);
        }
    }

}
