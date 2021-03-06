package br.com.ita.bdic3.service;

import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ita.bdic3.dao.ClienteDao;
import br.com.ita.bdic3.dao.FraudeDao;
import br.com.ita.bdic3.entity.Cliente;
import br.com.ita.bdic3.entity.Fraude;
import br.com.ita.bdic3.entity.FraudeTipo;
import br.com.ita.bdic3.entity.Transacao;
import br.com.ita.bdic3.vo.ContestacaoVO;
import br.ita.bdic3.hive.dao.ContestacaoDao;

@Component
public class ContestacaoService {

	@Autowired
	private ClienteDao clienteDao;

	@Autowired
	private FraudeDao fraudeDao;

	private ContestacaoDao contestacaoDao = new ContestacaoDao();

	public boolean validarCliente(ContestacaoVO contestacaoVO) {
		Cliente cliente = clienteDao.findByNomeAndCpf(
				contestacaoVO.getNomeCliente(), contestacaoVO.getCpfCliente());
		if (cliente != null) {
			return true;
		} else {
			return false;
		}
	}

	public Long validarTransacao(ContestacaoVO contestacaoVO) {
		return contestacaoDao.findTransacaoByValorOrData(contestacaoVO);
	}

	public List<ContestacaoVO> filtrar(ContestacaoVO contestacaoVO) {
		return contestacaoDao
				.findTransacaoByIntervaloDataAndIntervaloValor(contestacaoVO);
	}

	public boolean cadastrarFraude(ContestacaoVO contestacaoVO) {
		Transacao t = new Transacao();
		t.setId(contestacaoVO.getTraId());

		Fraude f = new Fraude();
		f.setNome("CONTESTACAO");
		
		String tipo = contestacaoVO.getFraudeTipo();
		
		if(tipo.equals(FraudeTipo.ESTELIONATO.toString())) {
			f.setTipo(FraudeTipo.ESTELIONATO);
		} else if (tipo.equals(FraudeTipo.AUTO_FRAUDE.toString())) {
			f.setTipo(FraudeTipo.AUTO_FRAUDE);
		} else if(tipo.equals(FraudeTipo.FRAUDE_AMIGA.toString())) {
			f.setTipo(FraudeTipo.FRAUDE_AMIGA);
		}
		
		f.setTransacao(t);
		f.setFormaDeteccao("DENUNCIA");
		f.setData(new Date(System.currentTimeMillis()));

		try {
			fraudeDao.save(f);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
