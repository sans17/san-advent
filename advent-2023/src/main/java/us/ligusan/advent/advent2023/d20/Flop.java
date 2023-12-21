package us.ligusan.advent.advent2023.d20;

import java.util.List;

class Flop extends AbstractModule {
    private boolean value;

    public Flop(final List<String> outputs) {
        super(outputs);
    }

    @Override
    public Boolean state() {
        return value;
    }

    @Override
    public Boolean processSignal(final String input, final boolean signal) {
        if (!signal) {
            value = !value;
            return value;
        }

        return null;
    }

    @Override
    public String toString() {
        return "% -> " + outputs();
    }
}
