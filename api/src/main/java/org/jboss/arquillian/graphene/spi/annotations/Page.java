package org.jboss.arquillian.graphene.spi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Deprecated:</b> use {@link org.jboss.arquillian.graphene.page.Page} annotation instead.</p>
 *
 * This annotation marks the given object to be instantiated and injected as Page object
 *
 * <pre>
 * {@code
 * public class LoginForm {
 *
 *     &#064;FindBy(css = ".login")
 *     WebElement login;
 *
 *     &#064;FindBy(css = ".password")
 *     WebElement password;
 *
 *     &#064;FindBy(css = ".confirmation")
 *     WebElement confirmation;
 *
 *     public void login(String login, String password) {
 *         ...
 *     }
 * }
 *
 * &#064;RunWith(Arquillian.class)
 * public class Test {
 *
 *     &#064;Page
 *     LoginForm loginForm;
 *
 *     &#064;Test
 *     public void test() {
 *         loginForm.login("login", "password");
 *     }
 * }
 * </pre>
 *
 * @author Juraj Huska
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface Page {

}