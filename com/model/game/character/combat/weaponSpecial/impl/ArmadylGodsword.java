package com.model.game.character.combat.weaponSpecial.impl;

import com.model.game.character.Animation;
import com.model.game.character.Entity;
import com.model.game.character.Graphic;
import com.model.game.character.Hit;
import com.model.game.character.combat.Combat;
import com.model.game.character.combat.CombatFormulae;
import com.model.game.character.combat.combat_data.CombatStyle;
import com.model.game.character.combat.weaponSpecial.SpecialAttack;
import com.model.game.character.player.Player;
import com.model.utility.Utility;

public class ArmadylGodsword implements SpecialAttack {

	@Override
	public int[] weapons() {
		return new int[] { 11802, 20593, 20368 };
	}

	@Override
	public void handleAttack(Player player, Entity target) {
		int damage = Utility.random(player.getCombat().calculateMeleeMaxHit());

		player.playAnimation(Animation.create(7061));
		player.playGraphics(Graphic.highGraphic(1211));
		boolean missed = !CombatFormulae.getAccuracy((Entity)player, (Entity)target, 0, getAccuracyMultiplier());
		if (missed)
			damage = 0;
		
		// Set up a Hit instance
        Hit hitInfo = target.take_hit(player, damage, CombatStyle.MELEE).giveXP(player);

        Combat.hitEvent(player, target, 1, hitInfo, CombatStyle.MELEE);
	}

	@Override
	public int amountRequired() {
		return 50;
	}

	@Override
	public boolean meetsRequirements(Player player, Entity target) {
		return true;
	}

	@Override
	public double getAccuracyMultiplier() {
		return 3.0;
	}

	@Override
	public double getMaxHitMultiplier() {
		return 1.375;
	}

}
