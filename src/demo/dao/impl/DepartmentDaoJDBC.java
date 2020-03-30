package demo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import demo.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {

		this.conn = conn;

	}

	@Override
	public void insert(Department obj) {

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("INSERT INTO Department (Name)  VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {

				ResultSet rs = st.getGeneratedKeys();

				if (rs.next()) {

					int Id = rs.getInt(1);
					obj.setId(Id);
					DB.closeResultSet(rs);

				}
			}

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {

			DB.closeStatemant(st);
		}

	}

	@Override
	public void update(Department obj) {
		//

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("UPDATE Department SET Name=?  WHERE id = ?");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {

				System.out.println(" Registros afetados: " + linhasAfetadas);
				System.out.println("Departamento Atualizado: " + obj.getId());

			} else {
				System.out.println("Nenhuma linha atualizada !");

				System.out.println("Departamento Inexistente: " + obj.getId());
			}
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatemant(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		//

		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("DELETE FROM Department WHERE id = ?");

			st.setInt(1, id);

			int linhasAfetadas = st.executeUpdate();

			if (linhasAfetadas > 0) {

				System.out.println(" Registros afetados: " + linhasAfetadas);
				System.out.println("Departamento Eliminado: " + id);

			} else {
				System.out.println("Nenhuma linha atualizada !");

				System.out.println("Departamento Inexistente: " + id);
			}
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatemant(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		//

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT department.* FROM Department WHERE id=?");

			st.setInt(1, id);

			rs = st.executeQuery();

			Department dep = null;

			if (rs.next()) {

				dep = InstantiateDep(rs);

			}

			return dep;

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		} finally {

			DB.closeStatemant(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			Map<Integer, Department> map = new HashMap<>();
			List<Department> lista = new ArrayList<>();

			st = conn.prepareStatement(" SELECT department.* FROM Department");

			rs = st.executeQuery();

			Department dep = new Department();

			while (rs.next()) {

				dep = map.get(rs.getInt("Id"));

				if (dep == null) {

					dep = InstantiateDep(rs);

					map.put(rs.getInt("id"), dep);

				}

				dep = InstantiateDep(rs);

				lista.add(dep);

			}

			return lista;

		} catch (SQLException e) {

			throw new DbException(e.getMessage());
		}

		finally {

			DB.closeStatemant(st);
			DB.closeResultSet(rs);
		}

	}

	// Metodo auxiliar para instanciar Departamento

	private Department InstantiateDep(ResultSet rs) throws SQLException {

		Department dep = new Department();

		dep.setId(rs.getInt(1));
		dep.setName(rs.getString(2));

		return dep;
	}

}
