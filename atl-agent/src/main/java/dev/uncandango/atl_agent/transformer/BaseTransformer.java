package dev.uncandango.atl_agent.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class
BaseTransformer implements ClassFileTransformer, Opcodes {
	protected final Logger LOGGER;
	protected final String targetClass;

	protected BaseTransformer(String targetClass) {
		this.targetClass = targetClass;
		this.LOGGER = LoggerFactory.getLogger(this.getClass());
	}

	public abstract ClassNode transform(ClassLoader loader, String className, ClassNode classNode);

	@Override
	public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		return shouldTransform(className) ? convertToBytes(transform(loader, className, convertToClassNode(classfileBuffer))) : classfileBuffer;
	}

	private boolean shouldTransform(String className) {
		return targetClass.equals(className);
	}

	private ClassNode convertToClassNode(byte[] classfileBuffer) {
		ClassReader classReader = new ClassReader(classfileBuffer);
		ClassNode classNode = new ClassNode();
		classReader.accept(classNode, 0);
		return classNode;
	}

	private byte[] convertToBytes(ClassNode classNode) {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		classNode.accept(classWriter);
		return classWriter.toByteArray();
	}

	protected void dumpClass(ClassNode classNode, String className){
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		try {
			classNode.accept(cw);
		} catch (Throwable e) {
			LOGGER.error(e.getMessage(), e);
		}
		byte[] clazz = cw.toByteArray();

		try {
			final Path tempFile = Files.createTempDirectory("classDump").resolve(className.replaceAll("/", ".") + ".class");
			Files.write(tempFile, clazz);
			LOGGER.debug("Wrote {} byte class file {} to {}", clazz.length, className, tempFile);
		} catch (IOException e) {
			LOGGER.error("Failed to write class file {}", className, e);
		}
	}

	// Very simple hash just to create an identifier
	// just to check if original code was changed
	protected long computeMethodHash(MethodNode methodNode){
		List<String> listString = new ArrayList<>();
		for (var inst : methodNode.instructions) {
			listString.add(inst.getClass().getSimpleName());
			listString.add(Integer.toString(inst.getOpcode()));
			listString.add(Integer.toString(inst.getType()));
			if (inst instanceof MethodInsnNode methodIns) {
				listString.add(methodIns.owner);
				listString.add(methodIns.name);
				listString.add(methodIns.desc);
			}
			if (inst instanceof FieldInsnNode fieldIns) {
				listString.add(fieldIns.owner);
				listString.add(fieldIns.name);
				listString.add(fieldIns.desc);
			}
			if (inst instanceof InvokeDynamicInsnNode invDynIns) {
				listString.add(invDynIns.name);
				listString.add(invDynIns.desc);
				listString.add(invDynIns.bsm.getOwner());
				listString.add(invDynIns.bsm.getName());
				listString.add(invDynIns.bsm.getDesc());
			}
		}
		return Objects.hash(listString.toArray());
	}

}
