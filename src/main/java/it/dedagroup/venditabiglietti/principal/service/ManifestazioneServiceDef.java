package it.dedagroup.venditabiglietti.principal.service;

import it.dedagroup.venditabiglietti.principal.dto.request.AddManifestazioneDTORequest;
import it.dedagroup.venditabiglietti.principal.dto.response.ManifestazioneMicroDTO;

public interface ManifestazioneServiceDef {
	
	
	void eliminaManifestazione(long id);
	ManifestazioneMicroDTO findById(long idManifestazione);
	ManifestazioneMicroDTO findByNome(String nome);
	String save(AddManifestazioneDTORequest request);
}
