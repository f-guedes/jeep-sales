package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, config = @SqlConfig(encoding = "utf-8"))
class CreateOrderTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int serverPort;

  /**
   * 
   */
  @Test
  void testCreateOrderReturnsSuccess201() {
    // Given: an order as JSON
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);

    // When: the order is sent
    ResponseEntity<Order> response =
        restTemplate.exchange(uri, HttpMethod.POST, bodyEntity, Order.class);

    // Then: a 201 status is returned
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    // And: the returned order is correct
    assertThat(response.getBody()).isNotNull();

    Order order = response.getBody();
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("IGNATOV_GISELLA");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.COMPASS);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("80th Anniversary");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_SPITFIRE_ORANGE");
    assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_TURBO");
    assertThat(order.getTire().getTireId()).isEqualTo("235_CONTINENTAL");
    assertThat(order.getOptions()).hasSize(6);
  }

  /**
   * 
   * @return
   */
  protected String createOrderBody() {
    // @formatter:off
    return "{\n"
        + "  \"customer\":\"IGNATOV_GISELLA\",\n"
        + "  \"model\":\"COMPASS\",\n"
        + "  \"trim\":\"80th Anniversary\",\n"
        + "  \"doors\":4,\n"
        + "  \"color\":\"EXT_SPITFIRE_ORANGE\",\n"
        + "  \"engine\":\"2_0_TURBO\",\n"
        + "  \"tire\":\"235_CONTINENTAL\",\n"
        + "  \"options\":[\n"
        + "    \"DOOR_QUAD_4\",\n"
        + "    \"EXT_AEV_LIFT\",\n"
        + "    \"EXT_WARN_WINCH\",\n"
        + "    \"EXT_WARN_BUMPER_FRONT\",\n"
        + "    \"EXT_WARN_BUMPER_REAR\",\n"
        + "    \"EXT_ARB_COMPRESSOR\"\n"
        + "  ]\n"
        + "}";
    // @formatter:on
  }

}
