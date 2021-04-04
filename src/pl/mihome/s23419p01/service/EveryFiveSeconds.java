package pl.mihome.s23419p01.service;


public class EveryFiveSeconds implements Runnable {
    @Override
    public void run() {
        DataStock dataStock = DataStock.getInstance();
        dataStock.nextDay();
    }
}
