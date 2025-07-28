package org.example.sms.service.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.example.sms.core.entities.api.MetricsReportDTO;
import org.example.sms.core.entities.orm.user.UserRoleEnum;
import org.example.sms.service.orm.MetricsRepository;
import org.example.sms.service.utils.Validator;
import org.jboss.logging.Logger;

import java.util.Set;

@Path("/metrics")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Metrics", description = "Endpoints related to SMS metrics reporting")
public class MetricsResource {
    private static final Logger logger = Logger.getLogger(MetricsResource.class);

    @Inject
    MetricsRepository metricsRepository;

    @Inject
    JsonWebToken jwt;

    @GET
    @Operation(summary = "Get SMS metrics report", description = "Returns aggregated SMS metrics for authorized admin users.")
    @APIResponse(
            responseCode = "200",
            description = "Metrics report retrieved successfully",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = MetricsReportDTO.class))
    )
    @APIResponse(
            responseCode = "401",
            description = "User is not authorized to access metrics",
            content = @Content(mediaType = MediaType.TEXT_PLAIN)
    )
    public Response getMetrics() {
        Set<String> userRoles = jwt.getGroups();
        String username = jwt.getName();

        logger.info(userRoles);

        if (!Validator.userRoleIsValid(userRoles, UserRoleEnum.ADMIN)) {
            logger.warnf("User with username: %s, tried to access /metrics but failed due to user role.", username);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Not authorized user.")
                    .build();
        }

        logger.warnf("User with username: %s, access /metrics.", username);

        return Response.status(Response.Status.OK).entity(metricsRepository.getReport()).build();
    }
}
