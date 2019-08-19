package com.erivan.produto.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erivan.produto.model.Usuario;
import com.erivan.produto.model.UsuarioLogin;
import com.erivan.produto.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	
	private final UsuarioRepository usuarioRepository;
	
	public UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	//cadastrar usuario
	@GetMapping("/cadastrarUsuario")
	public String cadastrar(Model model, Usuario usuario) {
		return "usuario/cadastrarUsuario";
	}
	    //cadastrar usuarios
	    @PostMapping("/cadastrarUsuario")
	    public String cadastrar(Model model, Usuario usuario, UsuarioLogin usuarioLogin, BindingResult result,  RedirectAttributes attributes) {
			//pega o nome do usuario e senha criptografa ela e vai para usuario para ser gravada no banco
			if(usuarioLogin.getNome().matches("^[a-zA-Z0-9]{3,}$")) {
				usuario = new Usuario(usuario.getNome_completo(), usuario.getTelefone(), usuarioLogin.getNome().trim(), new BCryptPasswordEncoder().encode(usuarioLogin.getSenha()));
			}
			else {
				result.rejectValue("nome", "usuarionome");//rever
				return "cadastrarUsuario";
				
			}
			
			try {
				//salvar em usuario
				this.usuarioRepository.save(usuario);
			}catch (DataIntegrityViolationException ex) {
				ex.printStackTrace();
				result.rejectValue("nome", "nome");
				return "cadastrarUsuario";
			}
			//salvou retorna para o formulario de cadastro atraves do GetMapping
			attributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso");
			return "redirect:/cadastrarUsuario";
			
		}
	
	
	
		
		
		//para o listarUsuario.html
		@RequestMapping("/usuarios")
		public ModelAndView listarUsuariosPage() {
			
		ModelAndView mv = new ModelAndView("usuario/listarUsuarios");	
		Iterable<Usuario> usuarios = usuarioRepository.findAll();
		mv.addObject("usuariosListar", usuarios);
			return mv;
		}
	
	//deleta o usuario
	@GetMapping("/delete/{id}")
	public String deletarUsuario(@PathVariable("id") long id, Model model) {
		//busca usuario
		Usuario usuario = usuarioRepository.findById(id)
				 .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		usuarioRepository.delete(usuario);
		model.addAttribute("usuariosListar", usuarioRepository.findAll());
		return "/usuario/listarUsuarios";
	}
	
	//chama a pagina de login
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
