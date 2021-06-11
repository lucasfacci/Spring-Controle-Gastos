package controle.gasto;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controle-gasto")
public class Gasto {
	
	@Autowired
	private GastoRepository gastoRepo = null;
	
	@Autowired
	private UsuarioRepository usuarioRepo = null;
	
	private static final Logger logger = LoggerFactory.getLogger(Gasto.class);
	
	@RequestMapping(path = "/gasto", method = RequestMethod.POST)
	public String criarGasto(@RequestBody GastoBean novoGasto) {
		
		logger.debug(novoGasto.getUsername());
		logger.debug(novoGasto.getCategoria());
		logger.debug("Valor = " + novoGasto.getValor());
		logger.debug(novoGasto.getData());
		
		ArrayList<String> valid = new ArrayList<String>();
		
		Iterable<UsuarioBean> usuarios = usuarioRepo.findAll();
		
		usuarios.forEach(usuario -> {
			if (novoGasto.getUsername().equals(usuario.getUsername())) {
				valid.add("yes");
			}
		});
		
		try {
			if (valid.get(0) == "yes") {
				gastoRepo.save(novoGasto);
				
				return "Gasto criado!";
			}
			else {
				return "Usuário da requisição inexistente!";
			}
		} catch(Exception e) {
			return "Usuário da requisição inexistente!";
		}
		
	}
	
	@RequestMapping("/gasto/{username}")
	public ResponseEntity<List<GastoBean>> getGastos(@PathVariable String username) {
		
		List<GastoBean> allGastos = new ArrayList<GastoBean>();
		
		Iterable<GastoBean> gastos = gastoRepo.findAll();
		
		gastos.forEach(gasto -> {
			if (username.equals(gasto.getUsername())) {
				allGastos.add(gasto);
			}
		});
		
		return new ResponseEntity<List<GastoBean>>(allGastos, HttpStatus.OK);
		
	}
	
	@RequestMapping(path = "/gasto/{id}", method = RequestMethod.DELETE)
	public String deletarGasto(@PathVariable int id) {
		try {
			gastoRepo.deleteById(id);
			return "Gasto deletado!";
		} catch (Exception e) {
			return "Gasto inexistente!";
		}
	}

}
