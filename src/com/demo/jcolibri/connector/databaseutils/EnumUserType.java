/**
 * DataBaseConnector.java
 * jCOLIBRI2 framework.
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 02/01/2007
 */
package com.demo.jcolibri.connector.databaseutils;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Class that allows to use Enums in the DataBaseConnector.<p>
 * This is a Hibernate subclass that wraps java 1.5 Enums.<p>
 * @author Juan A. Recio-Garc�a
 * @version 2.0
 */
public class EnumUserType implements UserType, ParameterizedType {

   private Class clazz = null;

   public void setParameterValues(Properties params) {
      String enumClassName = params.getProperty("enumClassName");
      if (enumClassName == null) {
         throw new MappingException("enumClassName parameter not specified");
      }

      try {
            this.clazz = Class.forName(enumClassName);
            return;
      }catch(Exception e){}
      try {
    	    String superclassName = enumClassName.substring(0, enumClassName.lastIndexOf("."));
    	    String subclassName = enumClassName.substring(enumClassName.lastIndexOf(".")+1, enumClassName.length());
    	    Class superclass = Class.forName(superclassName);
    	    Class[] decClasses = superclass.getDeclaredClasses();
    	    for(Class c : decClasses)
    	    	if(c.getName().equals(subclassName))
    	    		this.clazz = c;
      } catch (ClassNotFoundException e) {
         throw new MappingException("enumClass " + enumClassName + " not found", e);
        }
   }

    private static final int[] SQL_TYPES = {Types.VARCHAR};
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Class returnedClass() {
        return clazz;
    }

    @SuppressWarnings("unchecked")
	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
                             throws HibernateException, SQLException {
        String name = resultSet.getString(names[0]);
        Object result = null;
        if (!resultSet.wasNull()) {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }

   public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index)
                          throws HibernateException, SQLException {
        if (null == value) {
            preparedStatement.setNull(index, Types.VARCHAR);
        } else {
            preparedStatement.setString(index, ( (Enum) value ).name() );
        }
    }

    public Object deepCopy(Object value) throws HibernateException{
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable)value;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }


    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        return null;
    }


    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {

    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (null == x || null == y)
            return false;
        return x.equals(y);
    }
}