package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.joefoxe.hexerei.data.books.BookEntity;
import net.joefoxe.hexerei.data.books.BookHoverOffset;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(BookEntity.class)
public class BookEntityMixin implements UpdateableLevel<BookEntity> {
	@Shadow
	public Entity entity;

	@Shadow
	public String entityType;

	@Shadow
	public CompoundTag entityTags;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabObject(float scale, float x, float y, String entityType, Entity entity, CompoundTag entityTags, ArrayList entityTagsList, BookHoverOffset offset, CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null) {
			Optional<EntityType<?>> optionalEntityType = EntityType.byString(this.entityType);
			if (optionalEntityType.isPresent()) {
				Entity entity = optionalEntityType.get().create(level);
				if (entity instanceof LivingEntity livingEntityx) {
					this.entity = entity;
					if (!this.entityTags.isEmpty()) {
						livingEntityx.load(this.entityTags);
					}
				} else {
					this.entity = entity;
					if (!this.entityTags.isEmpty() && entity != null) {
						entity.load(this.entityTags);
					}
				}
			}
		} else {
			entity = null;
		}
	}
}
