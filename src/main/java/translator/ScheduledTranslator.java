package translator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduledTranslator extends TriggeredTranslator implements Runnable {
    int period = 500;
    boolean translate = false;

    public ScheduledTranslator(Supplier<String> input, Consumer<String> output) {
        super(input, output);
    }

    public ScheduledTranslator setPeriod(int period) {
        this.period = period;
        return this;
    }

    public synchronized ScheduledTranslator startTranslate() {
        notify();
        translate = true;
        return this;
    }

    public ScheduledTranslator stopTranslate() {
        translate = false;
        return this;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (!translate) pause();
                translate();
                Thread.sleep(period);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            translate = false;
        }
    }

    private synchronized void pause() throws InterruptedException {
        wait();
    }
}