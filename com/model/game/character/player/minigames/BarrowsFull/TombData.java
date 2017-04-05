package com.model.game.character.player.minigames.BarrowsFull;


import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.AHRIMS_TOMB_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.DHAROKS_TOMB_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.GUTHANS_TOMB_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.KARILS_TOMB_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.TORAGS_TOMB_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.VERACS_TOMB_ID;


import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum TombData {

	AHRIM(AHRIMS_TOMB_ID, Brother.AHRIM),
	DHAROK(DHAROKS_TOMB_ID, Brother.DHAROK),
	GUTHAN(GUTHANS_TOMB_ID, Brother.GUTHAN),
	KARIL(KARILS_TOMB_ID, Brother.KARIL),
	TORAG(TORAGS_TOMB_ID, Brother.TORAG),
	VERAC(VERACS_TOMB_ID, Brother.VERAC);

	private final int tombID;
	private final Brother brother;

	private TombData(int tombID, Brother brother) {
		this.tombID = tombID;
		this.brother = brother;
	}

	private static final Map<Integer, TombData> TOMBS = Arrays.stream(TombData.values()).collect(
			Collectors.toMap(TombData::getTombID, Function.identity()));

	public static final TombData lookup(int tombID) {
		return TOMBS.get(tombID);
	}

	public int getTombID() {
		return tombID;
	}

	public Brother getBrother() {
		return brother;
	}
}