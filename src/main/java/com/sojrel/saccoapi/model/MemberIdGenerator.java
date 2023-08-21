package com.sojrel.saccoapi.model;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.jdbc.ReturningWork;

public class MemberIdGenerator implements IdentifierGenerator{
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		String prefix = "SJL";
		int suffix;
		String formatted_suffix = "";
		try {
			Connection connection = session.doReturningWork(new ReturningWork<Connection>() {
	            @Override
	            public Connection execute(Connection conn) throws SQLException {
	                return conn;
	            }
	        });
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select count(id) from member");
			if(resultSet.next()) {
			    Integer id = resultSet.getInt(1) + 1;
			    suffix = id;
			    formatted_suffix = String.format("%03d", suffix);
			   }
		}catch(Exception e) {
			e.printStackTrace();
		}
		return prefix + formatted_suffix;
	}
	
}



