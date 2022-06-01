package Fakehalla.Game;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class PlayerScore {
    private String filename;
    private Scanner scanner;
    private LinkedList<HashElement> scoreMap;

    public PlayerScore(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.scanner = new Scanner(new FileReader(this.filename));
        this.scoreMap = new LinkedList<>();
        initHashMap();
        this.scanner.close();
    }

    public LinkedList<HashElement> getScoreMap() {return this.scoreMap; }//returns LinkedList of every player and its score

    void refreshScore(String playerName) throws IOException {
        if(this.contains(playerName))//if the name is already created in the file, it increments the score
        {
            this.scoreMap.stream().filter(e->e.getName().contains(playerName)).forEach(HashElement::increment);
        }
        else{
            this.scoreMap.add(new HashElement(playerName));//if the name is not in the file yet, we add a new name
        }

        this.scoreMap.sort(HashElement::compareTo);// sort

        BufferedWriter out = new BufferedWriter(new FileWriter(new File(this.filename)));;
        this.scoreMap.forEach(e->{ // writing to file every player that is "registered" (has occurred once)
            try {
                out.write(e.toString());// toString is overridden for HashElement, so i can just write the object to file like this
                out.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        out.close();
    }

    private void initHashMap() // init, every time a game is launched, it loads every player with its score to the linkedlist
    {
        while(scanner.hasNextLine())
        {
            String s = scanner.nextLine();
            String[] score = s.split(" ");// {playername playerscore} , separated by " "
            this.scoreMap.add(new HashElement(score[0],Integer.parseInt(score[1])));//0th element is name 1th element is score
        }
    }

    private boolean contains(String s)// checks if the name is already in the linkedlist
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
