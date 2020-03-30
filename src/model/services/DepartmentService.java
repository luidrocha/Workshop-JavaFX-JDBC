package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	public List<Department> findAll() {
		
		List<Department> lista = new ArrayList<>();
		
		lista.add(new Department(1, "Book"));
		lista.add(new Department(2, "Informatica"));
		lista.add(new Department(3, "Requisito"));
		lista.add(new Department(4, "Indicadores"));
		
		return lista;
		
	}

}
