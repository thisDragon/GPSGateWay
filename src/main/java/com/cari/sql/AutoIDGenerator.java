/**
 * 
 */
package com.cari.sql;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * @author zsy
 *
 */
public class AutoIDGenerator implements IdentifierGenerator {

	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		return IDCreator.getID12();
	}

}
