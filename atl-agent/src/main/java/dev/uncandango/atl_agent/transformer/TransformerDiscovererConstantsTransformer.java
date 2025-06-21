package dev.uncandango.atl_agent.transformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.HashSet;
import java.util.Set;

public class TransformerDiscovererConstantsTransformer extends BaseTransformer {
	private final Set<Long> validHashes = new HashSet<>();
	public TransformerDiscovererConstantsTransformer() {
		super("net/neoforged/fml/loading/TransformerDiscovererConstants");
		validHashes.add(-1440172211L);
	}

	@Override
	public ClassNode transform(ClassLoader loader, String className, ClassNode classNode) {

		LOGGER.info("Trying to transform class {}", className);

		// This is a fix for: https://github.com/neoforged/FancyModLoader/issues/289

		/** This injects a method call:
		 *  closeResource(jarContents);
		 * */
		var injected = false;
		var validHash = false;
		for (var method : classNode.methods) {
			if (method.name.equals("shouldLoadInServiceLayer") && method.desc.equals("(Lcpw/mods/jarhandling/JarContents;)Z")) {
				LOGGER.info("Method hash is {}", computeMethodHash(method));
				if (validHashes.contains(computeMethodHash(method))) validHash = true;
				if (!validHash) continue;
				for (var inst : method.instructions) {
					if (inst.getOpcode() == Opcodes.INVOKEINTERFACE) {
						var listInst = new InsnList();
						var param = new VarInsnNode(Opcodes.ALOAD, 0);
						var methodCall = new MethodInsnNode(INVOKESTATIC, "net/neoforged/fml/loading/TransformerDiscovererConstants", "closeResource", "(Ljava/lang/AutoCloseable;)V", false);
						listInst.add(param);
						listInst.add(methodCall);
						method.instructions.insertBefore(inst.getPrevious(), listInst);
						injected = true;
						break;
					}
				}
				if (injected) break;
			}
		}


		/** ASM for:
		 * 	private static void closeResource(AutoCloseable closeable) {
		 * 		try {
		 * 			closeable.close();
		 * 		} catch (Exception ignore) {}
		 *	}
		 * */
		if (validHash)
		{
			var methodVisitor = classNode.visitMethod(ACC_PRIVATE | ACC_STATIC, "closeResource", "(Ljava/lang/AutoCloseable;)V", null, null);
			Label label0 = new Label();
			Label label1 = new Label();
			Label label2 = new Label();
			methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
			methodVisitor.visitLabel(label0);
			methodVisitor.visitVarInsn(ALOAD, 0);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/lang/AutoCloseable", "close", "()V", true);
			methodVisitor.visitLabel(label1);
			Label label3 = new Label();
			methodVisitor.visitJumpInsn(GOTO, label3);
			methodVisitor.visitLabel(label2);
			methodVisitor.visitVarInsn(ASTORE, 1);
			methodVisitor.visitLabel(label3);
			methodVisitor.visitInsn(RETURN);
			Label label4 = new Label();
			methodVisitor.visitLabel(label4);
			methodVisitor.visitLocalVariable("closeable", "Ljava/lang/AutoCloseable;", null, label0, label4, 0);
			LOGGER.info("Transformed class {}", className);
		}

		// dumpClass(classNode, "TransformerDiscovererConstants");
		return classNode;
	}
}
