package dev.uncandango.alltheleaks.utils;

import org.objectweb.asm.tree.*;
import org.objectweb.asm.ConstantDynamic;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MethodNodeHasher {
	private static final int FNV_PRIME = 0x01000193;

	public synchronized static int hash(MethodNode method) {
		int hash = 0x811c9dc5; // FNV offset basis

		Map<LabelNode, Integer> labelIds = new IdentityHashMap<>();
		int[] labelCounter = {0};

		for (AbstractInsnNode insn : method.instructions) {
			if (insn instanceof LineNumberNode || insn instanceof FrameNode) {
				continue; // skip non-semantic
			}

			if (insn instanceof LabelNode) {
				labelIds.computeIfAbsent((LabelNode) insn, k -> labelCounter[0]++);
				continue;
			}

			// Always include opcode
			hash = fnvUpdate(hash, insn.getOpcode());

			switch (insn.getType()) {
				case AbstractInsnNode.VAR_INSN -> {
					hash = fnvUpdate(hash, ((VarInsnNode) insn).var);
				}
				case AbstractInsnNode.FIELD_INSN -> {
					FieldInsnNode f = (FieldInsnNode) insn;
					hash = fnvUpdate(hash, f.owner);
					hash = fnvUpdate(hash, f.name);
					hash = fnvUpdate(hash, f.desc);
				}
				case AbstractInsnNode.METHOD_INSN -> {
					MethodInsnNode m = (MethodInsnNode) insn;
					hash = fnvUpdate(hash, m.owner);
					hash = fnvUpdate(hash, m.name);
					hash = fnvUpdate(hash, m.desc);
					hash = fnvUpdate(hash, m.itf ? 1 : 0);
				}
				case AbstractInsnNode.LDC_INSN -> {
					Object cst = ((LdcInsnNode) insn).cst;
					updateLdcConstant(hash, cst);
					hash = updateLdcConstant(hash, cst);
				}
				case AbstractInsnNode.INT_INSN -> {
					hash = fnvUpdate(hash, ((IntInsnNode) insn).operand);
				}
				case AbstractInsnNode.TYPE_INSN -> {
					hash = fnvUpdate(hash, ((TypeInsnNode) insn).desc);
				}
				case AbstractInsnNode.JUMP_INSN -> {
					JumpInsnNode j = (JumpInsnNode) insn;
					int labelId = labelIds.computeIfAbsent(j.label, k -> labelCounter[0]++);
					hash = fnvUpdate(hash, labelId);
				}
				case AbstractInsnNode.IINC_INSN -> {
					IincInsnNode iinc = (IincInsnNode) insn;
					hash = fnvUpdate(hash, iinc.var);
					hash = fnvUpdate(hash, iinc.incr);
				}
				case AbstractInsnNode.INVOKE_DYNAMIC_INSN -> {
					InvokeDynamicInsnNode indy = (InvokeDynamicInsnNode) insn;
					hash = fnvUpdate(hash, indy.name);
					hash = fnvUpdate(hash, indy.desc);
					hash = fnvUpdate(hash, indy.bsm.toString());
					for (Object arg : indy.bsmArgs) {
						hash = fnvUpdate(hash, String.valueOf(arg));
					}
				}
				case AbstractInsnNode.MULTIANEWARRAY_INSN -> {
					MultiANewArrayInsnNode multi = (MultiANewArrayInsnNode) insn;
					hash = fnvUpdate(hash, multi.desc);
					hash = fnvUpdate(hash, multi.dims);
				}
				case AbstractInsnNode.LOOKUPSWITCH_INSN -> {
					LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insn;
					int dfltId = labelIds.computeIfAbsent(lsi.dflt, k -> labelCounter[0]++);
					hash = fnvUpdate(hash, dfltId);
					for (int i = 0; i < lsi.keys.size(); i++) {
						hash = fnvUpdate(hash, lsi.keys.get(i));
						int lid = labelIds.computeIfAbsent(lsi.labels.get(i), k -> labelCounter[0]++);
						hash = fnvUpdate(hash, lid);
					}
				}
				case AbstractInsnNode.TABLESWITCH_INSN -> {
					TableSwitchInsnNode tsi = (TableSwitchInsnNode) insn;
					hash = fnvUpdate(hash, tsi.min);
					hash = fnvUpdate(hash, tsi.max);
					int defId = labelIds.computeIfAbsent(tsi.dflt, k -> labelCounter[0]++);
					hash = fnvUpdate(hash, defId);
					for (LabelNode l : tsi.labels) {
						int lid = labelIds.computeIfAbsent(l, k -> labelCounter[0]++);
						hash = fnvUpdate(hash, lid);
					}
				}
			}
		}

		return hash;
	}

	private static int fnvUpdate(int hash, int value) {
		hash ^= value;
		return hash * FNV_PRIME;
	}

	private static int fnvUpdate(int hash, String str) {
		if (str == null) return hash;
		byte[] data = str.getBytes(StandardCharsets.UTF_8);
		for (byte b : data) {
			hash ^= (b & 0xff);
			hash *= FNV_PRIME;
		}
		return hash;
	}

	private static int updateLdcConstant(int hash, Object cst) {
		if (cst instanceof ConstantDynamic cd) {
			// Include name, descriptor, bsm, and args
			hash = fnvUpdate(hash, cd.getName());
			hash = fnvUpdate(hash, cd.getDescriptor());
			hash = fnvUpdate(hash, cd.getBootstrapMethod().toString());
			for (int i = 0; i < cd.getBootstrapMethodArgumentCount(); i++) {
				Object arg = cd.getBootstrapMethodArgument(i);
				hash = fnvUpdate(hash, String.valueOf(arg));
			}
		} else {
			hash = fnvUpdate(hash, String.valueOf(cst));
		}
		return hash;
	}

	public static int hash(ClassNode classNode, String methodDesc) {
		for (var method : classNode.methods) {
			var fullName = method.name + method.desc;
			if (fullName.equals(methodDesc)) {
				return hash(method);
			}
		}
		throw new IllegalArgumentException("Method with descriptor " + methodDesc + " not found!");
	}
}
