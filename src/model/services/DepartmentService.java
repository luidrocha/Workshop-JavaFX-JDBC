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

	// recebe o obj, se o id estiver vazio faz uma insercao se tiver preenchido faz
	// uma atualizacao
	public void saveorUpdate(Department obj) {

		if (obj.getId() == null) {

			dao.insert(obj);
		}

		else {

			dao.update(obj);

		}

	}

}
