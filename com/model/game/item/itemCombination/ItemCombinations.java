package com.model.game.item.itemCombination;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import com.model.game.item.Item;
import com.model.game.item.itemCombination.impl.SaradominsBlessedSword;

import com.model.game.item.itemCombination.impl.*;

public enum ItemCombinations {
	
	
	
	SARADOMINS_BLESSED_SWORD(
			new SaradominsBlessedSword(Optional.empty(), new Item(12809), 
					Optional.of(Arrays.asList(new Item(11838))),
						new Item[] {new Item(12804), new Item(11838)})
	),

	AMULET_OF_FURY(
			new AmuletOfFury(Optional.empty(), new Item(12436), 
					Optional.of(Arrays.asList(new Item(6585), new Item(12526))),
						new Item[] {new Item(6585), new Item(12526)})
	),
	
	AMULET_OF_TORTURE(
			new AmuletOFTorture(Optional.empty(), new Item(19553), 
					Optional.of(Arrays.asList(new Item(6585), new Item(19496))),
						new Item[] {new Item(6585), new Item(19496)})
	),
	
	NECKLACE_OF_ANGUISH(
			new NecklaceOFAnguish(Optional.empty(), new Item(19547), 
					Optional.of(Arrays.asList(new Item(6565), new Item(19496))),
						new Item[] {new Item(6565), new Item(19496)})
	),
	
	RING_OF_SUFFERING(
			new RingOFSuffering(Optional.empty(), new Item(19550), 
					Optional.of(Arrays.asList(new Item(6575), new Item(19496))),
						new Item[] {new Item(6575), new Item(19496)})
	),
	
	TORMENTED_BRACELET(
			new Tormentedbracelet(Optional.empty(), new Item(19544), 
					Optional.of(Arrays.asList(new Item(11130), new Item(19496))),
						new Item[] {new Item(11130), new Item(19496)})
	),
	
	BLUE_DARK_BOW(
			new BlueDarkBow(Optional.empty(), new Item(12765),
					Optional.empty(),
						new Item[] {new Item(11235), new Item(12757)})
	),
	
	GREEN_DARK_BOW(
			new GreenDarkBow(Optional.empty(), new Item(12766),
					Optional.empty(),
						new Item[] {new Item(11235), new Item(12759)})
	),
	
	YELLOW_DARK_BOW(
			new YellowDarkBow(Optional.empty(), new Item(12767),
					Optional.empty(),
						new Item[] {new Item(11235), new Item(12761)})
	),
	
	WHITE_DARK_BOW(
			new WhiteDarkBow(Optional.empty(), new Item(12768),
					Optional.empty(),
						new Item[] {new Item(11235), new Item(12763)})
	),
	
	MALEDICTION_WARD(
			new MaledictionWard(Optional.empty(), new Item(12806),
					Optional.of(Arrays.asList(new Item(11924))),
						new Item[] {new Item(11924), new Item(12802)})
	),
	
	ODIUM_WARD(
			new OdiumWard(Optional.empty(), new Item(12807),
					Optional.of(Arrays.asList(new Item(11926))),
						new Item[] {new Item(11926), new Item(12802)})
	),
	STEAM_STAFF(
			new SteamStaff(Optional.empty(), new Item(12796),
					Optional.of(Arrays.asList(new Item(11789))),
						new Item[] {new Item(11789), new Item(12798)})
	),
	
	GRANITE_MAUL(
			new GraniteMaul(Optional.empty(), new Item(12848),
					Optional.of(Arrays.asList(new Item(4153))),
						new Item[] {new Item(4153), new Item(12849)})
	),
	
	DRAGON_PICKAXE(
			new DragonPickaxe(Optional.empty(), new Item(12797),
					Optional.of(Arrays.asList(new Item(11920))),
						new Item[] {new Item(12800), new Item(11920)})
	),
	
	BLESSED_SPIRIT_SHIELD(
			new BlessedSpiritShield(Optional.empty(), new Item(12831),
					Optional.empty(), new Item[] {new Item(12829), new Item(12833)})
	),
	
	ARCANE_SPIRIT_SHIELD(
			new ArcaneSpiritShield(Optional.of(new int[] { 13, 85 }), new Item(12825), Optional.empty(), new Item[] {
				new Item(12827), new Item(12831)})
	),
	
	ELYSIAN_SPIRIT_SHIELD(
			new ElysianSpiritShield(Optional.of(new int[] { 13, 85 }), new Item(12817), Optional.empty(), new Item[] {
				new Item(12819), new Item(12831)})
	),
	
	SPECTRAL_SPIRIT_SHIELD(
			new SpectralSpiritShield(Optional.of(new int[] { 13, 85 }), new Item(12821), Optional.empty(), new Item[] {
				new Item(12823), new Item(12831)})
	),
	
	TENTACLE_WHIP(
			new TentacleWhip(Optional.empty(), new Item(12006), Optional.empty(), new Item[] {
				new Item(12004), new Item(4151)})
	),
	
	
	RING_OF_WEALTH_IMBUED(
			new RingOfWealthImbued(Optional.empty(), new Item(12785), Optional.empty(), new Item[] {
				new Item(2572), new Item(12783)})
	),
	
	
	
	INCOMPLETE_HEAVY_BALLISTA(
			new IncompleteBallista(Optional.of(new int[] { 9, 72 }), new Item(19598), Optional.empty(), new Item[] {
				new Item(19592), new Item(19589)})
	),
	
	UNSTRUNG_HEAVY_BALLISTA(
			new UnstrungBallista(Optional.of(new int[] { 9, 72 }), new Item(19607), Optional.empty(), new Item[] {
				new Item(19598), new Item(19601)})
	),
	
	HEAVY_BALLISTA(
			new HeavyBallista(Optional.of(new int[] { 9, 72 }), new Item(19481), Optional.empty(), new Item[] {
				new Item(19607), new Item(19610)})
	);
	
	
	
	private ItemCombination itemCombination;
	
	private ItemCombinations(ItemCombination itemCombination) {
		this.itemCombination = itemCombination;
	}
	
	public ItemCombination getItemCombination() {
		return itemCombination;
	}
	
	static final Set<ItemCombinations> COMBINATIONS = Collections.unmodifiableSet(EnumSet.allOf(ItemCombinations.class));
	
	public static Optional<ItemCombinations> isCombination(Item item1, Item item2) {
		Optional<ItemCombinations> available = COMBINATIONS.stream().filter(combos -> combos.getItemCombination().
				allItemsMatch(item1, item2)).findFirst();
		return available.isPresent() ? available : Optional.empty();
	}
	
	public static Optional<ItemCombination> isRevertable(Item item) {
		Predicate<ItemCombinations> itemMatches = ic -> ic.getItemCombination().getRevertItems().isPresent() &&
				ic.getItemCombination().getOutcome().getId() == item.getId();
		Optional<ItemCombinations> revertable = COMBINATIONS.stream().filter(itemMatches).findFirst();
		if (revertable.isPresent() && revertable.get().getItemCombination().isRevertable()) {
			return Optional.of(revertable.get().getItemCombination());
		}
		return Optional.empty();
	}

}
