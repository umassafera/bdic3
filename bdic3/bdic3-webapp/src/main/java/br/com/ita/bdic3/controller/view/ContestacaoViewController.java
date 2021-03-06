package br.com.ita.bdic3.controller.view;

import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.ita.bdic3.service.ContestacaoService;
import br.com.ita.bdic3.vo.ContestacaoVO;

@Controller
@RequestMapping("/contestacao")
public class ContestacaoViewController {

	private static final String VIEW_CONTESTACAO1 = "view.contestacao1"; // validar cliente
	private static final String VIEW_CONTESTACAO2 = "view.contestacao2"; // validar transacao
	private static final String VIEW_CONTESTACAO3 = "view.contestacao3"; // filtrar transacoes
	private static final String VIEW_CONTESTACAO4 = "view.contestacao4"; // confirar fraude

	@Autowired
	private ContestacaoService contestacaoService;

	@RequestMapping(value = "/contestacao1", method = RequestMethod.GET)
	public String validacao(Model model) {
		model.addAttribute("contestacaoVO", new ContestacaoVO());
		return VIEW_CONTESTACAO1;
	}

	@RequestMapping(value = "/contestacao2", method = RequestMethod.POST)
	public String validarCliente(@ModelAttribute("contestacaoVO") ContestacaoVO contestacaoVO,	Model model) {
		if (contestacaoService.validarCliente(contestacaoVO)) {
			return VIEW_CONTESTACAO2;
		} else {
			model.addAttribute("mensagemErro", "Cliente Inexistente");
			return VIEW_CONTESTACAO1;
		}
	}

	@RequestMapping(value = "/contestacao3", method = RequestMethod.POST)
	public String validarTransacao(@ModelAttribute("contestacaoVO") ContestacaoVO contestacaoVO, Model model) {
		Long tra_id = contestacaoService.validarTransacao(contestacaoVO);
		
		System.out.println(tra_id);
		
		if (tra_id > 0) {
			contestacaoVO.setTraId(tra_id);
			return VIEW_CONTESTACAO3;
		} else if(tra_id == 0) {
			model.addAttribute("mensagemErro", "Transacao Inexistente");
			return VIEW_CONTESTACAO2;
		} else {
			model.addAttribute("mensagemErro", "Erro ao Consultar");
			return VIEW_CONTESTACAO2;
		}
	}

	@RequestMapping(value = "/contestacao4", method = RequestMethod.POST)
	public String filtrar(@ModelAttribute("contestacaoVO") ContestacaoVO contestacaoVO, Model model) {
 
		boolean retorno = contestacaoService.cadastrarFraude(contestacaoVO);
		
		return VIEW_CONTESTACAO4;
	}

}
