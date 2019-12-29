package Fakehalla.Game;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayerScore {
    private String filename;
    private Scanner scanner;
    private LinkedList<HashElement> scoreMap;

    PlayerScore(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.scanner = new Scanner(new File(this.filename));
        this.scoreMap = new LinkedList<>();
        initHashMap();
        this.scanner.close();
    }

    public LinkedList<HashElement> getScoreMap() {return this.scoreMap; }

    void refreshScore(String playerName) throws IOException {
        if(this.contains(playerName))
        {
            this.scoreMap.stream().filter(e->e.getName().contains(playerName)).forEach(HashElement::increment);
        }
        else{
            this.scoreMap.add(new HashElement(playerName));
        }

        this.scoreMap.sort(HashElement::compareTo);

        BufferedWriter out = new BufferedWriter(new FileWriter(new File(this.filename)));;
        this.scoreMap.forEach(e->{
            try {
                out.write(e.toString());
                out.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        out.close();
    }

    private void initHashMap()
    {
        while(scanner.hasNextLine())
        {
            String s = scanner.nextLine();
            String[] score = s.split(" ");
            this.scoreMap.add(new HashElement(score[0],Integer.parseInt(score[1])));
        }
    }

    private boolean contains(String s)
    {
        for(HashElement he : this.scoreMap)
        {
            if(he.getName().equals(s))
            {
                return true;
            }
        }
        return false;
    }
}
