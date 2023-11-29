package it.dedagroup.venditabiglietti.principal.serviceimpl;

import it.dedagroup.venditabiglietti.principal.model.PrezzoSettoreEvento;
import it.dedagroup.venditabiglietti.principal.service.GeneralCallService;
import it.dedagroup.venditabiglietti.principal.service.PrezzoSettoreEventoServiceDef;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrezzoSettoreEventoServiceImpl implements PrezzoSettoreEventoServiceDef, GeneralCallService {

    String path = "http://localhost:8086/prezzi-settore-evento/";
    @Override
    public PrezzoSettoreEvento findById(Long idPrezzoSettoreEvento) {
        String mioPath=path+"id/"+ idPrezzoSettoreEvento;
        return callGet(mioPath, idPrezzoSettoreEvento, PrezzoSettoreEvento.class);
    }

    @Override
    public List<PrezzoSettoreEvento> findByEventiIds(List<Long> ids) {
        String mioPath = path+"ids-evento";
        return callGetForList(mioPath, ids, PrezzoSettoreEvento[].class);
    }

    @Override
    public List<PrezzoSettoreEvento> findAllByIdEvento(long idEvento) {
        String mioPath=path+"lista-by-evento/"+idEvento;
        return callGetForList(mioPath,idEvento,PrezzoSettoreEvento[].class);

    }


}
