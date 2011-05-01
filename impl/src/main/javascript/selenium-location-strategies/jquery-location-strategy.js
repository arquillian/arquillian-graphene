/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
var loc = locator;
var attr = null;
var isattr = false; 
var inx = locator.lastIndexOf('@');

if(inx != -1){
	loc = locator.substring(0, inx);
	attr = locator.substring(inx + 1);
	isattr = true;
}

var found = $(inDocument).find(loc);

if (found.length >= 1) {
	if (isattr){
		return found[0].getAttributeNode(attr);
	} else {
		return found[0];
	}
} else {
	return null;
}