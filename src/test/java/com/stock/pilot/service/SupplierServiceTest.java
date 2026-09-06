package com.stock.pilot.service;

import com.stock.pilot.dto.supplier.SupplierRequest;
import com.stock.pilot.mapper.SupplierMapper;
import com.stock.pilot.model.Supplier;
import com.stock.pilot.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.OffsetDateTime; import java.util.List; import java.util.UUID;
import static org.assertj.core.api.Assertions.*; import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {
 @Mock SupplierRepository repo; @Mock SupplierMapper mapper; @InjectMocks SupplierService service;
 @Test void createRejectsDuplicateCode(){ when(repo.existsByCode("ACME")).thenReturn(true); assertThatThrownBy(() -> service.create(new SupplierRequest("ACME","Acme",null,null))).isInstanceOf(RuntimeException.class); verify(repo,never()).save(any()); }
 @Test void createSavesNormalizedCode(){ when(repo.existsByCode("ACME")).thenReturn(false); UUID id=UUID.randomUUID(); Supplier s=new Supplier(id,"ACME","Acme",null,null,true,OffsetDateTime.now(),OffsetDateTime.now(),1); when(repo.save(any())).thenReturn(s); when(mapper.toResponse(s)).thenReturn(null); service.create(new SupplierRequest(" acme ","Acme",null,null)); verify(repo).save(argThat(x -> x.getCode().equals("ACME"))); }
 @Test void searchDelegatesPagination(){ PageRequest page=PageRequest.of(0,20); when(repo.search("acme",true,page)).thenReturn(new PageImpl<>(List.of())); assertThat(service.search(" acme ",true,page)).isEmpty(); verify(repo).search("acme",true,page); }
}
