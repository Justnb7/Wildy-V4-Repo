package com.model.game.character.player.minigames.BarrowsFull;

import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.AHRIMS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.AHRIMS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.AHRIMS_STAIRS_POSITION;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.DHAROKS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.DHAROKS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.DHAROKS_STAIRS_POSITION;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.GUTHANS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.GUTHANS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.GUTHANS_STAIRS_POSITION;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.KARILS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.KARILS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.KARILS_STAIRS_POSITION;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.TORAGS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.TORAGS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.TORAGS_STAIRS_POSITION;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.VERACS_MOUND_TOP;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.VERACS_STAIRS_ID;
import static com.model.game.character.player.minigames.BarrowsFull.BarrowsConstants.VERACS_STAIRS_POSITION;




import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.model.game.character.player.Player;
import com.model.game.location.Position;


public enum TombStairData {

	AHRIM(AHRIMS_STAIRS_ID, AHRIMS_STAIRS_POSITION, AHRIMS_MOUND_TOP),
	DHAROK(DHAROKS_STAIRS_ID, DHAROKS_STAIRS_POSITION, DHAROKS_MOUND_TOP),
	GUTHAN(GUTHANS_STAIRS_ID, GUTHANS_STAIRS_POSITION, GUTHANS_MOUND_TOP),
	KARIL(KARILS_STAIRS_ID, KARILS_STAIRS_POSITION, KARILS_MOUND_TOP),
	TORAG(TORAGS_STAIRS_ID, TORAGS_STAIRS_POSITION, TORAGS_MOUND_TOP),
	VERAC(VERACS_STAIRS_ID, VERACS_STAIRS_POSITION, VERACS_MOUND_TOP);

	private final int objectID;

	/**
	 * The position above ground to teleport to.
	 */
	private final Position aboveMound;

	/**
	 * The bottom position of the stairs where the player can stand.
	 */
	private final Position position;

	private TombStairData(int objectID, Position stairStart, Position aboveMound) {
		this.objectID = objectID;
		this.position = stairStart;
		this.aboveMound = aboveMound;
	}

	private static final Map<Integer, TombStairData> TOMB_STAIRS = Arrays.stream(TombStairData.values()).collect(
			Collectors.toMap(TombStairData::getObjectID, Function.identity()));

	public static final TombStairData lookup(int objectID) {
		return TOMB_STAIRS.get(objectID);
	}

	public void teleportAbove(Player client) {
		client.teleportToX = aboveMound.getX();
		client.teleportToY = aboveMound.getY();
		client.heightLevel = aboveMound.getZ();
	}

	public Position getAboveMound() {
		return aboveMound;
	}

	public int getObjectID() {
		return objectID;
	}

	public Position getPosition() {
		return position;
	}
}