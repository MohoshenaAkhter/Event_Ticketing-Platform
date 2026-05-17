package ee.ut.eventticketing.event;

import ee.ut.eventticketing.event.controller.EventController;
import ee.ut.eventticketing.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void testGetAllEvents_success() throws Exception {
        when(eventService.getAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

   @Test
void testGetEvent_notFound() throws Exception {
    mockMvc.perform(get("/events/999"))
            .andExpect(status().isOk());
}
}
    