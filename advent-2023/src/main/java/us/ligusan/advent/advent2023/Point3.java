package us.ligusan.advent.advent2023;

public record Point3(int x, int y, int z) {
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}
