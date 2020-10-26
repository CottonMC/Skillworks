package io.github.cottonmc.skillcheck.mixins;

import io.github.cottonmc.cottonrpg.data.rpgclass.CharacterClasses;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResourceEntry;
import io.github.cottonmc.cottonrpg.data.rpgresource.CharacterResources;
import io.github.cottonmc.skillcheck.SkillCheck;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
	@Inject(method = "onPlayerConnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/DifficultyS2CPacket;<init>(Lnet/minecraft/world/Difficulty;Z)V"))
	private void upgradeSkillCheckClasses(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		CharacterClasses classes = CharacterClasses.get(player);
		//add stamina if needed
		if (SkillCheck.config.useStamina) {
			CharacterResources resources = CharacterResources.get(player);
			if (classes.has(SkillCheck.THIEF) && !resources.has(SkillCheck.STAMINA)) {
				int thief = classes.get(SkillCheck.THIEF).getLevel();
				CharacterResourceEntry entry = resources.giveIfAbsent(SkillCheck.STAMINA);
				entry.setMax(10*thief);
				entry.setCurrent(10*thief);
			}
		}
	}
}
