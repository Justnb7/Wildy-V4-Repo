package com.model.game.character.combat.npcs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.model.game.character.combat.npcs.script.AhrimTheBlighted;
import com.model.game.character.combat.npcs.script.Aviansie;
import com.model.game.character.combat.npcs.script.Balfrug_Kreeyath;
import com.model.game.character.combat.npcs.script.Bree;
import com.model.game.character.combat.npcs.script.Commander_Zilyana;
import com.model.game.character.combat.npcs.script.Dharoks;
import com.model.game.character.combat.npcs.script.Dragons;
import com.model.game.character.combat.npcs.script.Flight_Kilisa;
import com.model.game.character.combat.npcs.script.Flockleader_Geerin;
import com.model.game.character.combat.npcs.script.General_Graardor;
import com.model.game.character.combat.npcs.script.Growler;
import com.model.game.character.combat.npcs.script.Guthan;
import com.model.game.character.combat.npcs.script.Karil;
import com.model.game.character.combat.npcs.script.KetZek;
import com.model.game.character.combat.npcs.script.Kree_Arra;
import com.model.game.character.combat.npcs.script.Krill_Tsutsaroth;
import com.model.game.character.combat.npcs.script.Sergeant_Grimspike;
import com.model.game.character.combat.npcs.script.Sergeant_Steelwill;
import com.model.game.character.combat.npcs.script.TokXil;
import com.model.game.character.combat.npcs.script.Torag;
import com.model.game.character.combat.npcs.script.Tstanon_Karlak;
import com.model.game.character.combat.npcs.script.TzTokJad;
import com.model.game.character.combat.npcs.script.Venenatis;
import com.model.game.character.combat.npcs.script.Verac;
import com.model.game.character.combat.npcs.script.Wingman_Skree;
import com.model.game.character.combat.npcs.script.Zakln_Gritch;


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
	
	/*
	 * Bandos
	 */
	private static final General_Graardor GENERAL_GRAARDOR = new General_Graardor(2215);
	private static final Sergeant_Grimspike SERGEANT_GRIMSPIKE = new Sergeant_Grimspike(2218);
	private static final Sergeant_Steelwill SERGEANT_STEELWILL = new Sergeant_Steelwill(2217);
	
	/*
	 * Armadyl
	 */
	private static final Kree_Arra KREE_ARRA = new Kree_Arra(3162);
	private static final Flight_Kilisa FLIGHT_KILISA = new Flight_Kilisa(3165);
	private static final Flockleader_Geerin FLOCKLEADER_GEERIN = new Flockleader_Geerin(3164);
	private static final Wingman_Skree WINGMAN_SKREE = new Wingman_Skree(3163);
	

/*	private static final Aviansie AVIANSIE = new Aviansie(3169);
	private static final Aviansie AVIANSIE2 = new Aviansie(3183);
	private static final Aviansie AVIANSIE3 = new Aviansie(3182);*/
	
	
	/*
	 * Saradomin
	 */
	private static final Commander_Zilyana COMMANDER_ZILYANA = new Commander_Zilyana(2205);
	private static final Bree BREE = new Bree(2208);
	private static final Growler GROWLER = new Growler(2207);
	
	/*
	 * Zamorak
	 */
	
	private static final Balfrug_Kreeyath BALFRUG_KREEYATH = new Balfrug_Kreeyath(3132);
	private static final Zakln_Gritch ZAKLN_GRITCH = new Zakln_Gritch(3131);
	private static final Krill_Tsutsaroth KRILL_TSUTAROTH = new Krill_Tsutsaroth(3129);
	private static final Tstanon_Karlak TSTANON_KARLAK = new Tstanon_Karlak(3130);
	/*
	 * Fight Caves
	 */
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
	
	/*
	 * wildy bosses
	 */
	private static final  Venenatis VENENATIS = new Venenatis(6610);
	//private static final  LizardShaman LIZARDSHAMAN = new LizardShaman(67666);
    
	static {
		/*
		 * Bandos
		 */
		bosses.put(GENERAL_GRAARDOR.npcId, GENERAL_GRAARDOR);
		bosses.put(SERGEANT_STEELWILL.npcId, SERGEANT_STEELWILL);
		bosses.put(SERGEANT_GRIMSPIKE.npcId, SERGEANT_GRIMSPIKE);
		/*
		 * Armadyl
		 */
		bosses.put(WINGMAN_SKREE.npcId, WINGMAN_SKREE);
		bosses.put(FLOCKLEADER_GEERIN.npcId, FLOCKLEADER_GEERIN);
		bosses.put(FLIGHT_KILISA.npcId, FLIGHT_KILISA);
		bosses.put(KREE_ARRA.npcId, KREE_ARRA);
		/*bosses.put(AVIANSIE.npcId, AVIANSIE);
		bosses.put(AVIANSIE2.npcId, AVIANSIE2);
		bosses.put(AVIANSIE3.npcId, AVIANSIE2);*/
		/*
		 * Saradomin
		 */
		bosses.put(COMMANDER_ZILYANA.npcId, COMMANDER_ZILYANA);
		bosses.put(BREE.npcId, BREE);
		bosses.put(GROWLER.npcId, GROWLER);
		
		/*
		 * Zamorak
		 */
		bosses.put(BALFRUG_KREEYATH.npcId, BALFRUG_KREEYATH);
		bosses.put(ZAKLN_GRITCH.npcId, ZAKLN_GRITCH);
		bosses.put(KRILL_TSUTAROTH.npcId, KRILL_TSUTAROTH);
		bosses.put(TSTANON_KARLAK.npcId, TSTANON_KARLAK);
		/*
		 * Fight Caves
		 */
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
	
		/*
		 * wildy bosses
		 */
		bosses.put(VENENATIS.npcId, VENENATIS);
		//bosses.put(LIZARDSHAMAN.npcId, LIZARDSHAMAN);
		
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
