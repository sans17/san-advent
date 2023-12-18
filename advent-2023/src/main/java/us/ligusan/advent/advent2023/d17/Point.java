package us.ligusan.advent.advent2023.d17;

record Point(int x, int y) {
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
