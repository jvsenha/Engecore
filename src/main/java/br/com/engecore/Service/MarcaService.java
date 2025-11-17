package br.com.engecore.Service;

import br.com.engecore.DTO.MarcaDTO;
import br.com.engecore.Entity.MarcaEntity;
import br.com.engecore.Mapper.MarcaMapper;
import br.com.engecore.Repository.MarcaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MarcaDTO cadastrar(MarcaDTO dto) {
        MarcaEntity marca = new MarcaEntity();
        marca.setNome(dto.getNome());
        marcaRepository.save(marca);
        return MarcaMapper.toDTO(marca);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MarcaDTO atualizar(Long id, MarcaDTO dto) {
        MarcaEntity marca = buscarMarca(id);
        marca.setNome(dto.getNome());
        marcaRepository.save(marca);
        return MarcaMapper.toDTO(marca);
    }

    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncAdm(authentication)")
    public void deletarMarca(Long id) {
        // Cuidado: Isso pode falhar se a marca estiver em uso.
        // Você pode querer adicionar tratamento de exceção, como no GlobalExceptionHandler.
        marcaRepository.deleteById(id);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public List<MarcaEntity> listar() {
        return marcaRepository.findAll();
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MarcaDTO detalhesMarca(Long id) {
        MarcaEntity marca = buscarMarca(id);
        return MarcaMapper.toDTO(marca);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication)")
    public MarcaEntity buscarMarca(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marca não encontrada: " + id));
    }
}