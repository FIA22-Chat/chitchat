package io.github.chitchat.common.filter.reader;

import java.nio.file.Path;
import javax.swing.JFileChooser;

public abstract class Dateihandler {
    JFileChooser chooser =
            new JFileChooser(
                    "H:\\java\\chitchat\\common\\src\\main\\java\\io\\github\\chitchat\\common\\filter\\list\\spamList");
    Path path;

    public String[] lesen() {
        chooser.showOpenDialog(null);
        System.out.println(chooser + " " + chooser.getSelectedFile().getPath() + "/");
        path = chooser.getSelectedFile().toPath();
        System.out.println(path);
        return null;
    }
}
