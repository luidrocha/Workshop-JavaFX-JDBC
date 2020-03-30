package model.services;

import java.util.List;

import demo.dao.DaoFactory;
import demo.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll() {

		return dao.findAll();

	}

}
