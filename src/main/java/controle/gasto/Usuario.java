package controle.gasto;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controle-gasto")
@CrossOrigin(originPatterns = "*")
public class Usuario {
	
	@Value("${login.ok}")
	String loginOK;
	
	@Value("${login.erro}")
	String loginErro;
	
	@Autowired
	private UsuarioRepository usuarioRepo = null;
	
	private static final Logger logger = LoggerFactory.getLogger(Usuario.class);
	
	@RequestMapping(path = "/usuario", method = RequestMethod.POST)
	public String criarUsuario(@RequestBody UsuarioBean novoUsuario) {
				
		logger.debug(novoUsuario.getUsername());
		logger.debug(novoUsuario.getSenha());
		logger.debug("Meta = " + novoUsuario.getMeta());
		
		usuarioRepo.save(novoUsuario);
		
		return "Usuario criado!";
		
	}
	
	// POST /login – efetua o login com o JSON: {"username": "jsilva", "senha": "123"}. O retorno será {"valido": true OU false}
	
	@RequestMapping("/login/{username}/{senha}")
	public ResponseEntity<String> login(@PathVariable("username") String username,
										@PathVariable("senha") String senha) {
		
		logger.debug("username = " + username);
		
		ArrayList<String> valid = new ArrayList<String>();
		
		Iterable<UsuarioBean> usuarios = usuarioRepo.findAll();
		
		usuarios.forEach(usuario -> {
			if (username.equals(usuario.getUsername()) && senha.equals(usuario.getSenha())) {
				valid.add("yes");
			}
		});
		
		try {
			if (valid.get(0) == "yes") {
				return new ResponseEntity<String>(loginOK, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>(loginErro, HttpStatus.UNAUTHORIZED);
			}
		} catch(Exception e) {
			return new ResponseEntity<String>(loginErro, HttpStatus.UNAUTHORIZED);
		}
	}
		
	@RequestMapping("/meta/{username}")
	public ResponseEntity<List<Float>> meta(@PathVariable String username) {
		
		List<Float> metaList = new ArrayList<Float>();
		
		Iterable<UsuarioBean> usuarios = usuarioRepo.findAll();
		
		usuarios.forEach(usuario -> {
			if (username.equals(usuario.getUsername())) {
				metaList.add(usuario.getMeta());
			}
		});
		
		try {
			return new ResponseEntity<List<Float>>(metaList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<Float>>(metaList, HttpStatus.OK);
		}
		
	}
		
}
