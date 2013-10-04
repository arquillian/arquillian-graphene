/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.javascript;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.jboss.arquillian.graphene.spi.ImplementedBy;
import org.openqa.selenium.JavascriptExecutor;

/**
 * <p>
 * Interfaces or abstract classes annotated with this interface invokes a script on a page under test.
 * </p>
 *
 * <p>
 * This annotation is used for both, annotating the type and marking the injection point as demonstrated in following example.
 * </p>
 *
 * <p>
 * The interface can also automatically retrieve JavaScript file dependencies, using {@link Dependency} annotation.
 * </p>
 *
 * <p>
 * Default implementation of {@link ExecutionResolver} which invokes the script on the page automatically converts all types
 * defined by {@link JavascriptExecutor#executeScript(String, Object...)} and additionally it converts enumerations to their
 * string representation.
 * </p>
 *
 * <p>
 * Default implementation also automatically resolves the getters and setters name as accessors to the object. Java method
 * called <tt>setName</tt> can either call <tt>setName</tt> method on target JavaScript object or it can set a value of
 * <tt>name</tt> property of that object. Similarly <tt>getName</tt> can either call a <tt>getName</tt> method or return
 * <tt>name</tt> property.
 * </p>
 *
 * <p>
 * The name of a method is automatically diverged from name of a Java interface method, however it can be re-defined using
 * {@link MethodName} annotation.
 *
 * <h2>Example</h2>
 *
 * <h4>helloworld.js</h4>
 *
 * <pre>
 * window.helloworld = {
 *   hello: function() {
 *     return "Hello World!";
 *   }
 * }
 * </pre>
 *
 * <h4>HelloWorld.java</h4>
 *
 * <pre>
 * &#064;JavaScript(&quot;helloworld&quot;)
 * &#064;Dependency(sources = &quot;helloworld.js&quot;)
 * public interface HelloWorld {
 *
 *     String hello();
 *
 * }
 * </pre>
 *
 * <h4>TestCase.java</h4>
 *
 * <pre>
 * public static class TestCase {
 *
 *     &#064;JavaScript
 *     private HelloWorld helloWorld;
 *
 *     &#064;Test
 *     public void testHelloWorld() {
 *         assertEquals(&quot;Hello World!&quot;, helloWorld.hello());
 *     }
 * }
 * </pre>
 *
 * @author Lukas Fryc
 */
@Target({ TYPE, FIELD })
@Retention(RUNTIME)
@Documented
public @interface JavaScript {

    /**
     * <p>
     * The name of JavaScript interface - Graphene will look up the global object of that name on a page.
     * </p>
     *
     * <p>
     * Defaults to <tt>window.&lt;type&gt;</tt> where type is name of the interface annotated with this annotation.
     * </p>
     *
     * @return name of the interface
     */
    String value() default "";

    /**
     * Indicates that this interface is just an interface and its implementation (another JavaScript interface) is specified in
     * linked implementation class.
     *
     * @return the class of implementation
     */
    String implementation() default "";

    /**
     * Returns {@link ExecutionResolver} which will execute the JavaScript interface.
     *
     * @return the class of a {@link ExecutionResolver} which will invokes this interface
     */
    Class<? extends ExecutionResolver> executionResolver() default DefaultExecutionResolver.class;

    @ImplementedBy(className = "org.jboss.arquillian.graphene.javascript.DefaultExecutionResolver")
    interface DefaultExecutionResolver extends ExecutionResolver {
    }
}
