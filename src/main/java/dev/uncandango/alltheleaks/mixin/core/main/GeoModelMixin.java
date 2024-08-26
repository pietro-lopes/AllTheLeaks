package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.loading.math.MolangQueries;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtil;

import java.lang.ref.WeakReference;

@Mixin(value = GeoModel.class)
public class GeoModelMixin {

	/**
	 * @author Uncandango
	 * @reason Too 🧠🤏 to proper make an accurate mixin for those changes.
	 */
	@SuppressWarnings("DataFlowIssue")
	@Overwrite(remap = false)
	public <T extends GeoAnimatable> void applyMolangQueries(AnimationState<T> animationState, double animTime) {
		Minecraft mc = Minecraft.getInstance();
		T animatable = animationState.getAnimatable();
		if (mc.level == null) {
			return;
		}
		var level = new WeakReference<>(mc.level);
		MathParser.setVariable(MolangQueries.LIFE_TIME, () -> animTime / 20d);
		MathParser.setVariable(MolangQueries.ACTOR_COUNT, () -> level.get().getEntityCount());
		MathParser.setVariable(MolangQueries.TIME_OF_DAY, () -> level.get().getDayTime() / 24000f);
		MathParser.setVariable(MolangQueries.MOON_PHASE, () -> level.get().getMoonPhase());

		if (animatable instanceof Entity entity) {
			var weakEntity = new WeakReference<>(entity);
			MathParser.setVariable(MolangQueries.DISTANCE_FROM_CAMERA, () -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(weakEntity.get().position()));
			MathParser.setVariable(MolangQueries.IS_ON_GROUND, () -> RenderUtil.booleanToFloat(weakEntity.get().onGround()));
			MathParser.setVariable(MolangQueries.IS_IN_WATER, () -> RenderUtil.booleanToFloat(weakEntity.get().isInWater()));
			MathParser.setVariable(MolangQueries.IS_IN_WATER_OR_RAIN, () -> RenderUtil.booleanToFloat(weakEntity.get().isInWaterRainOrBubble()));

			if (entity instanceof LivingEntity livingEntity) {
				var weakLivingEntity = new WeakReference<>(livingEntity);
				MathParser.setVariable(MolangQueries.HEALTH, () -> weakLivingEntity.get().getHealth());
				MathParser.setVariable(MolangQueries.MAX_HEALTH, () -> weakLivingEntity.get().getMaxHealth());
				MathParser.setVariable(MolangQueries.IS_ON_FIRE, () -> RenderUtil.booleanToFloat(weakLivingEntity.get().isOnFire()));
				MathParser.setVariable(MolangQueries.GROUND_SPEED, () -> {
					Vec3 velocity = weakLivingEntity.get().getDeltaMovement();

					return Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
				});
				MathParser.setVariable(MolangQueries.YAW_SPEED, () -> weakLivingEntity.get().getViewYRot((float) animTime - weakLivingEntity.get().getViewYRot((float) animTime - 0.1f)));
			}
		}
	}
}
