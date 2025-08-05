package dev.uncandango.alltheleaks.utils;

import dev.uncandango.alltheleaks.AllTheLeaks;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.ConstantDynamic;
import java.util.*;
import java.util.function.Function;

public class MethodNodeDebug {

	public static void printNormalizedInstructions(MethodNode method) {
		Map<LabelNode, Integer> labelIds = new IdentityHashMap<>();

		AllTheLeaks.LOGGER.info("Method: {}{}", method.name, method.desc);
		AllTheLeaks.LOGGER.info("Access: {}", method.access);
		AllTheLeaks.LOGGER.info("Instructions:");

		int index = 0;

		// Helper to get or assign label IDs without lambda
		Function<LabelNode, Integer> getLabelId = l -> {
			Integer id = labelIds.get(l);
			if (id == null) {
				int newId = labelIds.size();
				labelIds.put(l, newId);
				return newId;
			}
			return id;
		};

		for (AbstractInsnNode insn : method.instructions) {
			// skip frames and line numbers
			if (insn instanceof FrameNode || insn instanceof LineNumberNode) {
				continue;
			}

			if (insn instanceof LabelNode l) {
				int id = getLabelId.apply(l);
				System.out.printf("  %03d: LABEL_%d%n", index++, id);
				continue;
			}

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("  %03d: %-15s", index++, insn.getClass().getSimpleName()));

			// opcode
			sb.append(" opcode=").append(insn.getOpcode());

			// operand details
			switch (insn.getType()) {
				case AbstractInsnNode.VAR_INSN -> {
					sb.append(" var=").append(((VarInsnNode) insn).var);
				}
				case AbstractInsnNode.FIELD_INSN -> {
					FieldInsnNode f = (FieldInsnNode) insn;
					sb.append(" ").append(f.owner).append(".").append(f.name).append(":").append(f.desc);
				}
				case AbstractInsnNode.METHOD_INSN -> {
					MethodInsnNode m = (MethodInsnNode) insn;
					sb.append(" ").append(m.owner).append(".").append(m.name).append(m.desc);
					sb.append(" itf=").append(m.itf);
				}
				case AbstractInsnNode.LDC_INSN -> {
					Object cst = ((LdcInsnNode) insn).cst;
					if (cst instanceof ConstantDynamic cd) {
						sb.append(" ldc(ConstantDynamic) name=").append(cd.getName())
							.append(" desc=").append(cd.getDescriptor())
							.append(" bsm=").append(cd.getBootstrapMethod());
					} else {
						sb.append(" ldc=").append(cst);
					}
				}
				case AbstractInsnNode.INT_INSN -> {
					sb.append(" operand=").append(((IntInsnNode) insn).operand);
				}
				case AbstractInsnNode.TYPE_INSN -> {
					sb.append(" desc=").append(((TypeInsnNode) insn).desc);
				}
				case AbstractInsnNode.JUMP_INSN -> {
					JumpInsnNode j = (JumpInsnNode) insn;
					int id = getLabelId.apply(j.label);
					sb.append(" -> LABEL_").append(id);
				}
				case AbstractInsnNode.IINC_INSN -> {
					IincInsnNode iinc = (IincInsnNode) insn;
					sb.append(" var=").append(iinc.var).append(" incr=").append(iinc.incr);
				}
				case AbstractInsnNode.INVOKE_DYNAMIC_INSN -> {
					InvokeDynamicInsnNode indy = (InvokeDynamicInsnNode) insn;
					sb.append(" indy name=").append(indy.name).append(" desc=").append(indy.desc);
					sb.append(" bsm=").append(indy.bsm);
					sb.append(" args=").append(Arrays.toString(indy.bsmArgs));
				}
				case AbstractInsnNode.MULTIANEWARRAY_INSN -> {
					MultiANewArrayInsnNode m = (MultiANewArrayInsnNode) insn;
					sb.append(" desc=").append(m.desc).append(" dims=").append(m.dims);
				}
				case AbstractInsnNode.LOOKUPSWITCH_INSN -> {
					LookupSwitchInsnNode lsi = (LookupSwitchInsnNode) insn;
					int dfltId = getLabelId.apply(lsi.dflt);
					sb.append(" default=LABEL_").append(dfltId);
					sb.append(" keys/labels=[");
					for (int i = 0; i < lsi.keys.size(); i++) {
						int lid = getLabelId.apply(lsi.labels.get(i));
						sb.append(lsi.keys.get(i)).append("->LABEL_").append(lid).append(" ");
					}
					sb.append("]");
				}
				case AbstractInsnNode.TABLESWITCH_INSN -> {
					TableSwitchInsnNode tsi = (TableSwitchInsnNode) insn;
					sb.append(" min=").append(tsi.min).append(" max=").append(tsi.max);
					int defId = getLabelId.apply(tsi.dflt);
					sb.append(" default=LABEL_").append(defId).append(" labels=[");
					for (LabelNode l : tsi.labels) {
						int lid = getLabelId.apply(l);
						sb.append("LABEL_").append(lid).append(" ");
					}
					sb.append("]");
				}
			}

			AllTheLeaks.LOGGER.info(sb.toString());
		}
	}
}
