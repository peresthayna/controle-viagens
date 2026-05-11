package com.agt.viagens.application.service;

import com.agt.viagens.application.dto.request.AtualizarStatusRequest;
import com.agt.viagens.application.dto.request.CriarViagemRequest;
import com.agt.viagens.application.dto.response.ViagemResponse;
import com.agt.viagens.application.mapper.ViagemMapper;
import com.agt.viagens.domain.exception.DataVoltaAnteriorADataIdaException;
import com.agt.viagens.domain.exception.TransicaoStatusInvalidaException;
import com.agt.viagens.domain.exception.ViagemNaoEncontradaException;
import com.agt.viagens.domain.model.StatusViagem;
import com.agt.viagens.domain.model.Usuario;
import com.agt.viagens.domain.model.Viagem;
import com.agt.viagens.domain.port.ViagemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViagemServiceImpl implements ViagemService {

    private final ViagemPort   viagemPort;
    private final ViagemMapper viagemMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ViagemResponse> listarPorUsuario(Usuario usuario) {
    	return viagemPort.buscarPorUsuario(usuario)
    			.stream()
    			.map(viagemMapper::toResponse)
    			.toList();
    }

    @Override
    @Transactional
    public ViagemResponse criar(CriarViagemRequest request, Usuario usuario) {
    	if(request.dataVolta().isBefore(request.dataIda())) {
    		throw new DataVoltaAnteriorADataIdaException();
    	}
    	
    	Viagem viagem = Viagem.builder()
    			.criadoEm(LocalDateTime.now())
    			.dataIda(request.dataIda())
    			.dataVolta(request.dataVolta())
    			.destino(request.destino())
    			.finalidade(request.finalidade())
    			.observacoes(request.observacoes())
    			.transporte(request.transporte())
    			.status(StatusViagem.AGENDADA)
    			.usuario(usuario)
    			.build();
    	
    	Viagem novaViagem = viagemPort.salvar(viagem);
    	ViagemResponse viagemConvertida = viagemMapper.toResponse(novaViagem);
        
    	return viagemConvertida;
    }

    @Override
    @Transactional
    public ViagemResponse atualizarStatus(Long id, AtualizarStatusRequest request, Usuario usuario) {
    	Viagem viagemRetornada = viagemPort.buscarPorIdEUsuario(id, usuario)
    			.orElseThrow(() -> new ViagemNaoEncontradaException(id));
    	
    	if(viagemRetornada.getStatus().podeTransicionarPara(request.status())) {
    		viagemRetornada.setStatus(request.status());
    		Viagem viagemAtualizada = viagemPort.salvar(viagemRetornada);
    		return viagemMapper.toResponse(viagemAtualizada);
    	} else {
    		throw new TransicaoStatusInvalidaException(viagemRetornada.getStatus(), request.status());
    	}
    }
}
