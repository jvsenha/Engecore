package br.com.engecore.Repository;

import br.com.engecore.DTO.CustoInsumoDTO;
import br.com.engecore.Entity.MovFinanceiraEntity;
import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMovFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MovFinanceiraRepository extends JpaRepository<MovFinanceiraEntity, Long> {

 // Buscar por ID da obra
 List<MovFinanceiraEntity> findByObraId(Long idObra);

 // Buscar por ID do funcionário responsável
 List<MovFinanceiraEntity> findByFuncionarioResponsavelId(Long id);

 List<MovFinanceiraEntity> findByObraIsNullAndClienteIsNull();

 List<MovFinanceiraEntity> findByClienteId(Long clienteId);

 @Query("SELECT SUM(m.valor) FROM MovFinanceiraEntity m WHERE m.obra.id = :obraId AND m.tipo = :tipoDespesa")
 BigDecimal sumDespesasByObraId(
         @Param("obraId") Long obraId,
         @Param("tipoDespesa") TipoMovFinanceiro tipoDespesa
 );

 @Query("SELECT new br.com.engecore.DTO.CustoInsumoDTO(i.id, i.nome, SUM(m.valor)) " +
         "FROM MovFinanceiraEntity m JOIN m.insumo i " +
         "WHERE m.tipo = :tipoDespesa AND m.categoriaFinanceira = :categoriaCompra " +
         "GROUP BY i.id, i.nome " +
         "ORDER BY SUM(m.valor) DESC")
 List<CustoInsumoDTO> findCustoPorInsumo(
         @Param("tipoDespesa") TipoMovFinanceiro tipoDespesa,
         @Param("categoriaCompra") CategoriaFinanceira categoriaCompra
 );
}
