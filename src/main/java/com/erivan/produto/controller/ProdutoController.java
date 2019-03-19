package com.erivan.produto.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.erivan.produto.model.Produto;
import com.erivan.produto.repository.ProdutoRepository;

@Controller
public class ProdutoController {

	@Autowired
	ProdutoRepository produtoRepository;
	
	//chama a pagina de cadastrado
	@RequestMapping(value = "/cadastrarProduto", method = RequestMethod.GET)
	public String cadastroProduto() {
		return "produtos/cadastrarproduto";
	}
	
	//cadastrar
	@RequestMapping(value="/cadastrarProduto", method = RequestMethod.POST)
	public String cadastroProduto(@Valid Produto produto, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/cadastarProduto";
		}
		
		produtoRepository.save(produto);
		attributes.addFlashAttribute("mensagem", "Cadastrado com sucesso!");
		return "redirect:/cadastrarProduto";
	}
	
	//listar
	@RequestMapping("produtos")
	public ModelAndView listaProdutos() {
		//chama o html
		ModelAndView mv = new ModelAndView("produtos/listaProdutos");
		Iterable<Produto> produtos = produtoRepository.findAll();
		mv.addObject("produtos", produtos);
		return mv;
	}
	
	//deletar
	@RequestMapping("/deletarProduto")
	public String deletarProduto(long codigo) {
		Produto produto = produtoRepository.findByCodigo(codigo);
		produtoRepository.delete(produto);
		return "redirect:/produtos";
	}
	
	//editar
	@GetMapping("/edit/{codigo}")
	public String atualizarReceberDados(@PathVariable("codigo") long codigo, Model model) {
		Produto produto = produtoRepository.findByCodigo(codigo);
		//envia uma chave para o html, atraves do campo th:object
		model.addAttribute("produto", produto);
		return "produtos/editarProduto";
	}
	
	//alterar
	@PostMapping("/update/{codigo}")
	public String updateProduto(@PathVariable("codigo") long codigo, @Valid Produto produto, BindingResult result,
			Model model) {
		if(result.hasErrors()) {
			produto.setCodigo(codigo);
			return "redirect:/produtos/editarProduto";
		}
		
		produtoRepository.save(produto);
		model.addAttribute("produtos", produtoRepository.findAll());
		return "redirect:/produtos";
		
	}
	
}
