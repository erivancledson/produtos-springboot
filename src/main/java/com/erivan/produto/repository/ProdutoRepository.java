package com.erivan.produto.repository;

import org.springframework.data.repository.CrudRepository;

import com.erivan.produto.model.Produto;

public interface ProdutoRepository  extends CrudRepository<Produto, Long>{
	
	Produto findByCodigo(long codigo);

}
