package us.ligusan.advent.advent2023.d20;

import java.util.List;

class AbstractModule implements Module {
    private List<String> outputs;

    protected AbstractModule() {
    }

    protected AbstractModule(final List<String> newOutputs) {
        outputs = newOutputs;
    }

    @Override
    public List<String> outputs() {
        return outputs;
    }
}
