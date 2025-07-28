package org.example.sms.service.resource;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.example.sms.core.entities.api.ProfileDTO;
import org.jboss.logging.Logger;

import java.util.Set;

@Path("/profile")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Profile", description = "Endpoints related to user profile information")
public class ProfileResource {
    private static final Logger logger = Logger.getLogger(ProfileResource.class);

    @Inject
    JsonWebToken jwt;

    @GET
    @Operation(
            summary = "Get user profile",
            description = "Returns the user's profile information extracted from the JWT token claims."
    )
    @APIResponse(
            responseCode = "200",
            description = "User profile retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ProfileDTO.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User is not authenticated"
    )
    public Response getProfile() {
        String name = jwt.getClaim("name");
        String phone = jwt.getClaim("phone");
        String username = jwt.getName();
        Set<String> groups = jwt.getGroups();

        ProfileDTO profile = new ProfileDTO(username, name, phone, groups);

        logger.infof("User with username: %s accessed /profile.", username);

        return Response.ok(profile).build();
    }
}