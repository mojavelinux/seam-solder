/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.seam.solder.reflection.annotated;

import static org.jboss.seam.solder.bean.Beans.createInjectionPoints;
import static org.jboss.seam.solder.reflection.Reflections.EMPTY_OBJECT_ARRAY;
import static org.jboss.seam.solder.reflection.Reflections.invokeMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.seam.solder.reflection.annotated.ParameterValueRedefiner.ParameterValue;

/**
 * <p>
 * Allows an {@link AnnotatedMethod} to be injected using the CDI type safe
 * resolution rules.
 * </p>
 * 
 * <p>
 * {@link ParameterValueRedefiner} allows the default value to be overridden by
 * the caller of
 * {@link #invoke(Object, CreationalContext, ParameterValueRedefiner)}.
 * </p>
 * 
 * @author Pete Muir
 * 
 * @param <X> the declaring type
 */
public class InjectableMethod<X>
{

   private final AnnotatedMethod<X> method;
   private final List<InjectionPoint> parameters;
   private final BeanManager beanManager;

   /**
    * Instantiate a new {@link InjectableMethod}.
    * 
    * @param method the method which will be injected upon a call to
    *           {@link #invoke(Object, CreationalContext)}
    * @param bean the bean which defines the injectable method
    * @param beanManager the {@link BeanManager} to use to obtain the parameter
    *           values
    */
   public InjectableMethod(AnnotatedMethod<X> method, Bean<?> declaringBean, BeanManager beanManager)
   {
      this(method, createInjectionPoints(method, declaringBean, beanManager), beanManager);
   }

   /**
    * Instantiate a new {@link InjectableMethod}.
    * 
    * @param method the method which will be injected upon a call to
    *           {@link #invoke(Object, CreationalContext)}
    * @param parameters a collection of injection points representing the
    *           parameters of the method
    * @param beanManager the {@link BeanManager} to use to obtain the parameter
    *           values
    */
   public InjectableMethod(AnnotatedMethod<X> method, Collection<InjectionPoint> parameters, BeanManager beanManager)
   {
      this.method = method;
      this.parameters = new ArrayList<InjectionPoint>(parameters);
      this.beanManager = beanManager;
   }

    /**
    * Get the bean manager used by this injectable method.
    * 
    * @return the bean manager in use
    */
   protected BeanManager getBeanManager()
   {
      return beanManager;
   }

   /**
    * Get the injectable parameters of this method.
    * 
    * @return a collection of injection points representing the parameters of
    *         this method
    */
   protected List<InjectionPoint> getParameters()
   {
      return parameters;
   }

   /**
    * Invoke the method, causing all parameters to be injected according to the
    * CDI type safe resolution rules.
    * 
    * @param <T> the return type of the method
    * @param receiver the instance upon which to call the method
    * @param creationalContext the creational context to use to obtain
    *           injectable references for each parameter
    * @return the result of invoking the method or null if the method's return
    *         type is void
    * @throws RuntimeException if this <code>Method</code> object enforces Java
    *            language access control and the underlying method is
    *            inaccessible or if the underlying method throws an exception or
    *            if the initialization provoked by this method fails.
    * @throws IllegalArgumentException if the method is an instance method and
    *            the specified <code>receiver</code> argument is not an instance
    *            of the class or interface declaring the underlying method (or
    *            of a subclass or implementor thereof); if an unwrapping
    *            conversion for primitive arguments fails; or if, after possible
    *            unwrapping, a parameter value cannot be converted to the
    *            corresponding formal parameter type by a method invocation
    *            conversion.
    * @throws NullPointerException if the specified <code>receiver</code> is
    *            null and the method is an instance method.
    * @throws ExceptionInInitializerError if the initialization provoked by this
    *            method fails.
    */
   public <T> T invoke(Object receiver, CreationalContext<T> creationalContext)
   {
      return invoke(receiver, creationalContext, null);
   }

   /**
    * Invoke the method, calling the parameter redefiner for each parameter,
    * allowing the caller to override the default value obtained via the CDI
    * type safe resolver.
    * 
    * @param <T> the return type of the method
    * @param receiver the instance upon which to call the method
    * @param creationalContext the creational context to use to obtain
    *           injectable references for each parameter
    * @return the result of invoking the method or null if the method's return
    *         type is void
    * @throws RuntimeException if this <code>Method</code> object enforces Java
    *            language access control and the underlying method is
    *            inaccessible or if the underlying method throws an exception or
    *            if the initialization provoked by this method fails.
    * @throws IllegalArgumentException if the method is an instance method and
    *            the specified <code>receiver</code> argument is not an instance
    *            of the class or interface declaring the underlying method (or
    *            of a subclass or implementor thereof); if an unwrapping
    *            conversion for primitive arguments fails; or if, after possible
    *            unwrapping, a parameter value cannot be converted to the
    *            corresponding formal parameter type by a method invocation
    *            conversion.
    * @throws NullPointerException if the specified <code>receiver</code> is
    *            null and the method is an instance method.
    * @throws ExceptionInInitializerError if the initialization provoked by this
    *            method fails.
    */
   public <T> T invoke(Object receiver, CreationalContext<T> creationalContext, ParameterValueRedefiner redefinition)
   {
      List<Object> parameterValues = new ArrayList<Object>();
      for (int i = 0; i < getParameters().size(); i++)
      {
         if (redefinition != null)
         {
            ParameterValue value = new ParameterValue(i, getParameters().get(i), getBeanManager());
            parameterValues.add(redefinition.redefineParameterValue(value));
         }
         else
         {
            parameterValues.add(getBeanManager().getInjectableReference(getParameters().get(i), creationalContext));
         }
      }

      @SuppressWarnings("unchecked")
      T result = (T) invokeMethod(true, method.getJavaMember(), receiver, parameterValues.toArray(EMPTY_OBJECT_ARRAY));

      return result;
   }

}
