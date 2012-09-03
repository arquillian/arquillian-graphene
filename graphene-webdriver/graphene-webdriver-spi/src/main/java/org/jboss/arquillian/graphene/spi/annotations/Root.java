package org.jboss.arquillian.graphene.spi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the <tt>WebElement</tt> field where should be injected the root of the given fragment,
 * given by <tt>&#064;FindBy</tt> annotation on a injection point. 
 * 
 * <pre>
 * {@code
 * public class AutocompletionInput {
 *     
 *     &#064;Root
 *     WebElement input;
 *     
 *     public void typeText(String text) {
 *         input.typeText(text);
 *     }
 *     
 *     
 *     public void select(String suggestion) {
 *         ...
 *     }
 * }
 * 
 * &#064;RunWith(Arquillian.class)
 * public class Test {
 *     
 *     &#064;FindBy(css = ".autocompletion")
 *     AutocompletionInput autocomplete;
 *     
 *     &#064;Test
 *     public void test() {
 *         autocomplete.typeText("Arq");
 *         autocomplete.select("Arquillian");
 *     }
 * }
 * </pre>
 * 
 * @author Juraj Huska
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Root {
}
