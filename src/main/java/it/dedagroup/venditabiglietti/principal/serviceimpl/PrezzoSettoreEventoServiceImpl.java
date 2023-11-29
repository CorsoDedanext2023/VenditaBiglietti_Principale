package it.dedagroup.venditabiglietti.principal.serviceimpl;

import it.dedagroup.venditabiglietti.principal.dto.request.ModifyPSEDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.PrezzoSettoreEventoDtoRequest;
import it.dedagroup.venditabiglietti.principal.dto.response.PrezzoSettoreEventoMicroDTO;
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
    public PrezzoSettoreEventoMicroDTO findPSEById(Long idPrezzoSettoreEvento) {
        String mioPath=path+"/id/"+idPrezzoSettoreEvento;
        return callGet(mioPath,idPrezzoSettoreEvento,PrezzoSettoreEventoMicroDTO.class);
    }

    @Override
    public List<PrezzoSettoreEvento> findByEventiIds(List<Long> ids) {
        String mioPath = path+"ids-evento";
        return callPostForList(mioPath, ids, PrezzoSettoreEvento[].class);
    }

    @Override
    public List<PrezzoSettoreEventoMicroDTO> findByEventiIdsList(List<Long> ids) {
        return callPostForList(path+"/ids-evento",ids,PrezzoSettoreEventoMicroDTO[].class);
    }

    @Override
    public List<PrezzoSettoreEvento> findAllByIdEvento(long idEvento) {
        String mioPath=path+"lista-by-evento/id-evento/"+idEvento;
        return callGetForList(mioPath,idEvento,PrezzoSettoreEvento[].class);

    }

    @Override
    public List<PrezzoSettoreEventoMicroDTO> findAllPSEByIdEvento(long idEvento) {
        String mioPath=path+"lista-by-evento-is-cancellato-false/id-evento/"+idEvento;
        return callGetForList(mioPath,idEvento,PrezzoSettoreEventoMicroDTO[].class);
    }
    @Override
    public void modificaEvento(long idPrezzoSettoreEvento, long idEvento){
        callPost(path + "modifica-evento/id-pse/"+idPrezzoSettoreEvento+"/id-evento/"+idEvento,null,Void.class);
    }
    @Override
    public void modificaSettore(long idPrezzoSettoreEvento, long idSettore){
        callPost(path + "modifica-settore/id-pse/" + idPrezzoSettoreEvento + "/idSettore/" + idSettore, null, Void.class);
    }
    @Override
    public void modificaPrezzo(long idSettore, long idEvento, double prezzo){
        PrezzoSettoreEventoDtoRequest request = new PrezzoSettoreEventoDtoRequest(idSettore,idEvento,prezzo);
        callPost(path + "modifica-prezzo",  request, Void.class);
    }
}
