package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

// Obter e fechar conexao com bd. e contem os metodos auxiliares
public class DB {

	// cria um obj do tipo Connection

	public static Connection conn = null;

	public static Connection getConnection() throws SQLException {

		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props); // faz a conexao e guarda em Conn . Connectar e
																// instanciar um obj do tipo Connection
			} catch (SQLException e) { // Essa excecao e obrigado a tratar, deverivada da Exception
				throw new DbException(e.getMessage()); // Essa excecao personalizada 'e derivada da RuntimeException que
														// nao obriga a tratar.

			}
		}
		return conn;

	}

	public static void closeStatemant(Statement st) {

		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {

				throw new DbException(e.getMessage());
			}
		}

	}

	public static void closeResultSet(ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {

				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeConnection() throws SQLException {
		try {
			if (conn != null) {

				conn.close();
			}

		} catch (DbException e) {

			throw new DbException(e.getMessage());
		}

	}

	public static Properties loadProperties() {

		try (FileInputStream fs = new FileInputStream("db.properties")) {

			// Declara um obj do tipo Properties;

			Properties props = new Properties();

			// faz a leitura do arquico db.properties

			props.load(fs);

			return props;

		} catch (IOException e) { // trata as duas excecao fileioexception e filenotfoundException

			throw new DbException(e.getMessage());
		}
	}
}
