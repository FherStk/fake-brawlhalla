package Fakehalla.Game;

public class HashElement implements Comparable<HashElement> {
    private int score;
    private String name;

    HashElement(String name)
    {
        this.name = name;
        this.score = 1;
    }
    HashElement(String name, int score)
    {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void increment() {this.score+=1;}

    @Override
    public int compareTo(HashElement hs) {
        return hs.getScore() - this.getScore();
    }
    @Override
    public String toString()
    {
        return this.name + " " + this.score;
    }
}
