package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.MolangQueries;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtils;

import java.lang.ref.WeakReference;
import java.util.function.Function;

@SuppressWarnings("DataFlowIssue")
@Pseudo
@Mixin(value = GeoModel.class, remap = false)
public class GeoModelMixin {
    /**
     * @author Uncandango
     * @reason Too 🧠🤏 to proper make an accurate mixin for those changes.
     */
    @Overwrite
    public <T extends GeoAnimatable> void applyMolangQueries(T animatable, double animTime) {
        MolangParser parser = MolangParser.INSTANCE;
        Minecraft mc = Minecraft.getInstance();

        parser.setMemoizedValue(MolangQueries.LIFE_TIME, () -> animTime / 20.0);
        if (mc.level == null) return;
        var level = new WeakReference<>(mc.level);
        parser.setMemoizedValue(MolangQueries.ACTOR_COUNT, () -> level.get().getEntityCount());
        parser.setMemoizedValue(MolangQueries.TIME_OF_DAY, () -> (double) ((float) level.get().getDayTime() / 24000.0F));
        parser.setMemoizedValue(MolangQueries.MOON_PHASE, () -> level.get().getMoonPhase());

		if (animatable instanceof Entity entity) {
            var weakEntity = new WeakReference<>(entity);
            parser.setMemoizedValue(MolangQueries.DISTANCE_FROM_CAMERA, () -> alltheleaks$computeIfPresent(weakEntity.get(), ent -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(ent.position()),0).doubleValue());
            parser.setMemoizedValue(MolangQueries.IS_ON_GROUND, () -> alltheleaks$computeIfPresent(weakEntity.get(), ent -> RenderUtils.booleanToFloat(ent.onGround()), 0).doubleValue());
            parser.setMemoizedValue(MolangQueries.IS_IN_WATER, () -> alltheleaks$computeIfPresent(weakEntity.get(), ent -> RenderUtils.booleanToFloat(ent.isInWater()),0).doubleValue());
            parser.setMemoizedValue(MolangQueries.IS_IN_WATER_OR_RAIN, () -> alltheleaks$computeIfPresent(weakEntity.get(), ent -> RenderUtils.booleanToFloat(ent.isInWaterRainOrBubble()),0).doubleValue());

			if (entity instanceof LivingEntity livingEntity) {
                var weakLivingEntity = new WeakReference<>(livingEntity);
                parser.setMemoizedValue(MolangQueries.HEALTH, () -> alltheleaks$computeIfPresent(weakLivingEntity.get(), LivingEntity::getHealth,0).doubleValue());
                parser.setMemoizedValue(MolangQueries.MAX_HEALTH, () -> alltheleaks$computeIfPresent(weakLivingEntity.get(), LivingEntity::getMaxHealth, 0).doubleValue());
                parser.setMemoizedValue(MolangQueries.IS_ON_FIRE, () -> alltheleaks$computeIfPresent(weakLivingEntity.get(), living -> (double) RenderUtils.booleanToFloat(living.isOnFire()), 0).doubleValue());
                parser.setMemoizedValue(MolangQueries.GROUND_SPEED, () -> {
                    Vec3 velocity = alltheleaks$computeIfPresent(weakLivingEntity.get(), LivingEntity::getDeltaMovement, Vec3.ZERO);
                    return Mth.sqrt((float) (velocity.x * velocity.x + velocity.z * velocity.z));
                });
                parser.setMemoizedValue(
					MolangQueries.YAW_SPEED, () -> alltheleaks$computeIfPresent(weakLivingEntity.get(), living -> living.getViewYRot((float) animTime - living.getViewYRot((float) animTime - 0.1F)),0).doubleValue()
                );
            }
        }
    }

	@Unique
	private static <T,R> R alltheleaks$computeIfPresent(T entity, Function<T, R> function, R defaultValue) {
		return entity == null ? defaultValue : function.apply(entity);
	}
}
