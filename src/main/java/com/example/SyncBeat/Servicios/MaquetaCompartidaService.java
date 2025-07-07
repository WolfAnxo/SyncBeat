package com.example.SyncBeat.Servicios;

import com.example.SyncBeat.Entidades.Maqueta;
import com.example.SyncBeat.Entidades.MaquetaCompartida;
import com.example.SyncBeat.Entidades.Usuario;
import com.example.SyncBeat.repositorios.MaquetaCompartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquetaCompartidaService {

    @Autowired
    private MaquetaCompartidaRepository maquetaCompartidaRepository;

    public List<MaquetaCompartida> buscarMaquetasCompartidasConUsuario(Long usuarioId) {
        return maquetaCompartidaRepository.findByCompartidoConId(usuarioId);
    }

    public MaquetaCompartida guardarMaquetaCompartida(MaquetaCompartida maquetaCompartida) {
        return maquetaCompartidaRepository.save(maquetaCompartida);
    }
    public void eliminarMaquetasCompartidasPorMaqueta(Maqueta maqueta) {
        maquetaCompartidaRepository.deleteByMaqueta(maqueta);
    }

    public boolean existeMaquetaCompartida(Maqueta maqueta, Usuario usuario) {
        return maquetaCompartidaRepository.existsByMaquetaAndCompartidoCon(maqueta, usuario);
    }
    
    public void eliminarComparticion(Long comparticionId) {
        maquetaCompartidaRepository.deleteById(comparticionId);
    }

    public List<MaquetaCompartida> buscarComparticionesPorMaqueta(Maqueta maqueta) {
        return maquetaCompartidaRepository.findByMaqueta(maqueta);
    }
 
}
