package ar.edu.unq.epersgeist.modelo.random;

public class Random {
    private static Random instance;

    private Randomizer strategy;

    private Random() {
        this.strategy = new RandomGetter();
    }

    public static Random getInstance(){
        if(instance == null){
            instance = new Random();
        }
        return instance;
    }

    public Randomizer getStrategy() {
        return strategy;
    }

    public void setStrategy(Randomizer strategy) {
        this.strategy = strategy;
    }

}
