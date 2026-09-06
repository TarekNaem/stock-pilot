package com.stock.pilot.service;

import com.stock.pilot.service.ReportService; import com.stock.pilot.repository.*; import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.*; import java.time.OffsetDateTime; import static org.assertj.core.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class ReportServiceTest { @Mock ProductRepository p; @Mock SupplierRepository s; @Mock StockMovementRepository m;
 @Test void inventoryCalculatesNetMovement(){ when(p.countByActiveTrue()).thenReturn(10L); when(p.sumActiveQuantityOnHand()).thenReturn(100L); when(p.countActiveLowStock()).thenReturn(2L); when(p.countByActiveTrueAndQuantityOnHand(0)).thenReturn(1L); when(s.countByActiveTrue()).thenReturn(3L); when(m.countInRange(null,null)).thenReturn(8L); when(m.receivedInRange(null,null)).thenReturn(50L); when(m.issuedInRange(null,null)).thenReturn(20L); var r=new ReportService(p,s,m).inventory(null,null); assertThat(r.netMovement()).isEqualTo(30L); assertThat(r.totalMovements()).isEqualTo(8L); }
}
