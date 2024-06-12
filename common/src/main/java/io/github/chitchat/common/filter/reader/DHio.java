package io.github.chitchat.common.filter.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DHio extends Dateihandler {
    @Override
    public String[] lesen() {
        super.lesen();
        try (BufferedReader br =
                new BufferedReader(new FileReader(chooser.getSelectedFile().getName()))) {
            try {
                br.readLine();
                String[] s = new String[0];
                while (br.readLine() != null) {
                    s = br.readLine().split("");
                    System.out.println(s);
                }
                for (String string : s) {
                    return new String[] {string};
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
