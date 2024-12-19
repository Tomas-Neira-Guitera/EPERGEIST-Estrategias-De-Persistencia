package ar.edu.unq.epersgeist.modelo.random;

public class NroFijoGetter implements Randomizer {

    private int numero;

    public NroFijoGetter(int numero){
        this.numero = numero;
    }

    @Override
    public int getNro() {
        return numero;
    }
}
