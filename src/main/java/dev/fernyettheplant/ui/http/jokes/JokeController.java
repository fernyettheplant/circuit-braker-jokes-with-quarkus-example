package dev.fernyettheplant.ui.http.jokes;

import dev.fernyettheplant.application.service.CommandHandler;
import dev.fernyettheplant.application.service.sendjokeforcreation.command.SendJokeForCreationCommand;
import dev.fernyettheplant.ui.http.jokes.response.JokeResponseItem;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/jokes")
public class JokeController {
    private final CommandHandler<SendJokeForCreationCommand, Void> sendJokeForCreation;

    public JokeController(CommandHandler<SendJokeForCreationCommand, Void> sendJokeForCreation) {
        this.sendJokeForCreation = sendJokeForCreation;
    }

    @POST
    public Response getRandom() {
        sendJokeForCreation.handle(new SendJokeForCreationCommand("memes"));

        return Response.ok(new JokeResponseItem("thats the joke")).build();
    }
}
