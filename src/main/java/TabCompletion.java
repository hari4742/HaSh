import java.io.IOException;
import java.util.*;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class TabCompletion {
    List<String> strings;

    public TabCompletion(List<String> strings) {
        this.strings = strings;
    }

    public String read() throws IOException {

        Terminal terminal = TerminalBuilder.builder().dumb(true).build();
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).completer(new WordCompleter()).build();
        String prompt = "$ ";
        try {
            String input = lineReader.readLine(prompt);
            return input;
        } catch (UserInterruptException e) {
            System.exit(0);
            return "exit";
        }
    }

    class WordCompleter implements Completer {
        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            String prefix = line.line().substring(0, line.cursor());
            strings.stream().filter(word -> word.startsWith(prefix))
                    .forEach(word -> candidates.add(new Candidate(word)));
        }
    }

}
