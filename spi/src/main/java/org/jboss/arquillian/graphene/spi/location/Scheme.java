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
package org.jboss.arquillian.graphene.spi.location;

/**
 * URI scheme for {@link Location} annotation to specify the type of location to navigate to.
 *
 * Extend this class and implement your own scheme type, in case you want to add your own scheme. Implement location decider
 * which will decide that particular scheme as well.
 *
 * @see LocationDecider
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class Scheme {

    public static final class HTTP extends Scheme {
        @Override
        public String toString() {
            return "http://";
        }
    }

    public static final class FILE extends Scheme {
        @Override
        public String toString() {
            return "file://";
        }
    }

    public static final class RESOURCE extends Scheme {
        @Override
        public String toString() {
            return "resource://";
        }
    }

}
