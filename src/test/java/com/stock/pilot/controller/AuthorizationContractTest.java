package com.stock.pilot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.reflect.Method;
import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationContractTest {
 @Test void supplierEndpointsRequireManager(){ PreAuthorize a=SupplierController.class.getAnnotation(PreAuthorize.class); assertThat(a).isNotNull(); assertThat(a.value()).isEqualTo("hasRole('MANAGER')"); }
}
