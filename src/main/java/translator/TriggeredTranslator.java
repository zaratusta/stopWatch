package translator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TriggeredTranslator implements Translator {
    Supplier<String> input;
    Consumer<String> output;

    public TriggeredTranslator(Supplier<String> input, Consumer<String> output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public Translator translate() {
        output.accept(input.get());
        return this;
    }
}