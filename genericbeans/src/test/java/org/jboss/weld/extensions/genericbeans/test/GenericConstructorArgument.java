package org.jboss.weld.extensions.genericbeans.test;

import javax.inject.Inject;

import org.jboss.weld.extensions.genericbeans.Generic;

@Generic(TestAnnotation.class)
public class GenericConstructorArgument
{
   @Inject
   TestAnnotation data;

   public String getValue()
   {
      return data.value();
   }

}
