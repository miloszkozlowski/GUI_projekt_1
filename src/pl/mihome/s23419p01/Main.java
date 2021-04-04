package pl.mihome.s23419p01;

import pl.mihome.s23419p01.menu.Menu;
import pl.mihome.s23419p01.menu.UserMenu;
import pl.mihome.s23419p01.service.DataStock;
import pl.mihome.s23419p01.service.EveryFiveSeconds;
import pl.mihome.s23419p01.service.EveryTenSeconds;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {

        System.out.println("         #####   #####  #          #     #####");
        System.out.println("  ####  #     # #     # #    #    ##    #     #");
        System.out.println(" #            #       # #    #   # #    #     #");
        System.out.println("  ####   #####   #####  #######    #     ######");
        System.out.println("      # #             #      #     #          #");
        System.out.println(" #    # #       #     #      #     #    #     #");
        System.out.println("  ####  #######  #####       #   #####   #####");
        System.out.println();
        System.out.println();

        DataStock dataStock = DataStock.getInstance();
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new EveryFiveSeconds(), 5, 5, TimeUnit.SECONDS);
        // below the task which fails after couple of runs
        ses.scheduleAtFixedRate(new EveryTenSeconds(), 1 , 3, TimeUnit.SECONDS);
        dataStock.init();

        Menu currentScreen = new UserMenu();
        while(currentScreen != null) {
            currentScreen = currentScreen.display();
        }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Autor, Miłosz Kozłowski dziękuje za użytkowanie!");
        System.out.println("================================================");
    }
}
