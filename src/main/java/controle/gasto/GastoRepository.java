package controle.gasto;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends CrudRepository<GastoBean, Integer>{

}
