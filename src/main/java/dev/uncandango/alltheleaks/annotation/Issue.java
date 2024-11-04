package dev.uncandango.alltheleaks.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("UnusedReturnValue")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Issue {
	String modId();

	String issueId() default "[untracked]";

	String versionRange();

	String[] mixins() default {};

	String[] extraModDep() default {};

	String[] extraModDepVersions() default {};

	String config() default "";

	boolean devOnly() default false;

	String[] mixinsToCancel() default {};

	boolean configActivated() default false;

	String onlyIfModAbsent() default "";

	String description() default "";
}