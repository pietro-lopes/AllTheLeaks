package dev.uncandango.alltheleaks.mixin.core.main;

import com.mna.items.armor.FeyArmorItem;
import com.mna.items.renderers.FeyArmorRenderer;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

@Mixin(value = FeyArmorRenderer.class, remap = false)
public abstract class FeyArmorRendererMixin extends GeoArmorRenderer<FeyArmorItem> {

	public FeyArmorRendererMixin(GeoModel<FeyArmorItem> model) {
		super(model);
	}

	@Override
	public void doPostRenderCleanup() {
		FeyArmorItem.renderEntity = null;
	}
}
