package dev.uncandango.alltheleaks.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Issue {
	String modId();

	String issueId() default "[untracked]";

	String versionRange();

	String[] mixins() default {};

	boolean solved() default false;

	String[] extraModDep() default {};

	String[] extraModDepVersions() default {};
}
