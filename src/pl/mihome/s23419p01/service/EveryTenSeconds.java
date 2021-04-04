package pl.mihome.s23419p01.service;

public class EveryTenSeconds implements Runnable {
    @Override
    public void run() {
        Crawler crawler = new Crawler();
        crawler.crawl();
    }
}
