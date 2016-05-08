package stopWatch;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Translator implements Runnable {
	private final Consumer<String> display;
	private int period = 500;
	private boolean translate = false;
	private Input input;

	public static class Input<T> {
		Supplier<T> supplier;
		Function<T, String> function;

		public Input(Supplier<T> supplier, Function<T, String> function) {
			this.supplier = supplier;
			this.function = function;
		}

		public String get() {
			return function.apply(supplier.get());
		}
	}

	public Translator(Consumer<String> display, Input input) {
		this.display = display;
		this.input = input;
	}

	public Translator setPeriod(int period) {
		this.period = period;
		return this;
	}

	public synchronized Translator translate() {
		notify();
		translate = true;
		return this;
	}

	public Translator stopTranslate() {
		translate = false;
		return this;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (!translate) pause();
				display.accept(input.get());
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