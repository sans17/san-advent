package us.ligusan.advent.advent2023.d20;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D20p1 {

    public static void main(final String[] args) {
        final var modules = new HashMap<String, Module>();

        final var prefix_broadcaster = "broadcaster -> ";
        try (var scanner = new Scanner(D20p1.class.getResourceAsStream("input.txt"))) {
            scanner.useDelimiter("\r?\n").tokens().forEach(s -> {
                System.out.format("s=%s\n", s);
                final var matchResult = Pattern.compile("([%&])?([a-z]+) -> (.+)").matcher(s).results().findFirst().get();
                final var moduleType = matchResult.group(1);
                System.out.format("moduleType=%s\n", moduleType);
                final var outputs = Arrays.stream(matchResult.group(3).split(", ")).collect(Collectors.toList());
                modules.put(matchResult.group(2), moduleType == null ? new AbstractModule(outputs) {
                    @Override
                    public Boolean processSignal(String input, boolean signal) {
                        return signal;
                    }
                } : "%".equals(moduleType) ? new Flop(outputs) : new Conjunction(outputs));
            });
        }
        for (final var moduleEntry : modules.entrySet())
            for (final var moduleOutput : moduleEntry.getValue().outputs()) {
                final var module = modules.get(moduleOutput);
                if (module instanceof Conjunction) ((Conjunction) module).addInput(moduleEntry.getKey());
            }
        modules.putAll(modules.values().stream().flatMap(module -> module.outputs().stream()).filter(module -> !modules.containsKey(module)).collect(Collectors.toMap(Function.identity(), module -> new AbstractModule())));
        System.out.format("modules=%s\n", modules);

        final var states = new HashMap<Map<String, Object>, Integer>();
        final var allActions = new ArrayList<Signal>();

        int i = 0;
        for (; i < 1000; i++) {
            var currentState = modules.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().state()));

            final var stateI = states.get(currentState);
            if (states.containsKey(currentState)) {
                System.out.format("stateI=%d\n", stateI);
                break;
            }

            states.put(currentState, i);

            for (var actionsList = Collections.singletonList(new Signal("button", false, "broadcaster")); !actionsList.isEmpty(); ) {
                System.out.format("actionsList=%s\n", actionsList);

                allActions.addAll(actionsList);

                final var newActionsList = new ArrayList<Signal>();
                for (final var signal : actionsList) {
//                    System.out.format("signal=%s\n", signal);

                    final var signalOutput = signal.output();

                    final var module = modules.get(signalOutput);
                    Boolean newSignal = module.processSignal(signal.input(), signal.value());
                    if (newSignal != null)
                        newActionsList.addAll(module.outputs().stream().map(output -> new Signal(signalOutput, newSignal, output)).collect(Collectors.toList()));
                }

                actionsList = newActionsList;
            }
        }
        System.out.format("i=%d\n", i);

        int lowCounter = 0;
        int highCounter = 0;
        for (final var signal : allActions) {
            if (signal.value())
                highCounter++;
            else
                lowCounter++;
        }
        System.out.format("lowCounter=%d, highCounter=%d\n", lowCounter, highCounter);

        System.out.println(BigInteger.valueOf(lowCounter).multiply(BigInteger.valueOf(highCounter).multiply(BigInteger.valueOf(1000)).divide(BigInteger.valueOf(i))));
    }
}


