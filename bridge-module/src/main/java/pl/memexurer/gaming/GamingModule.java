package pl.memexurer.gaming;

import com.google.inject.name.Named;
import eu.cloudnetservice.driver.database.DatabaseProvider;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.event.events.service.CloudServiceLifecycleChangeEvent;
import eu.cloudnetservice.driver.module.*;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.memexurer.gaming.cloudnet.CloudNetServerPool;
import pl.memexurer.gaming.cloudnet.GameDatabase;
import pl.memexurer.gaming.game.generic.GenericGameData;
import pl.memexurer.gaming.game.generic.GenericGameModifiers;
import pl.memexurer.gaming.game.generic.GenericGameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GamingModule extends DefaultModule {
    private static final Logger logger = LoggerFactory.getLogger(GamingModule.class);
    private GameDatabase gameDatabase;

    @ModuleTask
    public void init(
            EventManager eventManager,
            DatabaseProvider databaseProvider,
            CloudServiceProvider serviceProvider) {
        eventManager.registerListener(this);
        CloudNetServerPool serverPool = new CloudNetServerPool("Castlemode", databaseProvider, serviceProvider);
        this.gameDatabase = serverPool.getGameDatabase();

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (gameDatabase.getAllGames().stream()
                    .filter(game -> game.gameData() instanceof GenericGameData)
                    .noneMatch(game -> ((GenericGameData) game.gameData()).getGameState() == GenericGameState.WAITING)) {
                serverPool.createGame(new GenericGameModifiers("nigger")).whenComplete(
                        (game, throwable) -> {
                            if (throwable != null) {
                                logger.error("Blad podczas tworzenia giery", throwable);
                            }
                        });
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    @EventListener
    public void handleServiceStop(CloudServiceLifecycleChangeEvent event) {
        if (event.newLifeCycle() == ServiceLifeCycle.STOPPED || event.newLifeCycle() == ServiceLifeCycle.DELETED) {
            logger.info("Service " + event.serviceInfo().name() + " has stopped - deleting games...");
            gameDatabase.deleteParent(event.serviceInfo().name());
        }
    }
}
