package com.model.game.regions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;

import com.model.game.character.Entity;
import com.model.game.regions.impl.GodWars;

public class AreaHandler {
	
	private static final List<Areas> areas_set = new ArrayList<>();

	static {
		areas_set.add(new GodWars());
	}

	private static final Optional<Areas> area = areas_set.stream().filter(Objects::nonNull).findAny();

	public static void firstClickObject(Entity player, int object) {
		area.get().sendFirstClickObject(player, object);
	}

	public static void secondClickObject(Entity player, int object) {
		area.get().sendSecondClickObject(player, object);
	}

	public static void thirdClickObject(Entity player, int object) {
		area.get().sendThirdClickObject(player, object);
	}

	public static void firstClickNPC(Entity player, int npc) {
		area.get().sendFirstClickNpc(player, npc);
	}

	public static void secondClickNPC(Entity player, int npc) {
		area.get().sendFirstClickNpc(player, npc);
	}

	public static void thirdClickNPC(Entity player, int npc) {
		area.get().sendFirstClickNpc(player, npc);
	}
	
}
