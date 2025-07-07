package com.example.SyncBeat.Servicios;

import com.example.SyncBeat.Entidades.Maqueta;
import com.example.SyncBeat.Entidades.MaquetaCompartida;
import com.example.SyncBeat.Entidades.Rol;
import com.example.SyncBeat.repositorios.MaquetaCompartidaRepository;
import com.example.SyncBeat.repositorios.MaquetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaquetaService {

	@Autowired
	private MaquetaRepository maquetaRepository;

	@Autowired
	private MaquetaCompartidaRepository maquetaCompartidaRepository;

	public List<Maqueta> listarMaquetas() {
		return maquetaRepository.findAll();
	}

	public Optional<Maqueta> obtenerMaquetaPorId(Long id) {
		return maquetaRepository.findById(id);
	}

	public Maqueta guardarMaqueta(Maqueta maqueta) {
		return maquetaRepository.save(maqueta);
	}

	public void eliminarMaqueta(Long id) {
		maquetaRepository.deleteById(id);
	}

	public Resource obtenerArchivoMaqueta(Long id) {
		Optional<Maqueta> maquetaOptional = maquetaRepository.findById(id);
		if (maquetaOptional.isPresent()) {
			Maqueta maqueta = maquetaOptional.get();
			return new ByteArrayResource(maqueta.getArchivoAudio());
		} else {
			return null;
		}
	}

	public Maqueta guardarMaqueta(Maqueta maqueta, byte[] archivoAudio) {
		if (archivoAudio != null) {
			maqueta.setArchivoAudio(archivoAudio);
		}
		return maquetaRepository.save(maqueta);
	}

	public List<Maqueta> obtenerMaquetasDeUsuario(Long usuarioId, Rol rol) {
		if (rol == Rol.ARTISTA) {
			return maquetaRepository.findByArtistaId(usuarioId);
		} else if (rol == Rol.PRODUCTOR) {
			return maquetaRepository.findByProductorId(usuarioId);
		} else {
			return maquetaRepository.findAll();
		}
	}

	public List<Maqueta> buscarMaquetasCompartidasConUsuario(Long usuarioId) {
		List<MaquetaCompartida> maquetasCompartidas = maquetaCompartidaRepository.findByCompartidoConId(usuarioId);
		return maquetasCompartidas.stream().map(MaquetaCompartida::getMaqueta).collect(Collectors.toList());
	}
}