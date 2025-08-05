package dev.uncandango.alltheleaks.fix.common.mods.kubejs;

import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

@Issue(modId = "kubejs", versionRange = "*", mixins = "main.IngredientKJSMixin")
public class FixItemStackModification {
	public static void transformClass(ClassNode targetClass) {
		var countReturn = 0;
		for (var method : targetClass.methods) {
			if (method.name.equals("kjs$getFirst")) {
				for (var inst : method.instructions) {
					if (inst.getOpcode() == Opcodes.ARETURN && inst.getPrevious().getOpcode() == Opcodes.ALOAD) {
						countReturn++;
						var newMethod = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/item/ItemStack", FMLEnvironment.production ? "m_41777_" : "copy", "()Lnet/minecraft/world/item/ItemStack;", false);
						method.instructions.insertBefore(inst, newMethod);
					}
					if (countReturn > 0) break;
				}
			}
			if (countReturn > 0) break;
		}
	}
}
