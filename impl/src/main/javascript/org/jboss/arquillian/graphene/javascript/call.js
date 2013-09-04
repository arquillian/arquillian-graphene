/*
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
function invokeInterface (target, method, args) {
    var args = args || [];
    try {
        if (target) {
            /* property getter */
            if (!args.length && method.indexOf("get") == 0) {
                var property = method.substring(3, method.length);
                /* uncapitalize */
                property = property.charAt(0).toLowerCase() + property.slice(1);
                if (property && target[property]) {
                    return target[property];
                }
            }
            
            /* property setter */
            if (args.length === 1 && method.indexOf("set") == 0) {
                var property = method.substring(3, method.length);
                /* uncapitalize */
                property = property.charAt(0).toLowerCase() + property.slice(1);
                if (property && target[property] != undefined) {
                    target[property] = args[0];
                    return;
                }
            }

            /* method */
            if (typeof(target[method]) == "function") {
                return target[method].apply(target, args);
            }
        } else {
            throw "target object of invocation is not defined"
        }
    } catch (e) {
        console.log('exception thrown when executing method ' + method + ': ' + e);
        /*
         * here an exception should be thrown, but it can't be because of
         * 'ARQGRA-289: JavaScript interfaces fails on Chrome and PhantomJS'
         */
        return 'GRAPHENE ERROR: ' + e;
    }
};