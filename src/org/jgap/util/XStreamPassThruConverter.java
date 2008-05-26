/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import org.jgap.gp.impl.*;

import junitx.util.*;
import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.annotations.*;
import com.thoughtworks.xstream.converters.*;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.reflection.*;
import com.thoughtworks.xstream.io.*;
import com.thoughtworks.xstream.mapper.*;

/**
 * An abstract XSTream converter that offers services for default marshalling
 * and unmarshalling of objects.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public abstract class XStreamPassThruConverter
    implements Converter {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Mapper m_mapper;

  private ReflectionProvider m_reflectionProvider;

  private AnnotationProvider m_annotationProvider;

  private Class m_clazz;

  private GPConfiguration m_conf;

  public XStreamPassThruConverter(Mapper mapper,
                                  ReflectionProvider reflectionProvider,
                                  Class a_clazz) {
    m_mapper = mapper;
    m_reflectionProvider = reflectionProvider;
    m_annotationProvider = new AnnotationProvider();
    m_clazz = a_clazz;
  }

  public XStreamPassThruConverter(XStream a_xstream, Class a_clazz) {
    this(a_xstream.getMapper(), a_xstream.getReflectionProvider(), a_clazz);
  }

  public boolean canConvert(Class type) {
    return m_clazz.equals(type);
  }

  public void marshal(Object source, HierarchicalStreamWriter writer,
                      MarshallingContext context) {
    marshalDefault(source, writer, context);
  }

  protected void marshalDefault(Object source, HierarchicalStreamWriter writer,
                                MarshallingContext context) {
    new AnnotationReflectionConverter(m_mapper, m_reflectionProvider,
                                      m_annotationProvider).marshal(source,
        writer, context);
  }

  public Object unmarshal(HierarchicalStreamReader reader,
                          UnmarshallingContext context) {
    try {
      return doUnmarshal(context);
    } catch (Exception ex) {
      throw new RuntimeException("unmarshalling failed", ex);
    }
  }

  public abstract Object doUnmarshal(UnmarshallingContext context)
      throws Exception;

  protected Object unmarshalDefault(UnmarshallingContext context, Object a_obj) {
    return context.convertAnother(a_obj, a_obj.getClass(),
                                  new AnnotationReflectionConverter(m_mapper,
        m_reflectionProvider, m_annotationProvider));
  }

  public void setConfiguration(GPConfiguration a_conf) {
    m_conf = a_conf;
  }

  public GPConfiguration getConfiguration() {
    return m_conf;
  }

  protected void setConfiguration(Object a_obj, String a_fieldName)
      throws Exception {
    Object conf = PrivateAccessor.getField(a_obj, a_fieldName);
    if (conf == null) {
      PrivateAccessor.setField(a_obj, a_fieldName, getConfiguration());
    }
  }
}
