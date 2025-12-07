from dataclasses import dataclass
from enum import Enum


class Direction(Enum):
    LEFT = -1
    RIGHT = 1


@dataclass
class Operation:
    direction: Direction
    write_value: int
    next: str


@dataclass
class State:
    name: str
    case_zero: Operation
    case_one: Operation


states = {
    "A": State(
        name="A",
        case_zero=Operation(direction=Direction.RIGHT, write_value=1, next="B"),
        case_one=Operation(direction=Direction.LEFT, write_value=0, next="C"),
    ),
    "B": State(
        name="B",
        case_zero=Operation(direction=Direction.LEFT, write_value=1, next="A"),
        case_one=Operation(direction=Direction.RIGHT, write_value=1, next="D"),
    ),
    "C": State(
        name="C",
        case_zero=Operation(direction=Direction.RIGHT, write_value=1, next="A"),
        case_one=Operation(direction=Direction.LEFT, write_value=0, next="E"),
    ),
    "D": State(
        name="D",
        case_zero=Operation(direction=Direction.RIGHT, write_value=1, next="A"),
        case_one=Operation(direction=Direction.RIGHT, write_value=0, next="B"),
    ),
    "E": State(
        name="E",
        case_zero=Operation(direction=Direction.LEFT, write_value=1, next="F"),
        case_one=Operation(direction=Direction.LEFT, write_value=1, next="C"),
    ),
    "F": State(
        name="F",
        case_zero=Operation(direction=Direction.RIGHT, write_value=1, next="D"),
        case_one=Operation(direction=Direction.RIGHT, write_value=1, next="A"),
    ),
}


def run_part_one() -> int:
    tape = {}
    cursor = 0
    num_steps = 12_919_244
    current_state = "A"
    for i in range(num_steps):
        state = states[current_state]
        current_value = tape.get(cursor, 0)
        op = state.case_zero if current_value == 0 else state.case_one
        tape[cursor] = op.write_value
        current_state = op.next
        cursor += op.direction.value
    return sum(tape.values())


if __name__ == "__main__":
    print(run_part_one())
