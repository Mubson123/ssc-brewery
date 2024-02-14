package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BeerRestControllerIT extends BaseIT {

  @Test
  void deleteBeerUrl() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed")
            .param("apiKey", "spring").param("apiSecret", "guru"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteBeerBadCredsUrl() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed")
            .param("apiKey", "spring").param("apiSecret", "guruXXXX"))
        .andExpect(status().isUnauthorized());

  }

  @Test
  void deleteBeerBadCreds() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed")
            .header("Api-Key", "spring").header("Api-Secret", "guruXXXX"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void deleteBeer() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed")
        .header("Api-Key", "spring").header("Api-Secret", "guru"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteBeerHttpBasic() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed")
            .with(httpBasic("spring", "guru")))
        .andExpect(status().is2xxSuccessful());

  }

  @Test
  void deleteBeerNoAuth() throws Exception {
    mockMvc.perform(delete("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed"))
        .andExpect(status().isUnauthorized());

  }

  @Test
  void findBeers() throws Exception {
    mockMvc.perform(get("/api/v1/beer/"))
        .andExpect(status().isOk());
  }

  @Test
  void findBeerById() throws Exception {
    mockMvc.perform(get("/api/v1/beer/0f5086a4-c547-4a59-9d51-fb9dad5a84ed"))
        .andExpect(status().isOk());
  } //

  @Test
  void findBeerByUpc() throws Exception {
    mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
        .andExpect(status().isOk());
  }
}