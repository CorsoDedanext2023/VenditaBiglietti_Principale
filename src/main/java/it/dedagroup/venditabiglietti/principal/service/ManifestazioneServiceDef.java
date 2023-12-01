package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.request.AddManifestazioneDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.EventiFiltratiDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.request.ManifestazioneCriteriaDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.ManifestazioneMicroDTO;

import java.util.List;

public interface ManifestazioneServiceDef {
	
	
	void eliminaManifestazione(long id);
	ManifestazioneMicroDTO findById(long idManifestazione);
	ManifestazioneMicroDTO findByNome(String nome);
	String save(AddManifestazioneDTORequest request);
	List<ManifestazioneMicroDTO> criteriaManifestazioniFiltrate(ManifestazioneCriteriaDTORequest request);
}
