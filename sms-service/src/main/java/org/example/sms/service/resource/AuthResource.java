package org.example.sms.service.resource;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.example.sms.core.entities.api.JwtDTO;
import org.example.sms.core.entities.api.UserDTO;
import org.example.sms.core.entities.orm.user.User;
import org.example.sms.core.entities.orm.user.UserRoleEnum;
import org.example.sms.service.orm.RoleRepository;
import org.example.sms.service.orm.UserRepository;
import org.jboss.logging.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentication", description = "Endpoints related to user authentication")
public class AuthResource {

    private static final Logger logger = Logger.getLogger(AuthResource.class);

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @POST
    @Path("/login")
    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and returns a JWT token if successful."
    )
    @APIResponse(
            responseCode = "200",
            description = "Successfully authenticated. JWT token returned.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = JwtDTO.class),
                    examples = @ExampleObject(
                            name = "JWT Token Response",
                            value = "{ \"jwt\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6...\" }"
                    )
            )
    )
    @APIResponse(
            responseCode = "401",
            description = "Authentication failed due to invalid credentials."
    )
    public Response login(
            @RequestBody(
                    description = "User login credentials",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(
                                    name = "Login Example",
                                    value = "{ \"username\": \"admin\", \"password\": \"admin\" }"
                            )
                    )
            )
            UserDTO userInfo
    ) {
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();

        User user = userRepository.getUser(username);

        if (user == null) {
            logger.infof("Unauthorized user tried to login with username: %s.", username);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (!result.verified) {
            logger.infof("User with username: %s, failed to login.", username);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        logger.infof("User with username: %s, successfully login.", username);

        List<UserRoleEnum> userRoles = roleRepository.getUserRoles(user.getId());
        Set<String> roles = new HashSet<>();
        userRoles.forEach(role -> roles.add(role.name().toLowerCase()));

        String token = Jwt.issuer("demo-app")
                .upn(username)
                .groups(roles)
                .claim("name", user.getFullname())
                .claim("phone", user.getPhone())
                .sign();

        return Response.ok(new JwtDTO(token)).build();
    }
}
