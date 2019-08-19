package com.erivan.produto.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.erivan.produto.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{

	Optional<Usuario> findByNome(String nome);
	
	
}
