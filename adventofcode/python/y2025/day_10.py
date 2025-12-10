from dataclasses import dataclass
from typing import Any, Callable

from z3 import Int, Optimize, sat

type ConstraintFn = Callable[[list[int], int], Any]


@dataclass
class Machine:
    start_state_requirement: list[int]
    buttons: list[list[int]]
    joltage_requirement: list[int]

    @staticmethod
    def parse(line: str) -> "Machine":
        brackets_parts = line.strip().split("]")

        raw_start_state_requirement = brackets_parts[0][1:]
        start_state_requirement = [1 if c == '#' else 0 for c in raw_start_state_requirement]

        curly_parts = brackets_parts[1].split("{")
        raw_buttons = curly_parts[0].strip().split(" ")
        buttons = []
        for raw_button in raw_buttons:
            raw_button_impacted_indexes = raw_button[1:-1].split(",")
            current_button = [int(i) for i in raw_button_impacted_indexes]
            buttons.append(current_button)

        raw_joltage_requirements = curly_parts[1][:-1].split(",")
        joltage_requirements = [int(raw_joltage) for raw_joltage in raw_joltage_requirements]

        return Machine(
            start_state_requirement=start_state_requirement,
            buttons=buttons,
            joltage_requirement=joltage_requirements
        )

    def find_fewest_buttons_pressed_to_start_light(self) -> int:
        return self._find_fewest_buttons_pressed(
            requirements=self.start_state_requirement,
            constraint_fn=lambda impacted_buttons, req: sum(impacted_buttons) % 2 == req
        )

    def find_fewest_buttons_pressed_to_configure_joltage(self) -> int:
        return self._find_fewest_buttons_pressed(
            requirements=self.joltage_requirement,
            constraint_fn=lambda impacted_buttons, req: sum(impacted_buttons) == req
        )

    def _find_fewest_buttons_pressed(self, requirements: list[int], constraint_fn: ConstraintFn) -> int:
        opt = Optimize()

        variables = [Int(f"b{i}") for i in range(len(self.buttons))]
        constraints = [var >= 0 for var in variables]
        for indicator_idx, indicator_requirement in enumerate(requirements):
            impacted_buttons = []
            for button_idx, button in enumerate(self.buttons):
                # If this button impacts the current indicator
                if indicator_idx in button:
                    # Add the variable representing this button to the list of impacted buttons
                    impacted_buttons.append(variables[button_idx])
            # Add the constraint for this indicator based on the impacted buttons and the requirement
            constraints.append(constraint_fn(impacted_buttons, indicator_requirement))

        opt.add(*constraints)
        opt.minimize(sum(variables))
        if opt.check() == sat:
            m = opt.model()
            return sum([m[v].as_long() for v in variables])

        raise ValueError("No solution found")


def parse_input() -> list[Machine]:
    with open("day_10.txt") as f_in:
        lines = f_in.readlines()

    return [Machine.parse(line.strip()) for line in lines]


def run_part_one() -> int:
    machines = parse_input()
    total_fewest_buttons_pressed = 0
    for machine in machines:
        total_fewest_buttons_pressed += machine.find_fewest_buttons_pressed_to_start_light()
    return total_fewest_buttons_pressed


def run_part_two() -> int:
    machines = parse_input()
    total_fewest_buttons_pressed = 0
    for machine in machines:
        total_fewest_buttons_pressed += machine.find_fewest_buttons_pressed_to_configure_joltage()
    return total_fewest_buttons_pressed


if __name__ == '__main__':
    print(run_part_one())
    print(run_part_two())
