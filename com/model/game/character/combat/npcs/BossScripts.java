package com.model.game.character.combat.npcs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.model.game.character.combat.npcs.script.AhrimTheBlighted;
import com.model.game.character.combat.npcs.script.Dharoks;
import com.model.game.character.combat.npcs.script.Dragons;
import com.model.game.character.combat.npcs.script.General_Graardor;
import com.model.game.character.combat.npcs.script.Guthan;
import com.model.game.character.combat.npcs.script.Karil;
import com.model.game.character.combat.npcs.script.KetZek;
import com.model.game.character.combat.npcs.script.Sergeant_Grimspike;
import com.model.game.character.combat.npcs.script.Sergeant_Steelwill;
import com.model.game.character.combat.npcs.script.TokXil;
import com.model.game.character.combat.npcs.script.Torag;
import com.model.game.character.combat.npcs.script.TzTokJad;
import com.model.game.character.combat.npcs.script.Verac;


/**
 * All the bosses that have a custom script
 * @author Patrick van Elderen
 * @date Feb, 23-2-2016
 */
public class BossScripts {
	
	private static Map<Integer, AbstractBossCombat> bosses = new HashMap<>();
	
	/*
	 * Godwars
	 */
	private static final General_Graardor GENERAL_GRAARDOR = new General_Graardor(2215);
	private static final Sergeant_Grimspike SERGEANT_GRIMSPIKE = new Sergeant_Grimspike(2218);
	private static final Sergeant_Steelwill SERGEANT_STEELWILL = new Sergeant_Steelwill(2217);
	
	
	private static final KetZek KET_ZEK = new KetZek(3125);
	private static final TokXil TOK_XIL = new TokXil(3121);
	private static final TzTokJad JAD = new TzTokJad(3127);
	
	/*
	 * Barrows NPC's
	 */
	private static final Dharoks DHAROK = new Dharoks(1673);
	private static final Verac VERAC = new Verac(1677);
	private static final Karil KARIL = new Karil(1675);
	private static final Guthan GUTHAN = new Guthan(1674);
	private static final Torag TORAG = new Torag(1676);
	private static final AhrimTheBlighted AHRIM_THE_BLIGHTED = new AhrimTheBlighted(1672);
	//private static final Dragons DRAGONS = new Dragons({247, 252, 264, 268, 270, 273, 274};
	
	static {
		bosses.put(GENERAL_GRAARDOR.npcId, GENERAL_GRAARDOR);
		bosses.put(SERGEANT_STEELWILL.npcId, SERGEANT_STEELWILL);
		bosses.put(SERGEANT_GRIMSPIKE.npcId, SERGEANT_GRIMSPIKE);
		
		bosses.put(KET_ZEK.npcId, KET_ZEK);
		bosses.put(JAD.npcId, JAD);
		bosses.put(TOK_XIL.npcId, TOK_XIL);
		
		
		/*
		 * Barrows NPC's
		 */
		bosses.put(DHAROK.npcId, DHAROK);
		bosses.put(AHRIM_THE_BLIGHTED.npcId, AHRIM_THE_BLIGHTED);
		bosses.put(VERAC.npcId, VERAC);
		bosses.put(KARIL.npcId, KARIL);
		bosses.put(TORAG.npcId, TORAG);
		bosses.put(GUTHAN.npcId, GUTHAN);
	
	}
	
	public static AbstractBossCombat get(int npcId) {
		if (!bosses.containsKey(npcId))
			return null;
		return bosses.get(npcId);
	}
	
	public static boolean isBoss(int npcId) {
		return Objects.nonNull(get(npcId));
	}
}
