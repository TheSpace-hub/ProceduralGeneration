package solutions.brilliant.proceduralGeneration.game;

import org.bukkit.event.Event;

public interface Rule {

    void enter();

    void event(Event event);

    void tick();

}
