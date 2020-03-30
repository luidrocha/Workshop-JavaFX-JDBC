package demo.dao;

import java.sql.SQLException;

import db.DB;
import db.DbException;
import demo.dao.impl.DepartmentDaoJDBC;
import demo.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {

		try {
			return new SellerDaoJDBC(DB.getConnection());
		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

	}
	
	public static DepartmentDao createDepartmentDao() {
		
		
		try {
			return new DepartmentDaoJDBC(DB.getConnection());
		}
		catch (SQLException e) {
			
			throw new DbException (e.getMessage());
		}
	}

}
