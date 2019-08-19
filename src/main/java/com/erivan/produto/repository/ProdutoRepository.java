package com.erivan.produto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.erivan.produto.model.Produto;

public interface ProdutoRepository  extends CrudRepository<Produto, Long>{
	
	Produto findByCodigo(long codigo);
	//namedQuery
	List<Produto> ordenaPelaOrdemCrescente();
	
	//lista para a busca pelo nome do produto
	//como é somente 1 parametro coloca ?1 se fose dois parametros @Query("select p from Produto p where p.nome like %?1% and %?2%")
	//jpql
	
	@Query("select p from Produto p where p.nome like %?1%")
	List<Produto> findProdutoByName(String nome);

}
