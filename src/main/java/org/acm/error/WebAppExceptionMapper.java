package org.acm.error;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.time.Instant;
import java.util.Map;

@Provider
public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", ex.getResponse().getStatus(),
                "message", ex.getMessage()
        );
        return Response.fromResponse(ex.getResponse())
                .type(MediaType.APPLICATION_JSON)
                .entity(body).build();
    }
}
