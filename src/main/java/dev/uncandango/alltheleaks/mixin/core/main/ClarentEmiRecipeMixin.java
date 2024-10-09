package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.EntityEmiStackFactory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import stepsword.mahoutsukai.integration.emi.ClarentEmiRecipe;

@Pseudo
@Mixin(targets = {"stepsword.mahoutsukai.integration.emi.ClarentEmiRecipe"}, remap = false)
public class ClarentEmiRecipeMixin {
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3))
	private Object addEmiStackWithFactory(Object e) {
		if (e instanceof EntityEmiStackFactory factory) {
			return factory.atl$withFactory((type, level) -> {
				var cloud = (AreaEffectCloud) type.create(level);
				cloud.setParticle(ParticleTypes.DRAGON_BREATH);
				cloud.setRadius(3.0F);
				cloud.setDuration(600);
				cloud.setRadiusPerTick((7.0F - cloud.getRadius()) / (float) cloud.getDuration());
				cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
				return cloud;
			});
		} else {
			return e;
		}
	}
}
