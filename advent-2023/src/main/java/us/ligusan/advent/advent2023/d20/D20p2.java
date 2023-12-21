package us.ligusan.advent.advent2023.d20;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D20p2 {

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

//        final var states = new HashMap<Map<String, Object>, Integer>();
//        final var allActions = new ArrayList<Signal>();

        boolean start = false;

        int i = 0;
        outer:
        for (; ; i++) {
//            var currentState = modules.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().state()));

//            final var testModule = modules.get("fh");
//
            System.out.format("i=%d\n", i);
//            System.out.println(testModule.state());
//
//            final var snStream = ((Conjunction) testModule).state().values().stream();
//            if (!start)
//                start = snStream.anyMatch(a -> a);
//            else if (snStream.allMatch(a -> !a))
//                break;

//            final var stateI = states.get(currentState);
//            if (states.containsKey(currentState)) {
////                System.out.format("stateI=%d\n", stateI);
//                break;
//            }

//            states.put(currentState, i);

            for (var actionsList = Collections.singletonList(new Signal("button", false, "broadcaster")); !actionsList.isEmpty(); ) {
//                System.out.format("actionsList=%s\n", actionsList);

//                allActions.addAll(actionsList);

                final var newActionsList = new ArrayList<Signal>();
                for (final var signal : actionsList) {
//                    System.out.format("signal=%s\n", signal);

                    final var signalOutput = signal.output();

                    final var module = modules.get(signalOutput);
                    Boolean newSignal = module.processSignal(signal.input(), signal.value());
                    if (newSignal != null)
                        for (final var output : module.outputs()) {
//                            if (output.equals("ql")) {
                            if(signalOutput.equals("ss") && newSignal) {
//                                if (!newSignal) {
                                    System.out.format("signalOutput=%s, newSignal=%s\n", signalOutput, newSignal);
//                                    if (start)
                                        break outer;
//                                    else
//                                        start = true;
//                                }
                            }

                            newActionsList.add(new Signal(signalOutput, newSignal, output));
                        }
                }

                actionsList = newActionsList;
            }
        }
        System.out.format("i=%d\n", i);
        modules.entrySet().stream().forEach(entry -> {
            final var module = entry.getValue();
            if ((module instanceof Flop) && ((Flop) module).state())
                System.out.format("%s, ", entry.getKey());
        });
        System.out.println();
    }
}
