package fr.alexdoru.mwe.commands;

import fr.alexdoru.mwe.features.ClientIRC;
import net.minecraft.command.ICommandSender;

public class CommandIRC extends MyAbstractCommand {
    @Override
    public String getCommandName() {
        return "i";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        ClientIRC.sendMessage( String.join(" ", strings));
    }
}
