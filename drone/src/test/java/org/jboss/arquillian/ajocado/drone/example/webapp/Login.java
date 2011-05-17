/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.ajocado.drone.example.webapp;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@SessionScoped
@Named
public class Login implements Serializable
{

   private static final long serialVersionUID = 7965455427888195913L;

   @Inject
   private Credentials credentials;
   
   @PersistenceContext
   private EntityManager userDatabase;

   private User currentUser;

   @SuppressWarnings("unchecked")
   public void login()
   {

      List<User> results = userDatabase.createQuery("select u from User u where u.username=:username and u.password=:password").setParameter("username", credentials.getUsername()).setParameter("password", credentials.getPassword()).getResultList();

      if (!results.isEmpty())
      {
         currentUser = results.get(0);
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome, " + currentUser.getName()));
      }

   }

   public void logout()
   {
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Goodbye, " + currentUser.getName()));
      currentUser = null;
   }

   public boolean isLoggedIn()
   {
      return currentUser != null;
   }

   @Produces
   @LoggedIn
   public User getCurrentUser()
   {
      return currentUser;
   }

}