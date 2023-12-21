package us.ligusan.advent.advent2023.d20;

import java.util.List;

interface Module {
    List<String> outputs();

    default Object state() {
        return "";
    }

    default Boolean processSignal(String input, boolean signal) {
        return null;
    }
}
