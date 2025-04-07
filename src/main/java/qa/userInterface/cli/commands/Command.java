package qa.userInterface.cli.commands;

import qa.domain.ports.InputPort;

import java.util.Scanner;

interface Command {
    void execute(Scanner scanner, InputPort service);
    String getName();
}
