package us.ligusan.advent.advent2023.d20;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Conjunction extends AbstractModule {
    private HashMap<String, Boolean> inputs = new HashMap<>();

    public Conjunction(List<String> outputs) {
        super(outputs);
    }

    public void addInput(String input) {
        inputs.put(input, false);
    }

    @Override
    public HashMap<String, Boolean> state() {
        return inputs;
    }

    @Override
    public Boolean processSignal(final String input, final boolean signal) {
        inputs.put(input, signal);
        return inputs.values().stream().allMatch(a -> a) ? false : true;
    }

    @Override
    public String toString() {
        return "& " + inputs.keySet().stream().collect(Collectors.joining(",")) + " -> " + outputs();
    }
}
