from enum import Enum
import math


class Status(Enum):
    INFECTED = 0
    CLEAN = 1
    WEAKENED = 2
    FLAGGED = 3

    def evolve(self) -> "Status":
        match self:
            case Status.CLEAN:
                return Status.WEAKENED
            case Status.WEAKENED:
                return Status.INFECTED
            case Status.INFECTED:
                return Status.FLAGGED
            case _:
                return Status.CLEAN


class Direction(Enum):
    UP = (0, -1)
    DOWN = (0, 1)
    LEFT = (-1, 0)
    RIGHT = (1, 0)

    def move(self, pos: tuple[int, int]) -> tuple[int, int]:
        return tuple(map(sum, zip(pos, self.value)))

    def turn_left(self) -> "Direction":
        match self:
            case Direction.UP:
                return Direction.LEFT
            case Direction.LEFT:
                return Direction.DOWN
            case Direction.DOWN:
                return Direction.RIGHT
            case _:
                return Direction.UP

    def turn_right(self) -> "Direction":
        match self:
            case Direction.UP:
                return Direction.RIGHT
            case Direction.RIGHT:
                return Direction.DOWN
            case Direction.DOWN:
                return Direction.LEFT
            case _:
                return Direction.UP

    def reverse(self) -> "Direction":
        match self:
            case Direction.UP:
                return Direction.DOWN
            case Direction.RIGHT:
                return Direction.LEFT
            case Direction.DOWN:
                return Direction.UP
            case _:
                return Direction.RIGHT


def parse_input() -> tuple[dict[tuple[int, int], Status], tuple[int, int]]:
    with open("day_22.txt") as f_in:
        lines = f_in.readlines()

    infected = {}

    for y, line in enumerate(lines):
        for x, c in enumerate(line.strip()):
            if c == "#":
                infected[(x, y)] = Status.INFECTED

    mid = math.floor(len(lines) / 2)

    return infected, (mid, mid)


def run_part_one() -> int:
    network, pos = parse_input()
    direction = Direction.UP
    infections_count = 0

    for _ in range(10_000):
        is_infected = network.get(pos, Status.CLEAN) == Status.INFECTED
        if is_infected:
            network[pos] = Status.CLEAN
            direction = direction.turn_right()
        else:
            network[pos] = Status.INFECTED
            infections_count += 1
            direction = direction.turn_left()
        pos = direction.move(pos)

    return infections_count


def run_part_two() -> int:
    network, pos = parse_input()
    direction = Direction.UP
    infections_count = 0

    for i in range(10_000_000):
        status = network.get(pos, Status.CLEAN)
        match status:
            case Status.CLEAN:
                direction = direction.turn_left()
            case Status.INFECTED:
                direction = direction.turn_right()
            case Status.FLAGGED:
                direction = direction.reverse()

        status = status.evolve()
        if status == Status.INFECTED:
            infections_count += 1

        if status == Status.CLEAN:
            network.pop(pos)
        else:
            network[pos] = status

        pos = direction.move(pos)

    return infections_count


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
