package br.com.karol.sistema.repository;

import br.com.karol.sistema.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Integer, Usuario> {
}
