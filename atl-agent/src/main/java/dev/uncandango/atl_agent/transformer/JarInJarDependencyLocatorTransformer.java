package dev.uncandango.atl_agent.transformer;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.HashSet;
import java.util.Set;

public class JarInJarDependencyLocatorTransformer extends BaseTransformer {
	private final Set<Long> validHashes = new HashSet<>();
	public JarInJarDependencyLocatorTransformer() {
		super("net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator");
		validHashes.add(1608934814L);
	}

	@Override
	public ClassNode transform(ClassLoader loader, String className, ClassNode classNode) {

		LOGGER.info("Trying to transform class {}", className);

		var injected = false;
		var validHash = false;
		var it = classNode.methods.iterator();
		while (it.hasNext()) {
			var method = it.next();
			if (method.name.equals("loadModFileFrom") && method.desc.equals("(Lnet/neoforged/neoforgespi/locating/IModFile;Ljava/nio/file/Path;Lnet/neoforged/neoforgespi/locating/IDiscoveryPipeline;)Ljava/util/Optional;")) {
				if (validHashes.contains(computeMethodHash(method))) validHash = true;
				if (!validHash) continue;
				it.remove();
				injected = true;
			}
		}

		if (injected)
		{
			classNode.methods.removeIf(method -> method.name.equals("<clinit>"));
		}

		if (injected)
		{
			var fieldVisitor = classNode.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "CACHED_MODS", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Lnet/neoforged/neoforgespi/locating/IModFile;>;", null);
			fieldVisitor.visitEnd();
		}

		if (injected)
		{
			var methodVisitor = classNode.visitMethod(ACC_PROTECTED, "loadModFileFrom", "(Lnet/neoforged/neoforgespi/locating/IModFile;Ljava/nio/file/Path;Lnet/neoforged/neoforgespi/locating/IDiscoveryPipeline;)Ljava/util/Optional;", "(Lnet/neoforged/neoforgespi/locating/IModFile;Ljava/nio/file/Path;Lnet/neoforged/neoforgespi/locating/IDiscoveryPipeline;)Ljava/util/Optional<Lnet/neoforged/neoforgespi/locating/IModFile;>;", null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			Label label1 = new Label();
			Label label2 = new Label();
			methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
			Label label3 = new Label();
			Label label4 = new Label();
			methodVisitor.visitTryCatchBlock(label3, label4, label2, "java/lang/Exception");
			methodVisitor.visitLabel(label0);
			methodVisitor.visitLineNumber(59, label0);
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitInsn(ICONST_1);
			methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
			methodVisitor.visitInsn(DUP);
			methodVisitor.visitInsn(ICONST_0);
			methodVisitor.visitVarInsn(ALOAD, 2);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/nio/file/Path", "toString", "()Ljava/lang/String;", true);
			methodVisitor.visitInsn(AASTORE);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/neoforged/neoforgespi/locating/IModFile", "findResource", "([Ljava/lang/String;)Ljava/nio/file/Path;", true);
			methodVisitor.visitVarInsn(ASTORE, 4);
			Label label5 = new Label();
			methodVisitor.visitLabel(label5);
			methodVisitor.visitLineNumber(60, label5);
			methodVisitor.visitTypeInsn(NEW, "java/net/URI");
			methodVisitor.visitInsn(DUP);
			methodVisitor.visitVarInsn(ALOAD, 4);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/nio/file/Path", "toAbsolutePath", "()Ljava/nio/file/Path;", true);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/nio/file/Path", "toUri", "()Ljava/net/URI;", true);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/net/URI", "getRawSchemeSpecificPart", "()Ljava/lang/String;", false);
			methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"jij:\u0001"});
			methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/net/URI", "<init>", "(Ljava/lang/String;)V", false);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/net/URI", "normalize", "()Ljava/net/URI;", false);
			methodVisitor.visitVarInsn(ASTORE, 5);
			Label label6 = new Label();
			methodVisitor.visitLabel(label6);
			methodVisitor.visitLineNumber(61, label6);
			methodVisitor.visitFieldInsn(GETSTATIC, "net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "CACHED_MODS", "Ljava/util/Map;");
			methodVisitor.visitVarInsn(ALOAD, 5);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/net/URI", "toString", "()Ljava/lang/String;", false);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
			methodVisitor.visitTypeInsn(CHECKCAST, "net/neoforged/neoforgespi/locating/IModFile");
			methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/Optional", "ofNullable", "(Ljava/lang/Object;)Ljava/util/Optional;", false);
			methodVisitor.visitVarInsn(ASTORE, 6);
			Label label7 = new Label();
			methodVisitor.visitLabel(label7);
			methodVisitor.visitLineNumber(62, label7);
			methodVisitor.visitVarInsn(ALOAD, 6);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/Optional", "isPresent", "()Z", false);
			methodVisitor.visitJumpInsn(IFEQ, label3);
			methodVisitor.visitVarInsn(ALOAD, 6);
			methodVisitor.visitLabel(label1);
			methodVisitor.visitInsn(ARETURN);
			methodVisitor.visitLabel(label3);
			methodVisitor.visitLineNumber(63, label3);
			methodVisitor.visitFrame(Opcodes.F_APPEND, 3, new Object[]{"java/nio/file/Path", "java/net/URI", "java/util/Optional"}, 0, null);
			methodVisitor.visitLdcInsn("packagePath");
			methodVisitor.visitVarInsn(ALOAD, 4);
			methodVisitor.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableMap", "of", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;", false);
			methodVisitor.visitVarInsn(ASTORE, 7);
			Label label8 = new Label();
			methodVisitor.visitLabel(label8);
			methodVisitor.visitLineNumber(64, label8);
			methodVisitor.visitVarInsn(ALOAD, 5);
			methodVisitor.visitVarInsn(ALOAD, 7);
			methodVisitor.visitMethodInsn(INVOKESTATIC, "java/nio/file/FileSystems", "newFileSystem", "(Ljava/net/URI;Ljava/util/Map;)Ljava/nio/file/FileSystem;", false);
			methodVisitor.visitVarInsn(ASTORE, 8);
			Label label9 = new Label();
			methodVisitor.visitLabel(label9);
			methodVisitor.visitLineNumber(65, label9);
			methodVisitor.visitVarInsn(ALOAD, 8);
			methodVisitor.visitLdcInsn("/");
			methodVisitor.visitInsn(ICONST_0);
			methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/nio/file/FileSystem", "getPath", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;", false);
			methodVisitor.visitMethodInsn(INVOKESTATIC, "cpw/mods/jarhandling/JarContents", "of", "(Ljava/nio/file/Path;)Lcpw/mods/jarhandling/JarContents;", true);
			methodVisitor.visitVarInsn(ASTORE, 9);
			Label label10 = new Label();
			methodVisitor.visitLabel(label10);
			methodVisitor.visitLineNumber(66, label10);
			methodVisitor.visitVarInsn(ALOAD, 3);
			methodVisitor.visitVarInsn(ALOAD, 9);
			methodVisitor.visitFieldInsn(GETSTATIC, "net/neoforged/neoforgespi/locating/ModFileDiscoveryAttributes", "DEFAULT", "Lnet/neoforged/neoforgespi/locating/ModFileDiscoveryAttributes;");
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/neoforged/neoforgespi/locating/ModFileDiscoveryAttributes", "withParent", "(Lnet/neoforged/neoforgespi/locating/IModFile;)Lnet/neoforged/neoforgespi/locating/ModFileDiscoveryAttributes;", false);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/neoforged/neoforgespi/locating/IDiscoveryPipeline", "readModFile", "(Lcpw/mods/jarhandling/JarContents;Lnet/neoforged/neoforgespi/locating/ModFileDiscoveryAttributes;)Lnet/neoforged/neoforgespi/locating/IModFile;", true);
			methodVisitor.visitVarInsn(ASTORE, 10);
			Label label11 = new Label();
			methodVisitor.visitLabel(label11);
			methodVisitor.visitLineNumber(67, label11);
			methodVisitor.visitFieldInsn(GETSTATIC, "net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "CACHED_MODS", "Ljava/util/Map;");
			methodVisitor.visitVarInsn(ALOAD, 5);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/net/URI", "toString", "()Ljava/lang/String;", false);
			methodVisitor.visitVarInsn(ALOAD, 10);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
			methodVisitor.visitInsn(POP);
			Label label12 = new Label();
			methodVisitor.visitLabel(label12);
			methodVisitor.visitLineNumber(68, label12);
			methodVisitor.visitVarInsn(ALOAD, 10);
			methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/Optional", "ofNullable", "(Ljava/lang/Object;)Ljava/util/Optional;", false);
			methodVisitor.visitLabel(label4);
			methodVisitor.visitInsn(ARETURN);
			methodVisitor.visitLabel(label2);
			methodVisitor.visitLineNumber(69, label2);
			methodVisitor.visitFrame(Opcodes.F_FULL, 4, new Object[]{"net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "net/neoforged/neoforgespi/locating/IModFile", "java/nio/file/Path", "net/neoforged/neoforgespi/locating/IDiscoveryPipeline"}, 1, new Object[]{"java/lang/Exception"});
			methodVisitor.visitVarInsn(ASTORE, 4);
			Label label13 = new Label();
			methodVisitor.visitLabel(label13);
			methodVisitor.visitLineNumber(70, label13);
			methodVisitor.visitFieldInsn(GETSTATIC, "net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "LOGGER", "Lorg/slf4j/Logger;");
			methodVisitor.visitLdcInsn("Failed to load mod file {} from {}");
			methodVisitor.visitVarInsn(ALOAD, 2);
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/neoforged/neoforgespi/locating/IModFile", "getFileName", "()Ljava/lang/String;", true);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/slf4j/Logger", "error", "(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", true);
			Label label14 = new Label();
			methodVisitor.visitLabel(label14);
			methodVisitor.visitLineNumber(71, label14);
			methodVisitor.visitTypeInsn(NEW, "net/neoforged/neoforgespi/locating/ModFileLoadingException");
			methodVisitor.visitInsn(DUP);
			methodVisitor.visitVarInsn(ALOAD, 1);
			methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/neoforged/neoforgespi/locating/IModFile", "getFileName", "()Ljava/lang/String;", true);
			methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"Failed to load mod file \u0001"});
			methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/neoforged/neoforgespi/locating/ModFileLoadingException", "<init>", "(Ljava/lang/String;)V", false);
			methodVisitor.visitVarInsn(ASTORE, 5);
			Label label15 = new Label();
			methodVisitor.visitLabel(label15);
			methodVisitor.visitLineNumber(72, label15);
			methodVisitor.visitVarInsn(ALOAD, 5);
			methodVisitor.visitVarInsn(ALOAD, 4);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/RuntimeException", "initCause", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", false);
			methodVisitor.visitInsn(POP);
			Label label16 = new Label();
			methodVisitor.visitLabel(label16);
			methodVisitor.visitLineNumber(74, label16);
			methodVisitor.visitVarInsn(ALOAD, 5);
			methodVisitor.visitInsn(ATHROW);
			Label label17 = new Label();
			methodVisitor.visitLabel(label17);
			methodVisitor.visitLocalVariable("pathInModFile", "Ljava/nio/file/Path;", null, label5, label2, 4);
			methodVisitor.visitLocalVariable("filePathUri", "Ljava/net/URI;", null, label6, label2, 5);
			methodVisitor.visitLocalVariable("cached", "Ljava/util/Optional;", "Ljava/util/Optional<Lnet/neoforged/neoforgespi/locating/IModFile;>;", label7, label2, 6);
			methodVisitor.visitLocalVariable("outerFsArgs", "Lcom/google/common/collect/ImmutableMap;", "Lcom/google/common/collect/ImmutableMap<Ljava/lang/String;Ljava/nio/file/Path;>;", label8, label2, 7);
			methodVisitor.visitLocalVariable("zipFS", "Ljava/nio/file/FileSystem;", null, label9, label2, 8);
			methodVisitor.visitLocalVariable("jar", "Lcpw/mods/jarhandling/JarContents;", null, label10, label2, 9);
			methodVisitor.visitLocalVariable("providerResult", "Lnet/neoforged/neoforgespi/locating/IModFile;", null, label11, label2, 10);
			methodVisitor.visitLocalVariable("exception", "Ljava/lang/RuntimeException;", null, label15, label17, 5);
			methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label13, label17, 4);
			methodVisitor.visitLocalVariable("this", "Lnet/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator;", null, label0, label17, 0);
			methodVisitor.visitLocalVariable("file", "Lnet/neoforged/neoforgespi/locating/IModFile;", null, label0, label17, 1);
			methodVisitor.visitLocalVariable("path", "Ljava/nio/file/Path;", null, label0, label17, 2);
			methodVisitor.visitLocalVariable("pipeline", "Lnet/neoforged/neoforgespi/locating/IDiscoveryPipeline;", null, label0, label17, 3);
			methodVisitor.visitMaxs(5, 11);
			methodVisitor.visitEnd();
		}

		if (injected)
		{
			var methodVisitor = classNode.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			methodVisitor.visitLabel(label0);
			methodVisitor.visitLineNumber(34, label0);
			methodVisitor.visitMethodInsn(INVOKESTATIC, "com/mojang/logging/LogUtils", "getLogger", "()Lorg/slf4j/Logger;", false);
			methodVisitor.visitFieldInsn(PUTSTATIC, "net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "LOGGER", "Lorg/slf4j/Logger;");
			Label label1 = new Label();
			methodVisitor.visitLabel(label1);
			methodVisitor.visitLineNumber(35, label1);
			methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
			methodVisitor.visitInsn(DUP);
			methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
			methodVisitor.visitFieldInsn(PUTSTATIC, "net/neoforged/fml/loading/moddiscovery/locators/JarInJarDependencyLocator", "CACHED_MODS", "Ljava/util/Map;");
			methodVisitor.visitInsn(RETURN);
			methodVisitor.visitMaxs(2, 0);
			methodVisitor.visitEnd();
		}

		// dumpClass(classNode, "JarInJarDependencyLocator");
		return classNode;
	}
}
