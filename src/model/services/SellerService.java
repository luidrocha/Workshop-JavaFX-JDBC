package model.services;

import java.util.List;

import demo.dao.DaoFactory;
import demo.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll() {

		return dao.findAll();

	}

	// recebe o obj, se o id estiver vazio faz uma insercao se tiver preenchido faz
	// uma atualizacao
	public void saveorUpdate(Seller obj) {

		if (obj.getId() == null) {

			dao.insert(obj);
		}

		else {

			dao.update(obj);

		}

	}
	
	// Elimina o Sellero de acordo com o Id 
	public void remove(Seller obj) {
		
		dao.deleteById(obj.getId());
	}

}
