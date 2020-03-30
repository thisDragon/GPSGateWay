/**
 * 
 */
package com.cari.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

/**
 * @author zsy
 *
 */
public class IncrementIDGenerator implements IdentifierGenerator, Configurable {

	private String tableName;
	private String columnName;
	private String index;
	
	public Serializable generate(SessionImplementor session, Object o) throws HibernateException {
		String next = null;
		try {
			String sql = "select max(" + columnName + ") from " + tableName;
			Connection con = null;
			PreparedStatement st = null;
			ResultSet rs = null;
			try {
				con = session.connection();
				st = con.prepareStatement(sql);
				rs = st.executeQuery();

				if ( rs.next() ) {
					next = rs.getString(1);
					if ( rs.wasNull() ) {
						next = index;
					} else {
						next = Long.toString(Long.parseLong(next) + 1);
					}
					
				} else {
					next = index;
				}
			} finally {
				//session.getBatcher().closeStatement(st);
				if (con != null) {
					con.close();
				}
				if (st != null) {
					st.close();
				}
				if (rs != null) {
					rs.close();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return next;
	}

	public void configure(Type types, Properties params, Dialect dialect) throws MappingException {
		tableName = params.getProperty("table");
		columnName = params.getProperty("column");
		index = params.getProperty("index");
		if (index == null || index.trim().equals("")) {
			index = "1";
		}
	}

}
