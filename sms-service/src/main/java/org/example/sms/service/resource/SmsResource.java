package org.example.sms.service.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.example.sms.core.entities.api.SmsDTO;
import org.example.sms.core.entities.broker.SmsMessageBR;
import org.example.sms.core.entities.orm.sms.Sms;
import org.example.sms.core.entities.orm.sms.SmsDeliveryStatusEnum;
import org.example.sms.core.entities.orm.user.UserRoleEnum;
import org.example.sms.service.broker.SmsOutServiceHandler;
import org.example.sms.service.orm.SmsRepository;
import org.example.sms.service.utils.Validator;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.logging.Logger;

import java.util.Set;

@Path("/sms")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "SMS", description = "Endpoints for sending and retrieving SMS messages")
public class SmsResource {
    private static final Logger logger = Logger.getLogger(SmsResource.class);

    @Inject
    SmsRepository smsRepository;

    @Inject
    SmsOutServiceHandler smsSender;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/send")
    @Operation(summary = "Send an SMS message", description = "Sends an SMS message if user role is authorized and SMS is valid")
    @APIResponse(
            responseCode = "201",
            description = "SMS message sent successfully"
    )
    @APIResponse(
            responseCode = "400",
            description = "SMS message failed validation"
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized user"
    )
    @RequestBody(
            description = "SMS message to send",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = SmsDTO.class),
                    examples = {
                            @ExampleObject(
                                    name = "Valid SMS",
                                    summary = "Example of a valid SMS",
                                    value = """
                                        {
                                            "recipient": "+1234567890",
                                            "content": "Hello, this is a test message."
                                        }
                                        """
                            )
                    }
            )
    )
    public Response sendSms(SmsDTO smsDTO) {

        String content = smsDTO.getContent();
        String recipient = smsDTO.getRecipient();
        Set<String> userRoles = jwt.getGroups();
        String phoneNumber = jwt.getClaim("phone");
        String username = jwt.getName();

        if (!Validator.userRoleIsValid(userRoles, UserRoleEnum.USER)) {
            logger.warnf("User with username: %s, tried to access /send but failed due to user role.", username);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Not authorized user.")
                    .build();
        }

        Sms sms = new Sms();
        sms.setContent(content);
        sms.setRecipient(recipient);
        sms.setSender(phoneNumber);
        smsRepository.saveSms(sms);

        if (!Validator.smsIsValid(sms)) {
            logger.warnf("User with username: %s, sent an invalid sms with id: %s.", username, sms.getId().toString());
            smsRepository.updateStatus(sms.getId(), SmsDeliveryStatusEnum.NOT_VALID);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sms failed validation.")
                    .build();
        }

        logger.infof("User with username: %s, sent successfully the sms with id: %s.", username, sms.getId().toString());
        smsRepository.updateStatus(sms.getId(), SmsDeliveryStatusEnum.VALID);
        smsSender.sendSms(new SmsMessageBR(sms));

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/listOutgoing")
    @Operation(summary = "List outgoing SMS messages", description = "Returns SMS messages sent by the authenticated user")
    @APIResponse(
            responseCode = "200",
            description = "List of outgoing SMS messages returned",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SmsDTO.class, type = SchemaType.ARRAY))
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized user"
    )
    public Response listOutgoingSms() {
        Set<String> userRoles = jwt.getGroups();
        String phoneNumber = jwt.getClaim("phone");
        String username = jwt.getName();

        if (!Validator.userRoleIsValid(userRoles, UserRoleEnum.USER)) {
            logger.warnf("User with username: %s, tried to access /listOutgoing but failed due to user role.", username);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Not authorized user.")
                    .build();
        }

        logger.infof("User with username: %s, access /listOutgoing successfully.", username);

        return Response.status(Response.Status.OK)
                .entity(smsRepository.listOutgoingSms(phoneNumber))
                .build();
    }

    @GET
    @Path("/listIncoming")
    @Operation(summary = "List incoming SMS messages", description = "Returns SMS messages received by the authenticated user")
    @APIResponse(
            responseCode = "200",
            description = "List of incoming SMS messages returned",
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SmsDTO.class, type = SchemaType.ARRAY))
    )
    @APIResponse(
            responseCode = "401",
            description = "Unauthorized user"
    )
    public Response listIncomingSms() {
        Set<String> userRoles = jwt.getGroups();
        String phoneNumber = jwt.getClaim("phone");
        String username = jwt.getName();

        if (!Validator.userRoleIsValid(userRoles, UserRoleEnum.USER)) {
            logger.warnf("User with username: %s, tried to access /listIncoming but failed due to user role.", username);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Not authorized user.")
                    .build();
        }

        logger.infof("User with username: %s, access /listIncoming successfully.", username);

        return Response.status(Response.Status.OK)
                .entity(smsRepository.listIncomingSms(phoneNumber))
                .build();
    }
}
