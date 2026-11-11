package br.edu.ifc.bikes.service;

import br.edu.ifc.bikes.dto.ClienteRequestDTO;
import br.edu.ifc.bikes.dto.ClienteResponseDTO;
import br.edu.ifc.bikes.dto.mapper.ClienteMapper;
import br.edu.ifc.bikes.entity.Cliente;
import br.edu.ifc.bikes.entity.Usuario;
import br.edu.ifc.bikes.exception.CpfUniqueViolationException;
import br.edu.ifc.bikes.exception.EntityNotFoundException;
import br.edu.ifc.bikes.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto, Usuario usuario) {
        try {
            Cliente cliente = clienteMapper.toCliente(dto);
            cliente.setUsuario(usuario);
            return clienteMapper.toResponse(clienteRepository.save(cliente));
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("O CPF '%s' não pode ser cadastrado, já existe no sistema", dto.cpf())
            );
        }
    }

    @Transactional
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente id=%d não encontrado", id))
        );
        return clienteMapper.toResponse(cliente);
    }

    @Transactional
    public ClienteResponseDTO buscarPorUsuarioId(Long idUsuario) {
        Cliente cliente = clienteRepository.findByUsuarioId(idUsuario).orElseThrow(
                () -> new EntityNotFoundException(String.format("Cliente vinculado ao usuário id=%d não encontrado", idUsuario))
        );
        return clienteMapper.toResponse(cliente);
    }

    @Transactional
    public List<ClienteResponseDTO> buscarTodos() {
        return clienteMapper.toResponse(clienteRepository.findAll());
    }
}
