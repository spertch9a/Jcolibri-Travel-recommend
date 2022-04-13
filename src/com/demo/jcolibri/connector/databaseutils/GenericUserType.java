/**
 * GenericUserType.java
 * jCOLIBRI2 framework.
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 11/01/2007
 */
package com.demo.jcolibri.connector.databaseutils;

import jcolibri.connector.TypeAdaptor;
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
 * Class that allows to use any jcolibri.connector.TypeAdaptor class in the DataBaseConnector.<p>
 * This is a Hibernate subclass that wraps any user defined data type. This way it can be persisted into the data base easly.<p>
 * @author Juan A. Recio-Garc�a
 * @version 2.0
 * @see TypeAdaptor
 */
public class GenericUserType implements UserType, ParameterizedType {

	private Class clazz = null;

	public void setParameterValues(Properties params) {
		String className = params.getProperty("className");
	    if (className == null) {
	    	throw new MappingException("className parameter not specified");
	    }
	    try {
	        this.clazz = Class.forName(className);
	    }catch(Exception e)
	    {
	    	throw new MappingException("Class " + className + " not found", e);
	    }

	    for(Class c: clazz.getInterfaces())
	    	if (c.equals(TypeAdaptor.class))
	    		return;
	    throw new MappingException("Class "+ className + " does not implements jcolibri.connector.TypeAdaptor. It is a requirement for using the genericUserType");
    }


	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		try {
			String s = (String)arg0;
			TypeAdaptor ta = (TypeAdaptor)arg1;
			ta.fromString(s);
			return ta;
		} catch (Exception e) {
			throw new HibernateException(e);
		}
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		TypeAdaptor ta = (TypeAdaptor)arg0;
		return ta.toString();
	}


	public Object deepCopy(Object arg0) throws HibernateException {
		try {
			TypeAdaptor ta = (TypeAdaptor) arg0;
			String s = ta.toString();

			TypeAdaptor copy = (TypeAdaptor) arg0.getClass().newInstance();
			copy.fromString(s);
			return copy;
		} catch (Exception e) {
			throw new HibernateException(e);
		}

	}


	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		return arg0.equals(arg1);
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	public int hashCode(Object arg0) throws HibernateException {
		return arg0.hashCode();
	}


	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
		return null;
	}


	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {

	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2)
			throws HibernateException, SQLException {
		 try {
//			String val = (String)Hibernate.STRING.nullSafeGet(arg0, arg1[0]);
			 TypeAdaptor ta = (TypeAdaptor)clazz.newInstance();
			 ta.fromString("val");
			 return ta;

		} catch (Exception e) {
			throw new HibernateException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#nullSafeSet(java.sql.PreparedStatement, java.lang.Object, int)
	 */
	public void nullSafeSet(PreparedStatement arg0, Object arg1, int i)
			throws HibernateException, SQLException {
		TypeAdaptor ta = (TypeAdaptor)arg1;
		String val = ta.toString();
        arg0.setString(i, val);

	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#replace(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#returnedClass()
	 */
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return this.clazz;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.usertype.UserType#sqlTypes()
	 */
	public int[] sqlTypes() {
		return new int[] {Types.VARCHAR};
	}


}
