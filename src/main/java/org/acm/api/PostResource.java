package org.acm.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acm.dto.MergedPostDto;
import org.acm.service.PostService;

import java.util.List;

@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

    @Inject
    PostService service;

    // Endpoint principal
    @GET
    public Response getMerged() {
        List<MergedPostDto> merged = service.getMergedPosts();
        return Response.ok(merged).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        if (id == null || id <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("id debe ser positivo").build();
        }
        service.deleteRemotePost(id);

        return Response.noContent().build();
    }
}
