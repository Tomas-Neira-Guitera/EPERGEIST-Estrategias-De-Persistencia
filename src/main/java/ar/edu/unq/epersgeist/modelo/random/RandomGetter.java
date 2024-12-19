package ar.edu.unq.epersgeist.modelo.random;

import java.util.Random;

public class RandomGetter implements Randomizer {
    @Override
    public int getNro() {
        Random random = new Random();
        return random.nextInt(10) + 1;
    }
}
