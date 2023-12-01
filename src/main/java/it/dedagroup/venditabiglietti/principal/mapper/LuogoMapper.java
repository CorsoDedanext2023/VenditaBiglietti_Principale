package it.dedagroup.venditabiglietti.principal.mapper;

import it.dedagroup.venditabiglietti.principal.dto.response.LuogoMicroDTO;
import it.dedagroup.venditabiglietti.principal.dto.response.SettoreMicroDTO;
import it.dedagroup.venditabiglietti.principal.model.Luogo;
import it.dedagroup.venditabiglietti.principal.model.Settore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LuogoMapper {

    public Luogo toLuogo(LuogoMicroDTO l, List<SettoreMicroDTO> s){
        Luogo luogo = new Luogo();
        luogo.setId(l.getId());
        luogo.setRiga1(l.getRiga1());
        luogo.setRiga2(l.getRiga2());
        luogo.setProvincia(l.getProvincia());
        luogo.setCap(l.getCap());
        luogo.setComune(l.getComune());
        luogo.setNazionalita(l.getNazionalita());
        luogo.setCancellato(l.isCancellato());
        luogo.setEventi(new ArrayList<>());
        luogo.setSettori(toSettoriList(s,luogo));
        luogo.setVersion(l.getVersion());
        return luogo;
    }
    
    public Luogo toLuogo(LuogoMicroDTO l) {
    	Luogo luogo = new Luogo();
        luogo.setId(l.getId());
        luogo.setRiga1(l.getRiga1());
        luogo.setRiga2(l.getRiga2());
        luogo.setProvincia(l.getProvincia());
        luogo.setCap(l.getCap());
        luogo.setComune(l.getComune());
        luogo.setNazionalita(l.getNazionalita());
        luogo.setCancellato(l.isCancellato());
        return luogo;
    }

    public List<Luogo> toLuogoList(List<LuogoMicroDTO> listLuoghi,List<SettoreMicroDTO> settori){
        return listLuoghi.stream().map(l->this.toLuogo(l,settori.stream().filter(s->s.getIdLuogo()==l.getId()).toList())).toList();
    }

    public Settore toSettore(SettoreMicroDTO sDTO,Luogo l){
        Settore s=new Settore();
        s.setId(sDTO.getId());
        s.setNome(sDTO.getNome());
        s.setCapienza(sDTO.getCapienza());
        s.setCancellato(sDTO.isCancellato());
        s.setLuogo(l);
        s.setVersion(sDTO.getVersion());
        return s;
    }

    public List<Settore> toSettoriList(List<SettoreMicroDTO> list,Luogo l){
        return list.stream().map(s->this.toSettore(s,l)).toList();
    }


}
