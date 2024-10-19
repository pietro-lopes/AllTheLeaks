package dev.uncandango.alltheleaks.mixin.core.main;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.molang.MolangQueries;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtils;

import java.lang.ref.WeakReference;

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
            parser.setMemoizedValue(MolangQueries.DISTANCE_FROM_CAMERA, () -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(weakEntity.get().position()));
            parser.setMemoizedValue(MolangQueries.IS_ON_GROUND, () -> (double) RenderUtils.booleanToFloat(weakEntity.get().onGround()));
            parser.setMemoizedValue(MolangQueries.IS_IN_WATER, () -> (double) RenderUtils.booleanToFloat(weakEntity.get().isInWater()));
            parser.setMemoizedValue(MolangQueries.IS_IN_WATER_OR_RAIN, () -> (double) RenderUtils.booleanToFloat(weakEntity.get().isInWaterRainOrBubble()));

			if (entity instanceof LivingEntity livingEntity) {
                var weakLivingEntity = new WeakReference<>(livingEntity);
                parser.setMemoizedValue(MolangQueries.HEALTH, () -> weakLivingEntity.get().getHealth());
                parser.setMemoizedValue(MolangQueries.MAX_HEALTH, () -> weakLivingEntity.get().getMaxHealth());
                parser.setMemoizedValue(MolangQueries.IS_ON_FIRE, () -> (double) RenderUtils.booleanToFloat(weakLivingEntity.get().isOnFire()));
                parser.setMemoizedValue(MolangQueries.GROUND_SPEED, () -> {
                    Vec3 velocity = weakLivingEntity.get().getDeltaMovement();
                    return Mth.sqrt((float) (velocity.x * velocity.x + velocity.z * velocity.z));
                });
                parser.setMemoizedValue(
					MolangQueries.YAW_SPEED, () -> (double) weakLivingEntity.get().getViewYRot((float) animTime - weakLivingEntity.get().getViewYRot((float) animTime - 0.1F))
                );
            }
        }
    }
}
