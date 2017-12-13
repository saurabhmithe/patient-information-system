package projects.distributed.happypatients.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import projects.distributed.happypatients.springboot.cache.CacheLoader;

@Component
public class StartupHousekeeper {

    @Autowired
    CacheLoader cacheLoader;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        cacheLoader.initializeCache();
    }
}
